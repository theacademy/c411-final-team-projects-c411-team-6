import React, { useEffect, useState, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import Navi from "./ui/Navi";
import StatementSummary from "./ui/StatementSummary";

const StatementComponent = () => {
    const [transactions, setTransactions] = useState([]);
    const [filteredTransactions, setFilteredTransactions] = useState([]);
    const [selectedMonth, setSelectedMonth] = useState(new Date().getMonth() + 1);
    const [selectedYear, setSelectedYear] = useState(new Date().getFullYear());
    const [userId, setUserId] = useState(null);
    const [historicalStatements, setHistoricalStatements] = useState([]);
    const [message, setMessage] = useState("");
    const navigate = useNavigate();

    const months = [
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    ];

    useEffect(() => {
        const id = localStorage.getItem("userID");
        if (id) {
            setUserId(id);
        } else {
            navigate("/login");
        }
    }, [navigate]);

    const applyDateFilter = (txns, month, year) => {
        return txns.filter(txn => {
            const txnDate = new Date(txn.date);
            return (
                txnDate.getMonth() + 1 === parseInt(month) &&
                txnDate.getFullYear() === parseInt(year)
            );
        });
    };

    const fetchTransactions = useCallback(async () => {
        if (!userId) return;
        try {
            const res = await fetch(`http://localhost:8080/transactions?userId=${userId}`);
            const data = await res.json();
            setTransactions(data);
            setFilteredTransactions(applyDateFilter(data, selectedMonth, selectedYear));
        } catch (err) {
            console.error("Failed to fetch transactions:", err);
        }
    }, [userId, selectedMonth, selectedYear]);

    useEffect(() => {
        if (userId) {
            fetchTransactions();
        }
    }, [userId, fetchTransactions]);

    const fetchHistoricalStatements = useCallback(async () => {
        try {
            const res = await fetch(`http://localhost:8080/statements?userId=${userId}`);
            const data = await res.json();
            setHistoricalStatements(data);
        } catch (err) {
            console.error("Failed to fetch historical statements:", err);
        }
    }, [userId]);

    useEffect(() => {
        if (userId) {
            fetchHistoricalStatements();
        }
    }, [userId, fetchHistoricalStatements]);

    const groupAndSumByCategory = (type) => {
        const isRevenue = type === "revenue";
        const categories = {};

        filteredTransactions.forEach(txn => {
            const isPositive = txn.amount > 0;
            const isMatch = isRevenue ? isPositive : !isPositive;

            if (isMatch) {
                const cat = txn.category || "Uncategorized";
                if (!categories[cat]) {
                    categories[cat] = 0;
                }
                categories[cat] += Math.abs(txn.amount);
            }
        });

        return categories;
    };

    const revenueBreakdown = groupAndSumByCategory("revenue");
    const expenseBreakdown = groupAndSumByCategory("expense");
    const totalRevenue = Object.values(revenueBreakdown).reduce((sum, val) => sum + val, 0);
    const totalExpenses = Object.values(expenseBreakdown).reduce((sum, val) => sum + val, 0);
    const netCashFlow = totalRevenue - totalExpenses;

    return (
        <div className="container mx-auto p-6">
            <Navi />
            <h2 className="text-2xl font-bold mb-4">Statements</h2>

            {/* Month/Year Filter */}
            <div className="flex gap-4 mb-6">
                <div>
                    <label>Month:</label>
                    <select
                        value={selectedMonth}
                        onChange={(e) => setSelectedMonth(parseInt(e.target.value))}
                    >
                        {months.map((month, index) => (
                            <option key={index} value={index + 1}>{month}</option>
                        ))}
                    </select>
                </div>
                <div>
                    <label>Year:</label>
                    <input
                        type="number"
                        value={selectedYear}
                        onChange={(e) => setSelectedYear(parseInt(e.target.value))}
                        min="2000"
                        max="2099"
                    />
                </div>
            </div>

            {message && <p className="text-sm text-blue-600 mb-4">{message}</p>}

            {/* Statement Summary */}
            <StatementSummary
                revenueBreakdown={revenueBreakdown}
                expenseBreakdown={expenseBreakdown}
                totalRevenue={totalRevenue}
                totalExpenses={totalExpenses}
                netCashFlow={netCashFlow}
            />

            {/* Historical Statements */}
            <div className="mt-12">
                <h3 className="text-xl font-semibold mb-4 text-light-black">Historical Statements</h3>
                <ul className="bg-white p-6 rounded-xl shadow-md space-y-4">
                    {historicalStatements.map((stmt) => (
                        <li key={stmt.id} className="flex justify-between font-body-2-regular-14-20-0-2px text-light-black">
                            <span>{`${months[stmt.month - 1]} ${stmt.year}`}</span>
                            <span>Net Cash Flow: ${stmt.netCashFlow.toFixed(2)}</span>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
};

export default StatementComponent;