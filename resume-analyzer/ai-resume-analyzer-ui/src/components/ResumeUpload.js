import React, { useState } from 'react';
import styles from './ResumeUpload.module.css';

const ResumeUpload = ({ onUpload }) => {
    const [selectedFile, setSelectedFile] = useState(null);
    const [uploadStatus, setUploadStatus] = useState('');
    const [jobPosting, setJobPosting] = useState('');

    const handleFileChange = (e) => {
        // targeting the first file selected by user
        setSelectedFile(e.target.files[0]);
    };

    const handleSubmit = async (e) => {
        // dont allow for the form to be submitted empty which would reload the page
        e.preventDefault();
        if (!selectedFile) {
            // if nothing is selected
            setUploadStatus('Please select a file.');
            return;
        }

        const formData = new FormData();
        formData.append('file', selectedFile);

        try {
            const response = await fetch (
                'http://localhost:8080/api/resume/upload', 
                {
                method: 'POST',
                body: formData
                });

                const data = await response.text();

                setUploadStatus(data);

                if (onUpload) {
                    // only if data from upload is provided is res passed back
                    onUpload(data);
                } 
            } catch (error) {
                    setUploadStatus('Error uploading file: ' + error.message);
              }
        };

        return (
            <div style={{ textAlign: 'center', marginTop: '20px' }}>
              <h2>Upload Your Resume</h2>
              <form onSubmit={handleSubmit}>
                {/* File input: calls handleFileChange when the user selects a file */}
                <input type="file" onChange={handleFileChange} accept=".pdf,.doc,.docx" />
                <br /><br />
                {/* Submit button to trigger the file upload */}
                <button type="submit">Upload</button>
              </form>
              {/* Display the upload status if available */}
              {uploadStatus && <p>{uploadStatus}</p>}
              <textarea
                    placeholder="Paste the job posting here..."
                    value={jobPosting}
                    onChange={(e) => setJobPosting(e.target.value)}
                    className={styles.uploadTextArea}
                />
            </div>
          );
        };
        
        // Export the component so it can be imported and used in other parts of your app
        export default ResumeUpload;
    
