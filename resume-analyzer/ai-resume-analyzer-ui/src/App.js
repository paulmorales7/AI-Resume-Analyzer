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
        <div style={{ padding: '20px' }}>
          <h2>Hello, {userName}!</h2>
          {/* Here you'll add the main UI components later */}
          <p>This is the main page. More components will come here.</p>

          <ResumeUpload />
        </div>
      )}
    </div>
  );
}

export default App;
