import React from 'react';
import styles from './Analysis.module.css';

const Analysis = ({ analysisData }) => {
  if (!analysisData) {
    return <div>Loading...</div>; // or any other loading indicator
  }

  const { keywords, missingRequirements, feedback } = analysisData;

  // Add default values in case the properties are undefined
  const keywordsList = keywords && keywords.length > 0 ? keywords : 'No keywords provided';
  const missingRequirementsText = missingRequirements || 'No missing requirements provided';
  const feedbackText = feedback || 'No feedback provided';

  return (
    <div>
      <h2>AI Analysis</h2>
      <div>
        <h3>Keywords</h3>
        <p>{keywordsList}</p>
      </div>
      <div>
        <h3>Missing Requirements</h3>
        <p>{missingRequirementsText}</p>
      </div>
      <div>
        <h3>Feedback</h3>
        <p>{feedbackText}</p>
      </div>
    </div>
  );
};

export default Analysis;
