package com.paulmorales.resume_analyzer;

import org.springframework.stereotype.Service;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

// Import the AnalysisResult class
import com.paulmorales.resume_analyzer.AiAnalysisController.AnalysisResult;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AIanalysisService {

    private final Tika tika = new Tika();

    public AnalysisResult analyzeResume(MultipartFile resumeFile, String jobPosting) {
        try {
            // Extract text from the uploaded resume file
            String resumeText = extractTextFromFile(resumeFile);

            // Analyze resume against job posting
            return performAnalysis(resumeText, jobPosting);

        } catch (IOException e) {
            return new AnalysisResult(new ArrayList<>(), "Error reading resume file: " + e.getMessage());
        }
    }

    private String extractTextFromFile(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            return tika.parseToString(inputStream);
        } catch (TikaException e) {
            throw new IOException("Error accessing text from file: " + e.getMessage(), e);
        }
    }

    private AnalysisResult performAnalysis(String resumeText, String jobPosting) {
        List<String> missingSkills = new ArrayList<>();

        // Simple keyword matching logic (replace with AI logic later)
        if (!resumeText.toLowerCase().contains("java")) {
            missingSkills.add("Java");
        }
        if (!resumeText.toLowerCase().contains("spring boot")) {
            missingSkills.add("Spring Boot");
        }

        String feedback = missingSkills.isEmpty() ?
                "Your resume matches the job posting well!" :
                "Consider adding experience with: " + String.join(", ", missingSkills);

        return new AnalysisResult(missingSkills, feedback);
    }
}
