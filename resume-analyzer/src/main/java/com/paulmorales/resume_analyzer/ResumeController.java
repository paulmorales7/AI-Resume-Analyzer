
package com.paulmorales.resume_analyzer;

import org.springframework.web.bind.annotation.*; // used to define API controller
import org.springframework.web.multipart.MultipartFile; // used to handle file upload (resumes)
import org.springframework.http.ResponseEntity; // used to send responses with HTTP status codes

@RestController
@RequestMapping("/api/resume")
@CrossOrigin(origins = "http://localhost:3000") // allowing CORS in react
public class ResumeController {

    @PostMapping("/upload") // creating HTTP POST endpoint for upload
    public ResponseEntity<String> uploadResume(@RequestParam("file") MultipartFile file) {
        // location for uploaded resume

        if (file == null || file.isEmpty()) {
            return ResponseEntity.status(400).body("No file uploaded or file is empty");
        }
        
        try {
            String filename = file.getOriginalFilename();

            System.out.println("received file: " + filename);
            return ResponseEntity.ok("File uploaded successfully: " + filename); // let user know the resquest was successful
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to upload file: " + e.getMessage()); //HTTP 500 is Internal Server Error
        }
    }
    
}

