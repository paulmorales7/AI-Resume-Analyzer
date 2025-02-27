import React, {useState} from 'react';
import styles from './LandingPage.module.css';
import bgImage from '../assets/aiBG.jpg';

const LandingPage = ({ onContinue }) => {
    const [name, setName] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        if (name.trim()) {
            onContinue(name);
        }
    }

    return (
        <div 
          className={styles.landingContainer}
          style={{bgImage: `url(${bgImage})`}}
        >
          <h1>Welcome to the AI Resume Analyzer!</h1>
          <form onSubmit={handleSubmit} className={styles.landingForm}>
            <input
              type="text"
              placeholder="Enter your name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className={styles.landingInput}
            />
            <button type="submit" className={styles.landingButton}>Continue</button>
          </form>
        </div>
      );
    };

    export default LandingPage;
