package com.paulmorales.resume_analyzer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Service
public class AIanalysisService {
    //added this to secure the API key info.
    @Value("${groq.api.key}")
    private String apiKey;
    private final String groqApiUrl = "https://api.groq.com/openai/v1/chat/completions";

    private final RestTemplate restTemplate;

    // Constructer of RTBuilder to initilize RT
    public AIanalysisService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public String analyzeResume(String resumeText) {
        //Headers for req
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        // JSON format
        String requestBody = "{"
                + "\"model\": \"llama-3.3-70b-versatile\", "  
                + "\"messages\": [{\"role\": \"system\", \"content\": \"You are an AI resume analyst.\"},"
                + "{\"role\": \"user\", \"content\": \"" + escapeJSON(resumeText) + "\"}],"
                + "\"max_tokens\": 500"
                + "}";


        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        //Make API req and return the response body
        // return message instead of error
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

