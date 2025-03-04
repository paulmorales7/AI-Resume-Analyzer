import React from "react";
import styles from './Analysis.module.css';

const Analysis = ({ results }) => {

    if (!results) {
        return <p>No Results available, please upload a resume file first.</p>
    }
    
    return(
        <div className={styles.resultsContainer}>
      <h3>Analysis Results</h3>
      <p><strong>Compatibility Score:</strong> {results.score}%</p>
      <h4>Suggested Keywords:</h4>
      {results?.suggestedKeywords?.length > 0 ? (
        <ul>
          {results.suggestedKeywords.map((keyword, index) => (
            <li key={index}>{keyword}</li>
          ))}
        </ul>
      ) : (
        <p>No suggested keywords.</p>
      )}
      <h4>Feedback:</h4>
      <p>{results.feedback}</p>
    </div>

    )
}

export default Analysis;