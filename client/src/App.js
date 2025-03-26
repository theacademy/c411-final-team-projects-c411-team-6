import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from './Home';
import Login from './components/LoginComponent';
import Register from './components/RegisterComponent';
import PlaidLink from './LinkAccount';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/link-account" element={<PlaidLink />} />
      </Routes>
    </Router>
  );
};

export default App;
