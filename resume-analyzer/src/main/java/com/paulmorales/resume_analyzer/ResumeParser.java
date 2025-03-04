package com.paulmorales.resume_analyzer;

import java.util.regex.*;
import java.util.*;

public class ResumeParser {

    public static String extractRelevantInfo(String resumeText) {
        // Extract sections based on keywords
        StringBuilder relevantInfo = new StringBuilder();

        String[] sections = {
            "summary", "objective", "experience", "education", "skills", "certifications"
        };

        for (String section : sections) {
            Pattern pattern = Pattern.compile("(?i)" + section + "[:\\n]+(.*?)(?=(\\n[A-Z]|$))", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(resumeText);
            if (matcher.find()) {
                relevantInfo.append(section.toUpperCase()).append(":\n").append(matcher.group(1).trim()).append("\n\n");
            }
        }

        // If no sections found, return original text (fallback)
        return relevantInfo.length() > 0 ? relevantInfo.toString() : resumeText;
    }
}
