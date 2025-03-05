import React, { useState } from 'react';
import styles from './ResumeUpload.module.css';

const ResumeUpload = ({ userName, onAnalysisComplete }) => {
  const [selectedFile, setSelectedFile] = useState(null);
  const [jobPosting, setJobPosting] = useState('');
  const [uploadStatus, setUploadStatus] = useState('');

  // Handle File Upload
  const handleFileChange = (e) => {
    setSelectedFile(e.target.files[0]);
  };

  // Handle Job Posting Text Change
  const handleJobPostingChange = (e) => {
    setJobPosting(e.target.value);
  };

  // Handle Analyze Button Click
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!selectedFile || !jobPosting.trim()) {
      setUploadStatus('Please upload a resume and paste the job posting.');
      return;
    }

    const formData = new FormData();
    formData.append('resume', selectedFile);
    formData.append('jobPosting', jobPosting);

    try {
      const response = await fetch('http://localhost:8080/api/ai-analyze-resume', {
        method: 'POST',
        body: formData,
      });

      const data = await response.json();

      if (response.ok) {
        setUploadStatus("Resume successfully analyzed");
        onAnalysisComplete(data);
      } else {
        setUploadStatus(data.error || "Error analyzing resume.");
      }
    } catch (error) {
      setUploadStatus(`Error uploading file: ${error.message}`);
    }
  };

  return (
    <div className={styles.uploadContainer}>
      <h2>Hello, {userName}! Upload Your Resume</h2>
      
      {/* File Upload Input */}
      <label htmlFor="resume-upload" className={styles.customFileUpload}>
        {selectedFile ? selectedFile.name : 'Choose Resume File'}
      </label>
      <input
        type="file"
        id="resume-upload"
        accept=".pdf,.doc,.docx"
        onChange={handleFileChange}
        style={{ display: 'none' }}
      />

      {/* Job Posting Text Area */}
      <textarea
        className={styles.uploadTextArea}
        placeholder="Paste the job posting here..."
        value={jobPosting}
        onChange={handleJobPostingChange}
      />

      {/* Analyze Button */}
      <button onClick={handleSubmit} className={styles.analyzeButton}>
        Analyze
      </button>

      {/* Upload Status */}
      {uploadStatus && <p>{uploadStatus}</p>}
    </div>
  );
};

export default ResumeUpload;
