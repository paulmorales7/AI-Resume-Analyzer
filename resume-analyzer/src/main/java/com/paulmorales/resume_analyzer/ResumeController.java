
package com.paulmorales.resume_analyzer;

import org.springframework.web.bind.annotation.*; // used to define API controller
import org.springframework.web.multipart.MultipartFile; // used to handle file upload (resumes)
import org.springframework.http.ResponseEntity; // used to send responses with HTTP status codes
import org.springframework.http.HttpStatus;

import java.util.*;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
 // allowing CORS in react
public class ResumeController {

    private final ResumeAnalyzerService resumeAnalyzerService;

    // Inject ResumeAnalyzerService using constructor
    public ResumeController(ResumeAnalyzerService resumeAnalyzerService) {
        this.resumeAnalyzerService = resumeAnalyzerService;
    }

    @PostMapping("/upload") // creating HTTP POST endpoint for upload
    public ResponseEntity<Map<String, Object>> analyzeResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("jobPosting") String jobPosting) {

        if (file == null || file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "No file uploaded or file is empty"));
        }

        try {
            String filename = file.getOriginalFilename();
            System.out.println("Received file: " + filename);
            System.out.println("Received job posting text: " + jobPosting);

            // Analyze resume (this is placeholder logic, replace with actual processing)
            int score = ResumeAnalyzerService.calculateScore(file, jobPosting);
            List<String> suggestedKeywords = resumeAnalyzerService.extractKeywords(jobPosting);
            String feedback = "Your resume matches " + score + "% of the job posting. "
                    + "Consider adding these keywords: " + String.join(", ", suggestedKeywords);

            // Create response JSON
            Map<String, Object> response = new HashMap<>();
            response.put("score", score);
            response.put("suggestedKeywords", suggestedKeywords);
            response.put("feedback", feedback);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error processing resume: " + e.getMessage()));
        }
    }
}

