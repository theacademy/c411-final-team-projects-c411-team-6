import React from "react";
import { Card } from "./card";

const StatementSummary = ({ revenueBreakdown, expenseBreakdown, totalRevenue, totalExpenses, netCashFlow }) => {
    return (
        <div className="bg-muted min-h-screen">
            {/* Title */}
            <div className="max-w-[1110px] mx-auto px-4 py-10 text-center">
                <h2 className="font-h3-bold-32-40-0-1px text-light-black text-2xl sm:text-3xl md:text-4xl">
                    Financial Statement Summary
                </h2>
                <p className="font-body-2-regular-14-20-0-2px text-muted-foreground mt-2">
                    Here's a detailed breakdown of your business finances for this period.
                </p>
            </div>

            {/* Financial Overview Cards */}
            <div className="max-w-[1110px] mx-auto grid grid-cols-1 md:grid-cols-3 gap-8 px-4">
                <Card className="p-6 bg-white text-center rounded-xl shadow-md">
                    <h3 className="font-h6-bold-18-24-0-1px text-light-black mb-2">Total Revenue</h3>
                    <p className="text-2xl font-bold text-green-600">${totalRevenue.toFixed(2)}</p>
                </Card>
                <Card className="p-6 bg-white text-center rounded-xl shadow-md">
                    <h3 className="font-h6-bold-18-24-0-1px text-light-black mb-2">Total Expenses</h3>
                    <p className="text-2xl font-bold text-red-500">${totalExpenses.toFixed(2)}</p>
                </Card>
                <Card className="p-6 bg-white text-center rounded-xl shadow-md">
                    <h3 className="font-h6-bold-18-24-0-1px text-light-black mb-2">Net Cash Flow</h3>
                    <p className={`text-2xl font-bold ${netCashFlow >= 0 ? "text-green-600" : "text-red-500"}`}>
                        ${netCashFlow.toFixed(2)}
                    </p>
                </Card>
            </div>

            {/* Breakdown Sections */}
            <div className="max-w-[1110px] mx-auto mt-16 px-4">
                {/* Revenue Breakdown */}
                <section className="mb-12">
                    <h3 className="text-xl font-semibold mb-4 text-light-black">Revenue Breakdown</h3>
                    <ul className="bg-white p-6 rounded-xl shadow-md space-y-2">
                        {Object.entries(revenueBreakdown).map(([category, value]) => (
                            <li key={category} className="flex justify-between font-body-2-regular-14-20-0-2px text-light-black">
                                <span>{category}</span>
                                <span>${value.toFixed(2)}</span>
                            </li>
                        ))}
                    </ul>
                </section>

                {/* Expense Breakdown */}
                <section className="mb-12">
                    <h3 className="text-xl font-semibold mb-4 text-light-black">Expense Breakdown</h3>
                    <ul className="bg-white p-6 rounded-xl shadow-md space-y-2">
                        {Object.entries(expenseBreakdown).map(([category, value]) => (
                            <li key={category} className="flex justify-between font-body-2-regular-14-20-0-2px text-light-black">
                                <span>{category}</span>
                                <span>${value.toFixed(2)}</span>
                            </li>
                        ))}
                    </ul>
                </section>
            </div>
        </div>
    );
};

export default StatementSummary;
