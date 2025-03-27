import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Home from "./components/Home";
import Login from "./components/LoginComponent";
import Register from "./components/RegisterComponent";
import PlaidLinkComponent from "./components/PlaidLinkComponent";
import TransactionsComponent from "./components/TransactionsComponent";
import Nav from "./components/Nav";

const App = () => {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const storedUser = JSON.parse(localStorage.getItem("user"));
    if (storedUser) {
      setUser(storedUser);
    }
  }, []);

  const customNavItems = ["Home", "Services", "About Us", "Contact"];
  const customLogoText = "My Custom Logo";
  const customLoginText = "Sign In";

  return (
      <Router>
        {/* Render Nav component here so it's always visible on top of pages */}
        <Nav
            navItems={customNavItems}
            logoText={customLogoText}
            loginText={customLoginText}
        />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login setUser={setUser} />} />
          <Route path="/register" element={<Register />} />
          <Route path="/link-account" element={<PlaidLinkComponent setUser={setUser} />} />
          <Route
              path="/transactions"
              element={user?.plaidAccessToken ? <TransactionsComponent /> : <Navigate to="/link-account" />}
          />
        </Routes>
      </Router>
  );
};

export default App;
