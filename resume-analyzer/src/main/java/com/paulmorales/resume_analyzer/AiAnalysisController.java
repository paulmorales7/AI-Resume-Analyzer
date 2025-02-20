package com.paulmorales.resume_analyzer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AiAnalysisController {

    // variable for our service file methods which must be initialized
    private final AIanalysisService aianalysisService;

    // constructor injection (to ensure that the service is provided when the controller is created)
    @Autowired
    public AiAnalysisController(AIanalysisService aianalysisService) {
        this.aianalysisService = aianalysisService;
    }

    //POST endpoint for text analysis
    @PostMapping("/analyze-resume")
    public String analyzeResume(@RequestBody String resumeText) {
        return aianalysisService.analyzeResume(resumeText);
    }
}

