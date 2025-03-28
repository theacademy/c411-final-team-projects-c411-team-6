import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Home from "./components/Home";
import Login from "./components/LoginComponent";
import Register from "./components/RegisterComponent";
import PlaidLinkComponent from "./components/PlaidLinkComponent";
import TransactionsComponent from "./components/TransactionsComponent";
import StatementComponent from "./components/StatementComponent";
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
          <Route path="/link-account" element={<PlaidLinkComponent setUser={setUser} />} />
          <Route
              path="/transactions"
              element={user?.plaidAccessToken ? <TransactionsComponent /> : <Navigate to="/link-account" />}
          />
          <Route path="/statements" element={<StatementComponent/>}/>
        </Routes>
      </Router>
  );
};

export default App;
