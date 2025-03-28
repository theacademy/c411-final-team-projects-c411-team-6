import React, {useEffect, useState} from "react";
import { useNavigate } from "react-router-dom";

const Login = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();
  console.log("Before User Id: " + localStorage.getItem("userID"));
  useEffect(() => {
    localStorage.removeItem("userID");
  }, []);



  const handleLogin = async (e) => {
    e.preventDefault();
    const userData = { username, password };

    try {
      // Send POST request to login the user
      const response = await fetch("http://localhost:8080/users/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(userData),
      });

      if (response.ok) {
        // If login is successful, redirect to Plaid link page
        // In Login Component after successful login:
        const data = await response.json();
        localStorage.setItem("userId", data.id);

        console.log("After User Id: " + localStorage.getItem("userId"));
        navigate("/transactions");
      } else {
        const error = await response.text();
        alert(error);
      }
    } catch (error) {
      console.error("Error occurred during login", error);
      alert("An error occurred during login");
    }
  };

  return (
    <div>
      <h2>Login</h2>
      <form onSubmit={handleLogin}>
        <div>
          <label>Username:</label>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </div>
        <div>
          <label>Password:</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
        <button type="submit">Login</button>
      </form>
    </div>
  );
};

export default Login;