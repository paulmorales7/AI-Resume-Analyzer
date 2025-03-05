package com.paulmorales.resume_analyzer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ResumeController {

    private final ResumeAnalyzerService resumeAnalyzerService;

    @Autowired
    public ResumeController(ResumeAnalyzerService resumeAnalyzerService) {
        this.resumeAnalyzerService = resumeAnalyzerService;
    }

    @PostMapping("/analyze-resume")
    public ResponseEntity<ResumeAnalyzerService.AnalysisResult> analyzeResume(
            @RequestParam("resume") MultipartFile resume, 
            @RequestParam("jobPosting") String jobPosting) {
        try {
            // Here we pass the MultipartFile 'resume' and the jobPosting text as parameters
            ResumeAnalyzerService.AnalysisResult result = resumeAnalyzerService.analyzeResume(jobPosting, resume);

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
