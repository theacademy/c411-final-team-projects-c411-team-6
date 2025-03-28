import React, { useEffect, useState, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import { jsPDF } from "jspdf";
import autoTable from "jspdf-autotable";
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
        if (!selectedMonth || !selectedYear) {
            setMessage("Please select both a month and a year.");
            return;
        }
        try {
            const res = await fetch(`http://localhost:8080/transactions?userId=${userId}`);
            const data = await res.json();
            const filtered = applyDateFilter(data, selectedMonth, selectedYear);
            setTransactions(data);
            setFilteredTransactions(filtered);
            if (filtered.length === 0) {
                setMessage("No information available for the selected month.");
            } else {
                setMessage("");
            }
        } catch (err) {
            console.error("Failed to fetch transactions:", err);
        }
    }, [userId, selectedMonth, selectedYear, setTransactions]);

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


    const downloadStatement = () => {
        const doc = new jsPDF();

        // Set Title
        doc.setFontSize(18);
        doc.setTextColor(40, 40, 40); // Dark text color
        doc.text("Financial Statement", 20, 20);

        // Month/Year
        doc.setFontSize(12);
        doc.setTextColor(100, 100, 100); // Lighter text color
        doc.text(`Month: ${months[selectedMonth - 1]}, Year: ${selectedYear}`, 20, 30);

        // Table Data: Revenue, Expenses, Gains, Losses, Net Income
        const tableData = [
            // Row 1: Headers
            ["Category", "Details", "Amount"],
            // Row 2: Revenue and its details
            ["Revenue", "Product Sales", `$${totalRevenue.toFixed(2)}`],
            ["", "Service Revenue", "$2000"], // Example breakdown
            ["", "Total Revenue", `$${totalRevenue.toFixed(2)}`], // Summed up
            // Row 3: Expenses and its details
            ["Expenses", "Rent", "$1000"],
            ["", "Salaries", "$2500"],
            ["", "Marketing", "$1000"],
            ["", "Total Expenses", "$4500"], // Summed up
            // Row 4: Gains
            ["Gains", "Gain from Asset Sale", ""],
            // Row 5: Losses
            ["Losses", "Loss from Investment", ""],
            // Row 6: Net Income
            ["Net Income", "Net Income", `$${netCashFlow.toFixed(2)}`]
        ];

        // Add Table
        autoTable(doc,{
            startY: 40,
            head: [tableData[0]],
            body: tableData.slice(1),
            theme: 'striped',
            styles: {
                fontSize: 10,
                cellPadding: 5,
                halign: 'center',
                valign: 'middle',
                textColor: [0, 0, 0],
                fontStyle: 'normal',
                overflow: 'linebreak',
                lineHeight: 1.5,
                maxWidth: 100,
            },
            headStyles: {
                fillColor: [174, 198, 207],
                textColor: [255, 255, 255],
                fontStyle: 'bold',
                halign: 'center',
                valign: 'middle',
                fontSize: 10,
            },
            columnStyles: {
                0: { cellWidth: 50 },
                1: { cellWidth: 'auto' },
                2: { cellWidth: 40 }
            },
            margin: { top: 10 },
            overflow: 'linebreak',
        });

        // Save PDF
        doc.save(`IncomeStatement_${months[selectedMonth - 1]}_${selectedYear}.pdf`);
    };



    return (
        <div className="bg-muted w-full min-h-screen">
            <Navi></Navi>
            <div className="max-w-6xl mx-auto px-6 py-8">
                <h2 className="text-3xl font-bold mb-6 text-light-black">Financial Statements</h2>

                {/* Month/Year Filter */}
                <div className="flex gap-4 mb-6 items-center">
                    <div>
                        <select
                            value={selectedMonth}
                            onChange={(e) => setSelectedMonth(parseInt(e.target.value))}
                            className="border px-2 py-1 rounded"
                        >
                            {months.map((month, index) => (
                                <option key={index} value={index + 1}>{month}</option>
                            ))}
                        </select>
                    </div>
                    <div>
                        <input
                            type="number"
                            value={selectedYear}
                            onChange={(e) => setSelectedYear(parseInt(e.target.value))}
                            min="2000"
                            max="2099"
                            className="border px-2 py-1 rounded"
                        />
                    </div>
                    <button
                        onClick={fetchTransactions}
                        className="bg-blue-600 text-white px-4 py-2 rounded"
                    >
                        Generate Statement
                    </button>
                </div>

                {message && <p className="text-sm text-blue-600 mb-4">{message}</p>}

                {/* Statement Summary */}
                {filteredTransactions.length > 0 && (
                    <StatementSummary
                        revenueBreakdown={revenueBreakdown}
                        expenseBreakdown={expenseBreakdown}
                        totalRevenue={totalRevenue}
                        totalExpenses={totalExpenses}
                        netCashFlow={netCashFlow}
                    />
                )}

                {/* Download Button */}
                <div className="mt-8">
                    {filteredTransactions.length > 0 && (
                        <button
                            onClick={downloadStatement}
                            className="bg-green-600 text-white px-4 py-2 rounded mt-8"
                        >
                            Download Statement as PDF
                        </button>
                    )}
                </div>
            </div>
        </div>
    );
};

export default StatementComponent;
