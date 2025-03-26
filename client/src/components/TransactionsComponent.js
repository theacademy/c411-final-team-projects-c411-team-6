import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const TransactionsComponent = () => {
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  // Retrieve user info from localStorage
  const user = JSON.parse(localStorage.getItem("user"));

  useEffect(() => {
    // Redirect if user is not authenticated or has not linked Plaid
    if (!user || !user.plaidAccessToken) {
      navigate("/link-account");
      return;
    }

    // Fetch transactions from backend
    fetch(`http://localhost:8080/transactions?userId=${user.id}`)
      .then((res) => res.json())
      .then((data) => {
        setTransactions(data);
        setLoading(false);
      })
      .catch((error) => {
        console.error("Error fetching transactions:", error);
        setLoading(false);
      });
  }, [navigate, user]);

  return (
    <div className="container mx-auto p-6">
      <h2 className="text-2xl font-bold mb-4">Transactions</h2>

      {loading ? (
        <p>Loading transactions...</p>
      ) : transactions.length === 0 ? (
        <p>No transactions found.</p>
      ) : (
        <div className="overflow-x-auto">
          <table className="min-w-full bg-white border border-gray-300">
            <thead>
              <tr className="bg-gray-200">
                <th className="py-2 px-4 border">Date</th>
                <th className="py-2 px-4 border">Name</th>
                <th className="py-2 px-4 border">Amount</th>
                <th className="py-2 px-4 border">Category</th>
                <th className="py-2 px-4 border">Status</th>
              </tr>
            </thead>
            <tbody>
              {transactions.map((txn, index) => (
                <tr key={index} className="text-center">
                  <td className="py-2 px-4 border">{txn.date}</td>
                  <td className="py-2 px-4 border">{txn.name}</td>
                  <td className="py-2 px-4 border">${txn.amount.toFixed(2)}</td>
                  <td className="py-2 px-4 border">{txn.category || "Unknown"}</td>
                  <td className={`py-2 px-4 border ${txn.pending ? "text-yellow-500" : "text-green-500"}`}>
                    {txn.pending ? "Pending" : "Completed"}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default TransactionsComponent;
