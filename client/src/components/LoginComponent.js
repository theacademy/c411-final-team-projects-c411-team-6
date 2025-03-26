import React, { useState } from 'react';
import axios from 'axios'; // You need to install axios for making API requests

const LoginComponent = () => {
  // Define state for username, password, and error message
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // Send a POST request to the backend login API
      const response = await axios.post('http://localhost:8080/users/login', { username, password });

      // On success, redirect or show a success message
      if (response.status === 200) {
        alert('Login successful!');
        // You can redirect the user to another page here, for example:
        // window.location.href = '/dashboard';
      }
    } catch (err) {
      // If error, show error message
      if (err.response) {
        setError(err.response.data);
      } else {
        setError('Something went wrong!');
      }
    }
  };

  return (
    <div className="login-container">
      <h2>Login</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="username">Username</label>
          <input
            type="text"
            id="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>
        <div>
          <label htmlFor="password">Password</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        {error && <p className="error">{error}</p>}
        <button type="submit">Login</button>
      </form>
    </div>
  );
};

export default LoginComponent;
