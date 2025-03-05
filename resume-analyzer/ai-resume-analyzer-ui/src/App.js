import React, { useState } from 'react';
import LandingPage from './components/LandingPage';
import ResumeUpload from './components/ResumeUpload';
import Analysis from './components/Analysis';

const App = () => {
  const [step, setStep] = useState('landing'); // Manage the step to control component flow
  const [analysisData, setAnalysisData] = useState(null); // Store analysis data

  // Function to handle name submit (for LandingPage)
  const onNameSubmit = (name) => {
    console.log("Name submitted:", name); // Can handle name logic here
    setStep('upload'); // Change step to show the upload component after submitting the name
  };

  // Function to pass AI feedback data to App
  const handleAnalysisData = (data) => {
    setAnalysisData(data); // Set analysis data
    setStep('analysis'); // Switch to Analysis page
  };

  const handleBackToUpload = () => {
    setStep('upload');
  };

  return (
    <div>
      {step === 'landing' && <LandingPage onNameSubmit={onNameSubmit} />}
      {step === 'upload' && <ResumeUpload setAnalysisData={handleAnalysisData} />}
      {step === 'analysis' && <Analysis data={analysisData} onBack={handleBackToUpload} />}
    </div>
  );
};

export default App;
