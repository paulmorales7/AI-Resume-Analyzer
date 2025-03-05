import React, { useState } from 'react';
import styles from './ResumeUpload.module.css'; // Assuming you are using CSS Modules

const ResumeUpload = ({ setAnalysisData }) => {
  const [resumeFile, setResumeFile] = useState(null);
  const [jobPosting, setJobPosting] = useState('');
  const [loading, setLoading] = useState(false);  // To manage loading state
  const [errorMessage, setErrorMessage] = useState(''); // To show errors to the user
  
  const handleFileChange = (e) => {
    setResumeFile(e.target.files[0]);
  };

  const handleJobPostingChange = (e) => {
    setJobPosting(e.target.value);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!resumeFile || !jobPosting) {
      alert("Please upload a resume and provide a job posting.");
      return;
    }

    setLoading(true);  // Set loading to true when starting the request
    setErrorMessage('');  // Clear previous errors

    // Prepare the form data for the API request
    const formData = new FormData();
    formData.append("resume", resumeFile);
    formData.append("jobPosting", jobPosting);

    try {
      // Send the POST request to the backend for AI analysis
      const response = await fetch("http://localhost:8080/api/analyze-resume", {
        method: "POST",
        body: formData,
      });

      if (!response.ok) {
        throw new Error('Error: ' + response.statusText);
      }

      const data = await response.json();

      if (data) {
        // Log the AI response in the terminal (VS Code)
        console.log("AI Feedback:", data);

        // Pass the AI data to App.js through the setAnalysisData function
        setAnalysisData(data);
      } else {
        console.log("No data received from AI.");
        setErrorMessage('No data received from AI.');
      }
    } catch (error) {
      console.error("Error uploading file:", error);
      setErrorMessage('There was an error processing your request. Please try again.');
    } finally {
      setLoading(false);  // Set loading to false once the request is complete
    }
  };

  return (
    <div>
      <h1>Upload Resume</h1>
      <form onSubmit={handleSubmit}>
        <div className={`form-group ${styles.formGroup}`}>
          <label htmlFor="resume-upload" className={`${styles.uploadLabel}`}>
            Upload Resume
          </label>
          <input
            type="file"
            id="resume-upload"
            onChange={handleFileChange}
            className={`${styles.fileInput}`}
          />
        </div>
        
        <div className={`form-group ${styles.formGroup}`}>
          <textarea
            placeholder="Job Posting"
            value={jobPosting}
            onChange={handleJobPostingChange}
            className={`${styles.uploadTextArea}`}
          />
        </div>
        
        <button type="submit" className={`${styles.analyzeButton}`} disabled={loading}>
          {loading ? 'Analyzing...' : 'Analyze Resume'}
        </button>
      </form>

      {errorMessage && <div className={styles.errorMessage}>{errorMessage}</div>}
    </div>
  );
};

export default ResumeUpload;
