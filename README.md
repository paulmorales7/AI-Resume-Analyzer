# AI Resume Analyzer

<img width="955" alt="Screenshot 2025-03-06 at 6 21 12 PM" src="https://github.com/user-attachments/assets/44192371-39b1-4cb3-9fc3-a31754e15c40" />


## Overview
The AI Resume Analyzer helps users compare their resumes against job descriptions using AI to provide feedback and compatibility scores. Highlighting any key qualifications the user may be missing. Ultimately assisting the user in formulating the strongest resume possible.

## Features
- Upload a resume and extract text.
- Compare the resume with a job description.
- Get a compatibility score and suggestions for improvement.

### Core Technologies  
- **Backend:** Java (Spring Boot 3.4.2)  
- **Frontend:** React (JavaScript)  
- **Database:** Not yet implemented 

### Development Tools  
- **Version Control:** Git, GitHub  
- **Build & Dependency Management:**  
  - **Backend:** Maven  
  - **Frontend:** npm (Node.js)  
- **Package Manager:** npm (for React dependencies)  
- **API Handling:** Spring Boot REST APIs  
- **HTTP Client:** Fetch API (or Axios if added later)  

## Setup Instructions

### Backend Setup
1. Navigate to the backend folder:
   ```sh
   cd backend
   ```
2. Build and run the backend:
   ```sh
   mvn clean install  
   mvn spring-boot:run  
   ```
3. The backend should now be running at [http://localhost:8080](http://localhost:8080).

### Frontend Setup
1. Navigate to the frontend folder:
   ```sh
   cd ../frontend
   ```
2. Install dependencies:
   ```sh
   npm install
   ```
3. Start the frontend:
   ```sh
   npm start
   ```
4. Open [http://localhost:3000](http://localhost:3000) in your browser.

5. Ensure the Groq api model being used accepts aleast 40,000+ tokens per call.
   (https://console.groq.com/docs/rate-limits)

## API Endpoints

### Resume Upload
- **Endpoint:** `POST /api/resume/upload`
- **Request:** FormData containing a resume file
- **Response:** JSON with extracted text

### Job Description Input
- **Endpoint:** `POST /api/job/description`
- **Request:** JSON with job description text
- **Response:** Compatibility score and suggestions

## CORS Issue Fix
If you encounter CORS issues (e.g., *"Blocked by CORS policy"*), add this to your Spring Boot configuration:

```java
@Configuration  
public class CorsConfig {  
    @Bean  
    public WebMvcConfigurer corsConfigurer() {  
        return new WebMvcConfigurer() {  
            @Override  
            public void addCorsMappings(CorsRegistry registry) {  
                registry.addMapping("/**")  
                        .allowedOrigins("http://localhost:3000")  
                        .allowedMethods("GET", "POST", "PUT", "DELETE")  
                        .allowedHeaders("*");  
            }  
        };  
    }  
}
```

## Contributing
Pull requests are welcome! Please open an issue first to discuss major changes.

## License
MIT License – Free to use and modify.
