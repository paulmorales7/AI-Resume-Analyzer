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

@Service
public class ResumeAnalyzerService {

    @Value("${groq.api.key}")
    private String groqApiKey;

    private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final int MAX_TOKENS = 5000;  // Reduced to ensure API efficiency

    // Calls the Groq API
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

            HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
            ResponseEntity<String> response = restTemplate.exchange(GROQ_API_URL, HttpMethod.POST, entity, String.class);

            JSONObject jsonResponse = new JSONObject(response.getBody());
            JSONArray choices = jsonResponse.optJSONArray("choices");

            if (choices != null && choices.length() > 0) {
                return choices.getJSONObject(0).getJSONObject("message").getString("content");
            } else {
                return "No valid response from AI.";
            }
        } catch (Exception e) {
            System.out.println("Error calling Groq API: " + e.getMessage());
            return "Error processing request. Please try again.";
        }
    }

    // Extracts key sections from resume
    private String extractRelevantInfo(String resumeText) {
        StringBuilder relevantInfo = new StringBuilder();

        String[] sections = {"summary", "objective", "experience", "education", "skills", "certifications"};
        for (String section : sections) {
            if (resumeText.toLowerCase().contains(section)) {
                relevantInfo.append(section.toUpperCase()).append(":\n");
                int startIndex = resumeText.toLowerCase().indexOf(section);
                int endIndex = Math.min(startIndex + 800, resumeText.length());  // Limit each section to 800 chars
                relevantInfo.append(resumeText.substring(startIndex, endIndex)).append("\n\n");
            }
        }
        return relevantInfo.length() > 0 ? relevantInfo.toString() : resumeText;  // Use full text as fallback
    }

    // Extracts keywords from the job posting
    public String extractKeywords(String jobPosting) {
        String prompt = "Extract key skills, technologies, tools, databases, and certifications mentioned in this job posting. Return as a list.\n\n" + jobPosting;
        return callGroqApi(prompt);
    }

    // Identifies missing requirements
    public String identifyMissingRequirements(String jobPosting, String resumeText) {
        String relevantResumeText = extractRelevantInfo(resumeText);
        String prompt = "Compare this resume to the job posting. Identify missing skills, experience, or qualifications:\n\nJob Posting:\n" 
                        + jobPosting + "\n\nResume:\n" + relevantResumeText;
        return callGroqApi(prompt);
    }

    // Assesses candidate preparedness
    public String assessPreparedness(String jobPosting, String resumeText) {
        String relevantResumeText = extractRelevantInfo(resumeText);
        String prompt = "Analyze this resume against the job posting and provide a short assessment of the candidate’s preparedness.\n\nJob Posting:\n" 
                        + jobPosting + "\n\nResume:\n" + relevantResumeText;
        return callGroqApi(prompt);
    }

    // Analyzes the resume
    public AnalysisResult analyzeResume(String jobPosting, MultipartFile resumeFile) throws IOException {
        String fullResumeText = new String(resumeFile.getBytes(), StandardCharsets.UTF_8);
        String relevantResumeText = extractRelevantInfo(fullResumeText);

        String keywords = extractKeywords(jobPosting);
        String missingRequirements = identifyMissingRequirements(jobPosting, relevantResumeText);
        String preparedness = assessPreparedness(jobPosting, relevantResumeText);

        return new AnalysisResult(keywords, missingRequirements, preparedness);
    }

    // Holds the analysis result
    public static class AnalysisResult {
        private final String keywords;
        private final String missingRequirements;
        private final String preparedness;

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
