import React, { useState } from 'react';
import styles from './LandingPage.module.css';

const LandingPage = ({ onNameSubmit }) => {
    const [name, setName] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        if (name) {
            onNameSubmit(name);  // This will call the onNameSubmit passed from App.js
        } else {
            alert('Please enter your name.');
        }
    };

    return (
        <div className={styles.landingContainer}>
            <h1>Welcome to AI Resume Analyzer</h1>
            <form className={styles.landingForm} onSubmit={handleSubmit}>
                <input
                    className={styles.landingInput}
                    type="text"
                    placeholder="Enter your name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                />
                <button type="submit" className={styles.landingButton}>
                    Submit
                </button>
            </form>
        </div>
    );
};

export default LandingPage;
