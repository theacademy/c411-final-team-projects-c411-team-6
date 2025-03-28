import React from "react";
import { useNavigate } from "react-router-dom"; // Updated import for v6

const LogoutComponent = () => {
  const navigate = useNavigate(); // Use useNavigate instead of useHistory

  const handleLogout = () => {
    // Remove userID from localStorage
    localStorage.removeItem("userID");
    localStorage.removeItem("user"); // Optional: Remove other user data if stored

    // Redirect to login page after logout
    navigate("/"); // Use navigate() instead of history.push()
  };

  return (
    <div className="flex justify-center items-center h-screen">
      <button
        onClick={handleLogout}
        className="bg-red-500 text-white px-6 py-2 rounded"
      >
        Logout
      </button>
    </div>
  );
};

export default LogoutComponent;
