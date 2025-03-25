import React, { useEffect, useState } from 'react';
import { PlaidLink } from 'react-plaid-link';

const PlaidLinkComponent = () => {
    const [linkToken, setLinkToken] = useState(null);

    // Fetch the link token from backend
    const fetchLinkToken = async () => {
        try {
            const response = await fetch('/api/plaid/create-link-token?userId=12345');
            const data = await response.json();
            setLinkToken(data.link_token);  // Store the link_token in state
        } catch (error) {
            console.error('Error fetching link token:', error);
        }
    };

    // Fetch the link token when the component mounts
    useEffect(() => {
        fetchLinkToken();
    }, []);

    // Callback function that receives the public_token
    const onSuccess = async (public_token, metadata) => {
        console.log('Public Token:', public_token);
        // Send the public_token to the backend for exchange
        try {
            const response = await fetch('/api/plaid/exchange-public-token', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ public_token }),
            });
            const data = await response.json();
            console.log('Access Token:', data.access_token);
            // Store access_token securely in backend or state
        } catch (error) {
            console.error('Error exchanging public token:', error);
        }
    };

    if (!linkToken) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <h2>Link Your Bank Account</h2>
            <PlaidLink
                token={linkToken}
                onSuccess={onSuccess}
                onExit={(err, metadata) => {
                    if (err) {
                        console.error('Error during Plaid Link:', err);
                    }
                }}
            >
                Connect Bank Account
            </PlaidLink>
        </div>
    );
};

export default PlaidLinkComponent;
