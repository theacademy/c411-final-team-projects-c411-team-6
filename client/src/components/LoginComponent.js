import React, { useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";

const LoginComponent = () => {
  const [userID, setUserID] = useState(null);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    if (userID !== null) {
      console.log("Updated userID:", userID); // This will print the updated userID
    }
  }, [userID]); // This effect will run every time userID changes

  const handleLogin = () => {
    // Send login request to the backend
    fetch("http://localhost:8080/users/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ username, password }),
    })
    .then((response) => response.json())
    .then((data) => {
      if (data) {
        // If login is successful, set the userID
        console.log("User ID:", data.id);
        setUserID(data.id); // Assuming the server returns the user object
        navigate("/link-account");
      } else {
        setErrorMessage('Invalid credentials');
      }
    })
    .catch((error) => {
      console.error("Login error:", error);
      setErrorMessage('An error occurred. Please try again later.');
    });
  };

  return (
    <div>
      <h2>Login</h2>
      <input
        type="text"
        placeholder="Username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      />
      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <button onClick={handleLogin}>Login</button>
      {errorMessage && <p>{errorMessage}</p>}
    </div>
  );
};

export default LoginComponent;
