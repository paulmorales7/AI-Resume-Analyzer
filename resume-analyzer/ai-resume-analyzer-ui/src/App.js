import React, { useState } from 'react';
import LandingPage from './components/LandingPage';
import ResumeUpload from './components/ResumeUpload';
import Analysis from './components/Analysis';

const App = () => {
  const [step, setStep] = useState(1);
  const [userName, setUserName] = useState('');
  const [results, setResults] = useState(null);

  const handleNameSubmit = (name) => {
    setUserName(name);
    setStep(2);
  };

  const handleAnalysisComplete = (data) => {
    setResults(data);
    setStep(3);
  };

  return (
    <div>
      {step === 1 && <LandingPage onNameSubmit={handleNameSubmit} />}
      {step === 2 && <ResumeUpload userName={userName} onAnalysisComplete={handleAnalysisComplete} />}
      {step === 3 && <Analysis results={results} />}
    </div>
  );
};

export default App;
