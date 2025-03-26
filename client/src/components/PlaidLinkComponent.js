import React, { useState, useEffect } from "react";
import { usePlaidLink } from "react-plaid-link";

const PlaidLinkComponent = ({ userID }) => {
  const [linkToken, setLinkToken] = useState(null);


  // Fetch link token from backend
  useEffect(() => {
      console.log("Received userID in PlaidLinkComponent:", userID); // Log the userID

      if (!userID) return; // If no userID, exit early

      // Fetch link token from backend
      fetch(`http://localhost:8080/plaid/create-link-token?userId=${userID}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ userId: userID }), // Send userID in the body as well
      })
        .then((res) => res.text())
        .then((token) => {
          console.log("Received link token:", token);
          setLinkToken(token); // <-- this is critical
        })
        .catch((err) => console.error("Failed to fetch:", err));
    }, [userID]); // Make sure to re-fetch if userID changes

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

  console.log("Link token:", linkToken); // Check if token is valid
  console.log("Ready to open Plaid Link:", ready);

  return (
    <>
      <h2>Connect to Your Account</h2>
      <p>Here, you'll be able to link your bank account through Plaid.</p>
      <button onClick={() => {
        console.log("Opening Plaid Link");
        open();
      }} disabled={!ready}>
        Connect Bank Account
      </button>
    </>
  );

};

export default PlaidLinkComponent;
