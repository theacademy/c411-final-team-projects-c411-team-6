import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./Home";
import Login from "./components/LoginComponent";
import Register from "./components/RegisterComponent";
import PlaidLinkComponent from "./components/PlaidLinkComponent";
import TransactionsComponent from "./components/TransactionsComponent";

const App = () => {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const storedUser = JSON.parse(localStorage.getItem("user"));
    if (storedUser) {
      setUser(storedUser);
    }
  }, []);

  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login setUser={setUser} />} />
        <Route path="/register" element={<Register />} />
        <Route
                  path="/transactions"
                  element={
                    user?.plaidAccessToken ? (
                      <TransactionsComponent setUser={setUser} />
                    ) : (
                      <PlaidLinkComponent setUser={setUser} />
                    )
                  }
                />
      </Routes>
    </Router>
  );
};

export default App;
