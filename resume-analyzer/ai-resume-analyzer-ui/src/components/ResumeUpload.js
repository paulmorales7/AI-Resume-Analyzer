import React, { useState } from 'react';
import styles from './ResumeUpload.module.css';
import Analysis from './Analysis'; 

const ResumeUpload = ({ userName, onUpload }) => {
    const [selectedFile, setSelectedFile] = useState(null);
    const [uploadStatus, setUploadStatus] = useState('');
    const [jobPosting, setJobPosting] = useState('');
    const [results, setResults] = useState(null);

    

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
        formData.append('resume', selectedFile);
        formData.append('jobPosting', jobPosting);

        try {
            const response = await fetch (
                'http://localhost:8080/api/ai-analyze-resume', 
                {
                method: 'POST',
                body: formData
                });

                const data = await response.json();

                if (response.ok) {
                  setUploadStatus("Resume successfully analyzed");
                  onUpload(data);
                } else {
                  setUploadStatus(data.error || "Error analyzing resume.")
                }

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

              {userName && <h2>Hello, {userName}!</h2>}


              <h2>Upload Your Resume</h2>

              <form onSubmit={handleSubmit}>
                {/* File input: calls handleFileChange when the user selects a file */}
                <label htmlFor="file-upload" className={styles.customFileUpload}>
                    {selectedFile ? selectedFile.name : 'Choose File'}
                </label>
                <input 
                type="file" 
                onChange={handleFileChange} 
                accept=".pdf,.doc,.docx" 
                id="file-upload"
                style={{ display: 'none' }} // setting as none so i can style a label instead of having custom choose file btn
                />
                <br /><br />

                <textarea
                    placeholder="Paste the job posting here..."
                    value={jobPosting}
                    onChange={(e) => setJobPosting(e.target.value)}
                    className={styles.uploadTextArea}
                />


                {/* Submit button to trigger the file upload */}
                <button type="submit">Analyze</button>
              </form>


              {/* Display the upload status if available */}
              {uploadStatus && <p>{uploadStatus}</p>}

              {/* Display Results After Processing */}
              {results && <Analysis results={results}/>}
            </div>
          );
        };
        
        export default ResumeUpload;
