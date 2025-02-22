import react, {useState} from 'react';
import styles from './LandingPage.module.css';

const LandingPage = ({ onContinue }) => {
    const [name, setName] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        if (name.trim()) {
            onContinue(name);
        }
    }

    return (
        <div style={styles.container}>
          <h1>Welcome to the AI Resume Analyzer!</h1>
          <form onSubmit={handleSubmit} style={styles.form}>
            <input
              type="text"
              placeholder="Enter your name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              style={styles.input}
            />
            <button type="submit" style={styles.button}>Continue</button>
          </form>
        </div>
      );
    };

    export default LandingPage;
