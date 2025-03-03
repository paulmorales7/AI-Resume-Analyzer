import React, { useState } from 'react';
import LandingPage from './components/LandingPage';
import ResumeUpload from './components/ResumeUpload';

function App() {
  const [userName, setUserName] = useState('');
  const [hasContinued, setHasContinued] = useState(false);

  const handleContinue = (name) => {
    setUserName(name);
    setHasContinued(true);
  };

  return (
    <div>
      {!hasContinued ? (
        <LandingPage onContinue={handleContinue} />
        
      ) : (
      
        <ResumeUpload userName={userName}/>
        
      )}
    </div>
  );
}

export default App;
