import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const RegisterComponent = ({ setUserID }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();

  const handleRegister = () => {
    fetch("http://localhost:8080/users/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ username, password }),
    })
      .then((response) => response.json())
      .then((createdUser) => {
        if (createdUser && createdUser.id) {
          setUserID(createdUser.id); // Extract the userID from the createdUser object
          console.log("User ID created:", createdUser.id); // Log the userID to verify
          navigate("/link-account");
        } else {
          console.error("User ID not found in response");
        }
      })
      .catch((error) => {
        console.error("Error during registration:", error);
      });
  };


  return (
    <div>
      <h2>Register</h2>
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
      <button onClick={handleRegister}>Register</button>
      {errorMessage && <p>{errorMessage}</p>}
    </div>
  );
};

export default RegisterComponent;
