import React, { useState } from 'react';
import LandingPage from './components/LandingPage';
import ResumeUpload from './components/ResumeUpload';
import Analysis from './components/Analysis'; 

function App() {
  const [userName, setUserName] = useState('');
  const [hasContinued, setHasContinued] = useState(false);
  const [results, setResults] = useState(null);
  const [currentPage,setCurrentPage ] = useState('landing');

  const handleContinue = (name) => {
    setUserName(name);
    setHasContinued(true);
    setCurrentPage('resume-upload');
  };

  const handleResults = (data) => {
    setResults(data);
    setCurrentPage('analysis');
  }

  return (
    <div>
    {currentPage === 'landing' && <LandingPage onContinue={handleContinue} />}
    {currentPage === 'resume-upload' && (
      <ResumeUpload userName={userName} onUpload={handleResults} />
    )}
    {currentPage === 'analysis' && <Analysis results={results} />}
  </div>
  );
}

export default App;
