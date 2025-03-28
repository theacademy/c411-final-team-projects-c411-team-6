import React, { useState, useEffect } from "react";
import { usePlaidLink } from "react-plaid-link";
import { useNavigate } from "react-router-dom";
import { Button } from "./ui/button";

const PlaidLinkComponent = ({ onSuccessCallback }) => {
  const [linkToken, setLinkToken] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const storedUserID = localStorage.getItem("userID");

    if (storedUserID) {
      fetch(`http://localhost:8080/plaid/create-link-token?userId=${storedUserID}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ userId: storedUserID }),
      })
          .then((res) => res.text())
          .then((token) => {
            console.log("Received link token:", token);
            setLinkToken(token);
          })
          .catch((err) => console.error("Failed to fetch link token:", err));
    }
  }, []);

  const { open, ready } = usePlaidLink({
    token: linkToken,
    onSuccess: async (publicToken, metadata) => {
      const storedUserID = localStorage.getItem("userID");

      try {
        const response = await fetch("http://localhost:8080/plaid/exchange-public-token", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ public_token: publicToken, user_id: storedUserID }),
        });

        if (!response.ok) throw new Error("Failed to exchange public token");

        if (onSuccessCallback) {
          onSuccessCallback();
        }

        navigate("/transactions");
      } catch (error) {
        console.error("Error:", error);
      }
    },
    onExit: (error, metadata) => {
      console.log("User exited:", metadata);
      if (error) console.error("Exit Error:", error);
    },
  });

 return (
     <Button onClick={() => open()} disabled={!ready} variant="primary" size="default" className="bg-blue-500 text-white">
       Connect a Bank Account
     </Button>
   );
};

export default PlaidLinkComponent;
