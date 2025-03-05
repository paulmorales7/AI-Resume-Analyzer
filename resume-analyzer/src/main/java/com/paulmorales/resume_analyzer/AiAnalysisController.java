package com.paulmorales.resume_analyzer;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AiAnalysisController {

    private final AIanalysisService aiAnalysisService;

    public AiAnalysisController(AIanalysisService aiAnalysisService) {
        this.aiAnalysisService = aiAnalysisService;
    }

    @PostMapping("/ai-analyze-resume")
    public ResponseEntity<AnalysisResult> analyzeResume(
            @RequestParam("resumeFile") MultipartFile resumeFile,
            @RequestParam("jobPosting") String jobPosting) {

        if (resumeFile.isEmpty() || jobPosting.isBlank()) {
            return ResponseEntity.badRequest().body(new AnalysisResult(List.of(), "Resume file and job posting are required."));
        }

        AnalysisResult result = aiAnalysisService.analyzeResume(resumeFile, jobPosting);
        return ResponseEntity.ok(result);
    }

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

        public String getFeedback() {
            return feedback;
        }
    }
}
