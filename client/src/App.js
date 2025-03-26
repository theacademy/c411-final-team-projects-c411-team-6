import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from './Home';
import Login from './components/LoginComponent';
import Register from './components/RegisterComponent';
import PlaidLink from './components/PlaidLinkComponent'; // Ensure you import PlaidLinkComponent here

const App = () => {
  const [userID, setUserID] = useState(null); // Store userID here

  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login setUserID={setUserID} />} />
        <Route path="/register" element={<Register setUserID={setUserID} />} />
        <Route path="/link-account" element={<PlaidLink userID={userID} />} />
      </Routes>
    </Router>
  );

};

export default App;
