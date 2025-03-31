DROP DATABASE IF exists FlowTrack;
CREATE DATABASE FlowTrack;
USE FlowTrack;

DELIMITER $$

-- USERS TABLE
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TRIGGER users_updated_at_timestamp
BEFORE UPDATE ON users
FOR EACH ROW
BEGIN
  SET NEW.updated_at = CURRENT_TIMESTAMP;
END; $$

-- ITEMS TABLE, LINKS USER ACCOUNTS WITH PLAID
CREATE TABLE items(
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    plaid_access_token VARCHAR(512) UNIQUE NOT NULL,
    plaid_item_id VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ACCOUNTS TABLE, STORES USER FINANCIAL ACCOUNTS LINKED VIA PLAID
CREATE TABLE accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    item_id INT,
    plaid_account_id varchar(255) UNIQUE NOT NULL,
    name varchar(255) NOT NULL,
    mask varchar(10) NOT NULL,
    official_name varchar(255),
    current_balance DECIMAL(28,10),
    available_balance DECIMAL(28,10),
    iso_currency_code varchar(10),
    unofficial_currency_code varchar(10),
    type varchar(50) NOT NULL,
    subtype varchar(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
);

CREATE TRIGGER accounts_updated_at_timestamp
BEFORE UPDATE ON accounts
FOR EACH ROW
BEGIN
  SET NEW.updated_at = CURRENT_TIMESTAMP;
END; $$

-- TRANSACTIONS TABLE, STORES TRANSACTION HISTORY, ALLOWING FILTERING BY CATEGORY, AMOUNT, ETC.
CREATE TABLE transactions (
    plaid_transaction_id VARCHAR(255) PRIMARY KEY, 
    account_id INT,
    category VARCHAR(100),                              
    type VARCHAR(50) NOT NULL,                          
    name VARCHAR(255) NOT NULL,                         
    amount DECIMAL(28,10) NOT NULL,
    date DATE NOT NULL,
    account_owner VARCHAR(255),                         
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
);

CREATE TRIGGER transactions_updated_at_timestamp
BEFORE UPDATE ON transactions
FOR EACH ROW
BEGIN
  SET NEW.updated_at = CURRENT_TIMESTAMP;
END; $$

-- CASH FLOW STATEMENTS TABLE
CREATE TABLE statements (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  month INT NOT NULL CHECK (month BETWEEN 1 AND 12),
  year INT NOT NULL,
  total_income DECIMAL(28,10) NOT NULL,
  total_expenses DECIMAL(28,10) NOT NULL,
  net_cash_flow DECIMAL(28,10) GENERATED ALWAYS AS (total_income - total_expenses) STORED,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TRIGGER before_statements_update
BEFORE UPDATE ON statements
FOR EACH ROW
BEGIN
  SET NEW.updated_at = CURRENT_TIMESTAMP;
END$$

DELIMITER ;