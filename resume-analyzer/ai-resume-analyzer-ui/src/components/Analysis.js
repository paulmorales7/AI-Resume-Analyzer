import React from "react";
import styles from './Analysis.module.css';

const Analysis = ({ results }) => {

    if (!results) {
        return <p>No results available, please upload a resume and job posting first.</p>;
    }

    return(
        <div className={styles.resultsContainer}>
            <h3>Analysis Results</h3>

            {/* Display Compatibility Score */}
            <h4>Compatibility Score:</h4>
            {results.compatibility ? (
                <p>{results.compatibility}%</p>
            ) : (
                <p>No compatibility score available.</p>
            )}

            {/* Display Missing Requirements */}
            <h4>Missing Requirements:</h4>
            {results.missingRequirements && results.missingRequirements.length > 0 ? (
                <ul>
                    {results.missingRequirements.map((req, index) => (
                        <li key={index}>{req}</li>
                    ))}
                </ul>
            ) : (
                <p>No missing requirements identified.</p>
            )}

            {/* Display Suggested Keywords */}
            <h4>Suggested Keywords:</h4>
            {results.keywords && results.keywords.length > 0 ? (
                <ul>
                    {results.keywords.map((keyword, index) => (
                        <li key={index}>{keyword}</li>
                    ))}
                </ul>
            ) : (
                <p>No suggested keywords available.</p>
            )}

            {/* Display Feedback */}
            <h4>Feedback:</h4>
            {results.feedback ? (
                <p>{results.feedback}</p>
            ) : (
                <p>No feedback available.</p>
            )}
        </div>
    );
};

export default Analysis;
