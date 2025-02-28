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

    private static final String GROQ_API_URL = "https://api.groq.com/v1/chat/completions";

    // Example function to compare resume with job posting
    public static int calculateScore(MultipartFile resume, String jobPosting) {
        return new Random().nextInt(41) + 50; // Returns a random score between 50-90%
    }

    // Function to extract keywords using Groq API
    public List<String> extractKeywords(String jobPosting) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Set up headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + groqApiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create the prompt
            String prompt = "Extract key skills, programming languages, software, and technologies mentioned in this job posting:\n\n" + jobPosting;

            // Construct request body
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "gpt-4"); // Use the appropriate model
            requestBody.put("messages", new JSONArray()
                    .put(new JSONObject().put("role", "system").put("content", "You are an AI assistant that extracts key skills and software from job postings."))
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

                // Splitting response by commas to form a list of keywords
                return Arrays.asList(extractedText.split(",\\s*"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.singletonList("No specific keywords found");
    }
}
