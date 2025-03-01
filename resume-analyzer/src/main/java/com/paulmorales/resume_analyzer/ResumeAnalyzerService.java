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

    // Method to calculate compatibility score (using random value for now)
    public int calculateScore(MultipartFile resume, String jobPosting) {
        return new Random().nextInt(41) + 50;
    }

    // Function to extract keywords using Groq API
    public List<String> extractKeywords(String jobPosting) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Set up headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + groqApiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create the prompt to extract essential keywords from the job posting
            String prompt = "Extract key skills, qualifications, software, certifications, and experiences mentioned in this job posting. " +
                            "Return the list of essential keywords (skills, software, qualifications) and experience. Don't include unnecessary words:\n\n" + jobPosting;

            // Construct request body
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "llama-3.3-70b-versatile"); // Use the appropriate model
            requestBody.put("messages", new JSONArray()
                    .put(new JSONObject().put("role", "system").put("content", "You are an AI assistant that extracts essential skills, qualifications, software, certifications, and experiences from job postings."))
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

                // Split the extracted text into a list of keywords
                return Arrays.asList(extractedText.split(",\\s*"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.singletonList("No specific keywords found");
    }

    // Method to analyze a job posting and resume (returns keywords and feedback)
    public AnalysisResult analyzeResume(String jobPosting, MultipartFile resumeFile) throws IOException {
        // Convert resume MultipartFile to String
        String resumeText = new String(resumeFile.getBytes(), StandardCharsets.UTF_8);

        // Extract keywords from the job posting
        List<String> jobPostingKeywords = extractKeywords(jobPosting);

        // Check if the keywords are in the resumeText (this can be expanded)
        List<String> matchedKeywords = new ArrayList<>();
        for (String keyword : jobPostingKeywords) {
            if (resumeText.contains(keyword)) {
                matchedKeywords.add(keyword);
            }
        }

        // Generate feedback based on the matched keywords
        String feedback = "The resume contains the following keywords that match the job posting: " + String.join(", ", matchedKeywords) + ".\n" +
                          "Additional analysis could be done to refine the feedback.";

        // Return the analysis result with keywords, feedback, and compatibility score
        return new AnalysisResult(jobPostingKeywords, feedback, calculateScore(resumeFile, jobPosting));
    }

    // Nested static class to hold the analysis result (keywords, feedback, score)
    public static class AnalysisResult {
        private List<String> suggestedKeywords;
        private String feedback;
        private int score;

        // Constructor to initialize keywords, feedback, and score
        public AnalysisResult(List<String> suggestedKeywords, String feedback, int score) {
            this.suggestedKeywords = suggestedKeywords;
            this.feedback = feedback;
            this.score = score;
        }

        // Getters and setters
        public List<String> getSuggestedKeywords() {
            return suggestedKeywords;
        }

        public String getFeedback() {
            return feedback;
        }

        public int getScore() {
            return score;
        }
    }
}
