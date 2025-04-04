# Financial Management App

## Overview
The Financial Management App is a Spring Boot-based RESTful application designed to help small businesses track, manage, and project financial goals. It provides an intuitive interface to monitor transactions, generate financial statements, and forecast business spending.

## Features
- **Track Transactions**: 
  - View all business transaction history.
  - Filter and categorize transactions by month or year.
  - Classify transactions into revenues and expenses.
- **Generate Financial Statements**:
  - Generate cash flow statements on demand.
  - Retrieve previously generated statements.
- **Forecast Spending**:
  - View financial forecasts based on historical data.
  - Adjust financial projections dynamically.
- **User Authentication and Bank Integration**:
  - Secure user registration and login.
  - Connect bank accounts using Plaid API.

## Tech Stack
- **Backend**: Spring Boot, JDBC Template, MySQL
- **Frontend**: React (in development)
- **Third-Party API**: Plaid (for financial data access)

## Installation
### Prerequisites
- Java 17+
- MySQL
- Node.js (for frontend development)
- Postman (for API testing)

### Backend Setup
1. Clone the repository:
   ```sh
   git clone https://github.com/your-repo/financial-app.git
   cd financial-app
   ```

2. Set up environment variables in  `.env`:
   ```properties
    # Plaid API credentials
    PLAID_CLIENT_ID=your_plaid_client_id
    PLAID_SECRET=your_plaid_secret
    PLAID_ENVIRONMENT=sandbox|development|production
    PLAID_PRODUCTS=auth,transactions  # comma-separated list
    PLAID_COUNTRY_CODES=US,CA         # comma-separated list
    PLAID_REDIRECT_URI=your_redirect_uri_if_applicable
    
     # Database configuration
    DB_URL=jdbc:mysql://localhost:3306/your_db
    DB_USER=your_db_username
    DB_PASS=your_db_password
   ```
3. Configure the database in `application.properties`:
   ```properties
    # Plaid configuration
    plaid.client.id=${PLAID_CLIENT_ID}
    plaid.secret=${PLAID_SECRET}
    plaid.environment=${PLAID_ENVIRONMENT:sandbox}
    plaid.products=${PLAID_PRODUCTS}
    plaid.country.code=${PLAID_COUNTRY_CODES}
    plaid.redirect.uri=${PLAID_REDIRECT_URI}
    
    # Database configuration
    spring.datasource.url=${DB_URL}
    spring.datasource.username=${DB_USER}
    spring.datasource.password=${DB_PASS}
   ```
4. Build and run the application:
   ```sh
   mvn spring-boot:run
   ```

### Frontend Setup
1. Navigate to the frontend directory:
   ```sh
   cd client
   ```
2. Install dependencies:
   ```sh
   npm install
   ```
3. Start the frontend:
   ```sh
   npm start
   ```

## API Endpoints
| Endpoint | Method | Description |
|----------|--------|-------------|
| `/users/register` | POST | Registers a new user |
| `/users/login` | POST | Logs in a user |
| `/transactions/{id}` | GET | Fetches all transactions for a user|
| `/statements` | POST | Generates a cash flow statement |
| `/transactions/forecast` | GET | Retrieves financial forecast data |

## Usage
1. **Register/Login**: Users create an account and log in.
2. **Connect Bank Account**: Using Plaid, users securely connect their bank account.
3. **View Transactions**: Users can see and filter past transactions.
4. **Generate Statements**: Users can generate and retrieve financial reports.
5. **Forecast Spending**: Users analyze past trends and adjust projections.

## Future Enhancements
- Implement advanced data visualization for forecasts.
- Improve AI-powered financial recommendations.
- Add role-based access control for multi-user businesses.

## Contributors
- Vivian Chen: Environment Setup, Transactions Logic Backend, API connection Logic Backend, User Login Frontend, Statements Frontend, Frontend UI navigation creation, Frontend Development Lead.
- Ly Ha: Transactions Logic Frontend, API connection Logic Frontend, User Login Logic Backend, Forecast Frontend, Base Service Testing, SQL backend schema creation and management, Backend Development Lead.
- Thuc Luong: DAO and DTO creation, Service Layer Middleware, Statements Logic Middleware, Service Layer Advanded Testing, Presentation Creation, Middleware Development Lead.
- Tavonga Tafuma: Controller creation, Forecast Logic Middleware, DAO Testing, Presentation Creation, Middleware Development Lead.

## License
This project is licensed under the MIT License.
