package com.paulmorales.resume_analyzer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AIanalysisService {
    @Value("${groq.api.key}")
    private String apiKey;
    private final String groqApiUrl = "https://api.groq.com/openai/v1/chat/completions";

    private final RestTemplate restTemplate;

    public AIanalysisService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public String analyzeResume(String resumeText) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        String requestBody = "{"
                + "\"model\": \"llama-guard-3-8b\", "  
                + "\"messages\": [{\"role\": \"system\", \"content\": \"You are an AI resume analyst.\"},"
                + "{\"role\": \"user\", \"content\": \"" + escapeJSON(resumeText) + "\"}],"
                + "\"max_tokens\": 500"
                + "}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try { 
            ResponseEntity<String> response = restTemplate.exchange(groqApiUrl, HttpMethod.POST, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            return "Error calling Groq api: " + e.getMessage();
        }
    }

    private String escapeJSON(String text) {
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r");
    }
}
