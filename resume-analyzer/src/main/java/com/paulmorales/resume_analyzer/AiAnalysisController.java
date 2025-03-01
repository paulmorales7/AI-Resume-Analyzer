package com.paulmorales.resume_analyzer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AiAnalysisController {

    private final ResumeAnalyzerService resumeAnalyzerService;

    // Constructor injection for ResumeAnalyzerService
    @Autowired
    public AiAnalysisController(ResumeAnalyzerService resumeAnalyzerService) {
        this.resumeAnalyzerService = resumeAnalyzerService;
    }

    // POST endpoint for analyzing a job posting
    @PostMapping("/analyze-resume")
    public ResponseEntity<ResumeAnalyzerService.AnalysisResult> analyzeResume(@RequestBody String resumeText) {
        try {
            // Call the ResumeAnalyzerService to get the analysis result
            ResumeAnalyzerService.AnalysisResult result = resumeAnalyzerService.analyzeResume(resumeText);

            // Return the result with HTTP status OK
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception (optional, for debugging)
            e.printStackTrace();

            // Return error response with INTERNAL_SERVER_ERROR status
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
