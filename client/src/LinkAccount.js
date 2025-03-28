import React from 'react';
import PlaidLinkComponent from './components/PlaidLinkComponent';

const PlaidLink = () => {
  return (
    <div className="plaid-link-container">
      <h2>Connect to your Account</h2>
      <p>Here, you can link your bank account through Plaid.</p>
      <PlaidLinkComponent />
    </div>
  );
};

export default PlaidLink;
