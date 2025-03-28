import { BrowserRouter as Router, Routes, Route} from "react-router-dom";
import Home from "./components/Home";
import Login from "./components/LoginComponent";
import Register from "./components/RegisterComponent";
import TransactionsComponent from "./components/TransactionsComponent";
import StatementComponent from "./components/StatementComponent";
import SpendingForecast from "./components/SpendingForecast";

const App = () => {

  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login  />} />
        <Route path="/register" element={<Register />} />
        <Route  path="/transactions" element={<TransactionsComponent  />}/>
        <Route path='/statements' element={<StatementComponent />} />
        <Route path="/forecast" element={<SpendingForecast />} />
      </Routes>
    </Router>
  );
};

export default App;
