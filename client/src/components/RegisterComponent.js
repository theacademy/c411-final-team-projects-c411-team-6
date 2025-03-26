import React, { useState } from 'react';
import axios from 'axios';

const Register = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  const handleRegister = async (e) => {
    e.preventDefault();

    // Clear previous error/success messages
    setError('');
    setSuccessMessage('');

    try {
      // Send POST request to the server to register the user
      const response = await axios.post('http://localhost:8080/users/register', {
        username,
        password,
      });

      // Handle success response
      if (response.status === 200) {
        setSuccessMessage('Registration successful! You can now log in.');
      }
    } catch (err) {
      // Handle error response
      if (err.response && err.response.data) {
        setError(err.response.data.error || 'An error occurred during registration.');
      } else {
        setError('An unexpected error occurred.');
      }
    }
  };

  return (
    <div>
      <h2>Register</h2>
      {successMessage && <p style={{ color: 'green' }}>{successMessage}</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}

      <form onSubmit={handleRegister}>
        <div>
          <label>Username</label>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>

        <div>
          <label>Password</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>

        <button type="submit">Register</button>
      </form>
    </div>
  );
};

export default Register;