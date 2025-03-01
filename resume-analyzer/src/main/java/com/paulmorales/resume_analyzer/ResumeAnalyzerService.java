package com.paulmorales.resume_analyzer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ResumeAnalyzerService {

    @Value("${groq.api.key}")
    private String groqApiKey;

    private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";

    // Function to analyze the resume using the Groq API
    public AnalysisResult analyzeResume(String jobPosting) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Set up headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + groqApiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create the prompt for Groq API
            String prompt = "Extract key skills, programming languages, software, and technologies mentioned in this job posting. " +
                            "Then, provide feedback on the compatibility between these and a typical resume. " +
                            "Return the extracted keywords in a numbered list, followed by the feedback paragraph.\n\n" + jobPosting;

            // Construct request body
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "llama-3.3-70b-versatile");
            requestBody.put("messages", new JSONArray()
                    .put(new JSONObject().put("role", "system").put("content", "You are an AI assistant that analyzes job postings and resumes."))
                    .put(new JSONObject().put("role", "user").put("content", prompt))
            );
            requestBody.put("temperature", 0.3);

            // Make API request
            HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
            ResponseEntity<String> response = restTemplate.exchange(GROQ_API_URL, HttpMethod.POST, entity, String.class);

            // Parse the response
            JSONObject jsonResponse = new JSONObject(response.getBody());
            JSONArray choices = jsonResponse.getJSONArray("choices");

            if (choices.length() > 0) {
                String extractedText = choices.getJSONObject(0).getJSONObject("message").getString("content");

                // Splitting response into keywords and feedback
                String[] parts = extractedText.split("\n\n");
                List<String> keywords = new ArrayList<>();
                String feedback = "";

                if (parts.length > 0) {
                    String[] keywordLines = parts[0].split("\n");
                    for (String line : keywordLines) {
                        if (!line.trim().isEmpty()) {
                            keywords.add(line.trim());
                        }
                    }
                }
                if (parts.length > 1) {
                    feedback = parts[1].trim();
                }

                // Return both keywords and feedback as part of an AnalysisResult
                return new AnalysisResult(keywords, feedback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AnalysisResult(Collections.singletonList("No specific keywords found"), "No feedback provided.");
    }

    // Helper class to hold the result
    public static class AnalysisResult {
        private List<String> keywords;
        private String feedback;

        public AnalysisResult(List<String> keywords, String feedback) {
            this.keywords = keywords;
            this.feedback = feedback;
        }

        public List<String> getKeywords() {
            return keywords;
        }

        public void setKeywords(List<String> keywords) {
            this.keywords = keywords;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }
    }
}
