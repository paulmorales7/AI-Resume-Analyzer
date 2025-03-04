package com.paulmorales.resume_analyzer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.json.JSONObject;
import org.json.JSONArray;

import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.*;

@Service
public class ResumeAnalyzerService {

    @Value("${groq.api.key}")
    private String groqApiKey;

    private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";

    private static final int MAX_TOKENS = 6000;

    // Function to call Groq API with a specific prompt
    private String callGroqApi(String prompt) {
        try {
            System.out.println("Sending request to Groq API...");
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + groqApiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);
    
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "llama-guard-3-8b");
            requestBody.put("messages", new JSONArray()
                    .put(new JSONObject().put("role", "system").put("content", "You are an AI assistant helping with resume analysis."))
                    .put(new JSONObject().put("role", "user").put("content", prompt))
            );
            requestBody.put("temperature", 0.3);
    
            System.out.println("Request body: " + requestBody.toString());
    
            HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
            ResponseEntity<String> response = restTemplate.exchange(GROQ_API_URL, HttpMethod.POST, entity, String.class);
            
            System.out.println("Response received: " + response.getBody());
    
            JSONObject jsonResponse = new JSONObject(response.getBody());
            JSONArray choices = jsonResponse.getJSONArray("choices");
    
            if (choices.length() > 0) {
                return choices.getJSONObject(0).getJSONObject("message").getString("content");
            }
        } catch (Exception e) {
            System.out.println("Error calling Groq API: " + e.getMessage());
            e.printStackTrace();
        }
        return "Error processing request.";
    }

    // Summarize text to fit token limit
    private String summarizeText(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "... (truncated)";
    }

    // Extract essential keywords from the job posting
    public String extractKeywords(String jobPosting) {
        String shortJobPosting = summarizeText(jobPosting, MAX_TOKENS / 3);
        String prompt = "Extract key skills, technologies, tools, databases, and certifications mentioned in this job posting. Return as a simple list.\n\n" + shortJobPosting;
        return callGroqApi(prompt);
    }

    // Identify missing requirements from the resume compared to the job posting
    public String identifyMissingRequirements(String jobPosting, String resumeText) {
        String shortJobPosting = summarizeText(jobPosting, MAX_TOKENS / 3);
        String shortResume = summarizeText(resumeText, MAX_TOKENS / 3);
        String prompt = "Compare the following resume to the job posting. Identify the main missing skills, experience, or qualifications:\n\nJob Posting:\n" + shortJobPosting + "\n\nResume:\n" + shortResume;
        return callGroqApi(prompt);
    }

    // Assess the candidate's overall preparedness
    public String assessPreparedness(String jobPosting, String resumeText) {
        String shortJobPosting = summarizeText(jobPosting, MAX_TOKENS / 3);
        String shortResume = summarizeText(resumeText, MAX_TOKENS / 3);
        String prompt = "Analyze this resume against the job posting and provide a short assessment of the candidate’s overall preparedness for the role.\n\nJob Posting:\n" + shortJobPosting + "\n\nResume:\n" + shortResume;
        return callGroqApi(prompt);
    }

    // Analyze resume and job posting
    public AnalysisResult analyzeResume(String jobPosting, MultipartFile resumeFile) throws IOException {
        String resumeText = new String(resumeFile.getBytes(), StandardCharsets.UTF_8);
        String keywords = extractKeywords(jobPosting);
        String missingRequirements = identifyMissingRequirements(jobPosting, resumeText);
        String preparedness = assessPreparedness(jobPosting, resumeText);

        return new AnalysisResult(keywords, missingRequirements, preparedness);
    }

    // Nested static class to hold the analysis result
    public static class AnalysisResult {
        private String keywords;
        private String missingRequirements;
        private String preparedness;

        public AnalysisResult(String keywords, String missingRequirements, String preparedness) {
            this.keywords = keywords;
            this.missingRequirements = missingRequirements;
            this.preparedness = preparedness;
        }

        public String getKeywords() {
            return keywords;
        }

        public String getMissingRequirements() {
            return missingRequirements;
        }

        public String getPreparedness() {
            return preparedness;
        }
    }
}
