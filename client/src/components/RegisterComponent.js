import React, { useState, useEffect } from "react";
import { usePlaidLink } from "react-plaid-link";

const PlaidLinkComponent = () => {
  const [linkToken, setLinkToken] = useState(null);

  // Fetch link token from backend using dynamic userID
  useEffect(() => {
    const storedUserID = localStorage.getItem("userID");  // Retrieve from localStorage

    if (storedUserID) {
      console.log("User ID from localStorage:", storedUserID); // Log userID

      fetch(`http://localhost:8080/plaid/create-link-token?userId=${storedUserID}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ userId: storedUserID }),
      })
        .then((res) => res.text())
        .then((token) => {
          console.log("Received link token:", token);  // Log the token received
          setLinkToken(token);
        })
        .catch((err) => console.error("Failed to fetch:", err));
    } else {
      console.log("No userID found in localStorage.");
    }
  }, []);

  const { open, ready } = usePlaidLink({
    token: linkToken,
    onSuccess: (publicToken, metadata) => {
      console.log("Public Token:", publicToken);  // Log publicToken
      console.log("Metadata:", metadata);  // Log metadata

      // Send the publicToken to your backend to exchange for an access token
      fetch("http://localhost:8080/plaid/exchange-public-token", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ public_token: publicToken }),
      })
        .then((response) => response.json())
        .then((data) => {
          console.log("Access Token Response:", data);  // Log access token response
        })
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
