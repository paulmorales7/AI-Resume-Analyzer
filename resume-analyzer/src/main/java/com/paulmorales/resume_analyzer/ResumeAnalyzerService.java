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
    private static final int MAX_TOKENS = 15000;  // Max token limit for the AI API
    private static final int MAX_TOKENS_PER_REQUEST = 2000;  // Reduced to ensure AI payload is manageable

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

    // Extracts relevant sections of the resume
    private String extractRelevantInfo(String resumeText) {
        StringBuilder relevantInfo = new StringBuilder();

        String[] sections = {"experience", "skills", "education"};
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

    // Analyzes the resume, returns missing requirements and a small feedback paragraph
    public AnalysisResult analyzeResume(String jobPosting, MultipartFile resumeFile) throws IOException {
        String fullResumeText = new String(resumeFile.getBytes(), StandardCharsets.UTF_8);
        String relevantResumeText = extractRelevantInfo(fullResumeText); // Extract only relevant sections

        // Provide a small feedback paragraph
        String feedback = callGroqApi("Provide general feedback on this resume:\n\n" + relevantResumeText);

        // Return result with feedback
        return new AnalysisResult("", "", feedback);
    }

    // Holds the analysis result
    public static class AnalysisResult {
        private final String keywords;
        private final String missingRequirements;
        private final String feedback;

        public AnalysisResult(String keywords, String missingRequirements, String feedback) {
            this.keywords = keywords;
            this.missingRequirements = missingRequirements;
            this.feedback = feedback;
        }

        public String getKeywords() {
            return keywords;
        }

        public String getMissingRequirements() {
            return missingRequirements;
        }

        public String getFeedback() {
            return feedback;
        }
    }
}
