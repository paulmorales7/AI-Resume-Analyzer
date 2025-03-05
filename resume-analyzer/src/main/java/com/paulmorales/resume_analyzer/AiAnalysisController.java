package com.paulmorales.resume_analyzer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AiAnalysisController {

    private final AIanalysisService aiAnalysisService;

    @Autowired
    public AiAnalysisController(AIanalysisService aiAnalysisService) {
        this.aiAnalysisService = aiAnalysisService;
    }

    @PostMapping("/ai-analyze-resume")
    public ResponseEntity<Object> analyzeResume(
            @RequestParam("resume") MultipartFile resume,
            @RequestParam("jobPosting") String jobPosting) {
        try {
            String resumeText = new String(resume.getBytes());

            // Call the AI analysis service to get the analysis result
            AnalysisResult result = aiAnalysisService.analyzeResume(resumeText, jobPosting);

            // Return the result with HTTP status OK
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            // Return a proper error message in JSON format
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error calling Groq API: " + e.getMessage()));
        }
    }

    // Error response class to return consistent error structure
    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    // Analysis result class to structure the response
    public static class AnalysisResult {
        private List<String> missingSkills;
        private String feedback;

        public AnalysisResult(List<String> missingSkills, String feedback) {
            this.missingSkills = missingSkills;
            this.feedback = feedback;
        }

        public List<String> getMissingSkills() {
            return missingSkills;
        }

        public void setMissingSkills(List<String> missingSkills) {
            this.missingSkills = missingSkills;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }
    }
}
