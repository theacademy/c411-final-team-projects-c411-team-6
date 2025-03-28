import React, { useEffect, useState, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import Navi from "./ui/Navi";

const StatementComponent = () => {
    const [transactions, setTransactions] = useState([]);
    const [filteredTransactions, setFilteredTransactions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [startDate, setStartDate] = useState("");
    const [endDate, setEndDate] = useState("");
    const [selectedCategory, setSelectedCategory] = useState("");
    const [categories, setCategories] = useState([]);

    const navigate = useNavigate();
    const [user, setUser] = useState(null);

    useEffect(() => {
        const storedUser = JSON.parse(localStorage.getItem("user"));
        if (storedUser && storedUser.plaidAccessToken) {
            setUser(storedUser);
        } else {
            navigate("/link-account");
        }
    }, [navigate]);

    const newSta = useCallback(async () => {
        if (!user || !user.id) return;
        setLoading(true);
        try {
            const res = await fetch(`http://localhost:8080/transactions?userId=${user.id}`);
            const data = await res.json();
            setTransactions(data);
            setFilteredTransactions(data);
            extractCategories(data);
        } catch (error) {
            console.error("Error fetching transactions:", error);
        } finally {
            setLoading(false);
        }
    }, [user]);

    useEffect(() => {
        if (user) {
            fetchTransactions();
        }
    }, [user, fetchTransactions]);

    const extractCategories = (transactions) => {
        const uniqueCategories = new Set();
        transactions.forEach((txn) => {
            if (txn.category) {
                uniqueCategories.add(txn.category);
            }
        });
        setCategories([...uniqueCategories]);
    };

    const filterTransactions = async () => {
        if (!startDate || !endDate) return; // Date filters are required
        setLoading(true);
        try {
            const categoryParam = selectedCategory ? `&category=${selectedCategory}` : '';
            const res = await fetch(
                `http://localhost:8080/transactions/by-date?userId=${user.id}&startDate=${startDate}&endDate=${endDate}${categoryParam}`
            );
            const data = await res.json();
            setFilteredTransactions(data);
        } catch (error) {
            console.error("Error fetching transactions by date:", error);
        } finally {
            setLoading(false);
        }
    };

    const filterByCategory = (category) => {
        setSelectedCategory(category);
        if (!category) {
            setFilteredTransactions(transactions);
        } else {
            const filtered = transactions.filter((txn) => txn.category === category);
            setFilteredTransactions(filtered);
        }
    };

    return (
        <div className="container mx-auto p-6">
            <Navi></Navi>
            <h2 className="text-2xl font-bold mb-4">Transactions</h2>

            {/* Filters */}
            <div className="flex gap-4 mb-4">
                {/* Date Filter */}
                <div>
                    <label className="block font-medium">Start Date:</label>
                    <input
                        type="date"
                        value={startDate}
                        onChange={(e) => setStartDate(e.target.value)}
                        className="border px-2 py-1"
                    />
                </div>
                <div>
                    <label className="block font-medium">End Date:</label>
                    <input
                        type="date"
                        value={endDate}
                        onChange={(e) => setEndDate(e.target.value)}
                        className="border px-2 py-1"
                    />
                </div>
                <button
                    onClick={filterTransactions}
                    className="bg-blue-500 text-white px-4 py-2 rounded"
                >
                    Filter by Date
                </button>

                {/* Category Filter */}
                <div>
                    <label className="block font-medium">Category:</label>
                    <select
                        value={selectedCategory}
                        onChange={(e) => filterByCategory(e.target.value)}
                        className="border px-2 py-1"
                    >
                        <option value="">All</option>
                        {categories.map((category, index) => (
                            <option key={index} value={category}>
                                {category}
                            </option>
                        ))}
                    </select>
                </div>
            </div>

            {/* Transactions Table */}
            {loading ? (
                <p>Loading transactions...</p>
            ) : filteredTransactions.length === 0 ? (
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
                        {filteredTransactions.map((txn, index) => (
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

export default StatementComponent;
