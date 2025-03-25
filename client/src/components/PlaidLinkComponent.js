import React, { useState, useEffect } from "react";
import { usePlaidLink } from "react-plaid-link";

const PlaidLinkComponent = () => {
  const [linkToken, setLinkToken] = useState(null);

  // Fetch link token from backend
  useEffect(() => {
    fetch("http://localhost:8080/plaid/create-link-token?userId=1", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ userId: 1 })
    })
        .then((res) => res.text())
        .then((token) => {
          console.log("Received link token:", token);
          setLinkToken(token); // <-- this is critical
        })
        .catch((err) => console.error("Failed to fetch:", err));
  }, []);


  const { open, ready } = usePlaidLink({
    token: linkToken,
    onSuccess: (publicToken, metadata) => {
      console.log("Public Token:", publicToken);
      console.log("Metadata:", metadata);

      // Send the publicToken to your backend to exchange for an access token
      fetch("http://localhost:8080/plaid/exchange-public-token", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ public_token: publicToken }),
      })
        .then((response) => response.json())
        .then((data) => console.log("Access Token Response:", data))
        .catch((error) => console.error("Error:", error));
    },
    onExit: (error, metadata) => {
      console.log("User exited:", metadata);
      if (error) {
        console.error("Error:", error);
      }
    },
  });

  return (
    <button onClick={() => open()} disabled={!ready}>
      Connect Bank Account
    </button>
  );
};

export default PlaidLinkComponent;
