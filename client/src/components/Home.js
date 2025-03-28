import React from "react";
import { Button } from "./ui/button";
import { Card } from "./ui/card";
import { useNavigate } from "react-router-dom";
import { LifeBuoyIcon, ShoppingCartIcon, TagIcon } from "lucide-react";
import LandingHeader from "./ui/LandingHeader";

// Feature cards data
const featureCards = [
    {
        icon: <ShoppingCartIcon className="w-10 h-10" />,
        title: "Transaction Tracking",
        description:
            "Transaction tracking involves recording and monitoring every financial exchange within a business, such as sales, purchases, and payments. For small businesses, it ensures accurate financial reporting, tax compliance, and effective cash flow management, helping owners make informed decisions, prevent fraud, and plan for growth. This practice is crucial for maintaining financial organization, identifying issues early, and ensuring the business remains on track.",
    },
    {
        icon: <TagIcon className="w-10 h-10" />,
        title: "Financial Statement Generator",
        description:
            "Financial statement generation involves creating key reports like income statements, balance sheets, and cash flow statements that summarize a business's financial performance and position. For small businesses, these statements are vital for tracking profitability, managing expenses, and ensuring tax compliance. They provide essential insights for making strategic decisions, securing funding, and planning for growth, helping businesses stay financially sustainable.",
    },
    {
        icon: <LifeBuoyIcon className="w-10 h-10" />,
        title: "Business Financial Forecasting",
        description:
            "Business finance forecast involves predicting a company’s future financial performance based on historical data, trends, and assumptions about future conditions. For small businesses, this forecast is crucial for budgeting, managing cash flow, and planning for potential challenges or opportunities. It helps business owners make informed decisions, secure investments, and ensure long-term financial stability by anticipating revenue, expenses, and profitability.",
    },
];

const Home = () => {
    const navigate = useNavigate();

    const handleLoginClick = () => {
        navigate("/login");
    };

    const handleRegisterClick = () => {
        navigate("/register");
    };

    return (
        <div className="bg-muted flex flex-col justify-center w-full">
            {/* Header Section */}
            <LandingHeader onLoginClick={handleLoginClick} bgColor = "bg-muted" />

            {/* Main Content Section */}
            <div className="w-full relative mt-[50px]">
                <section className="w-full h-[600px] relative">
                    <div className="absolute w-full h-full bg-muted opacity-[0.06]" />

                    <div className="relative h-full flex items-center">
                        <div className="w-full max-w-[1110px] bg-muted mx-auto flex flex-col lg:flex-row items-center justify-between px-4 lg:px-8 gap-8">

                            {/* Left Side Text */}
                            <div className="flex-1 flex flex-col justify-center">
                                <h1 className="font-h3-bold-32-40-0-1px text-light-black mb-6 text-xl sm:text-2xl md:text-3xl lg:text-4xl">
                                    Take Control of Your Business Finances
                                </h1>

                                <p className="font-body-1-regular-16-22-0-3px text-muted-foreground mb-8">
                                    FlowTrack is an intuitive financial management tool designed
                                    for small businesses to effortlessly track transactions,
                                    generate insightful financial statements, and forecast future
                                    business performance. With comprehensive features that offer a
                                    clear overview of your business health, FlowTrack ensures
                                    you're always equipped to make informed financial decisions.
                                </p>

                                {/* Register Button */}
                                <Button
                                    className="w-[158px] h-[42px] rounded-[25px] text-white"
                                    onClick={handleRegisterClick}
                                >
                            <span className="font-button-bold-14-18-0-3px">
                              Create Account
                            </span>
                                                </Button>
                            </div>

                            {/* Right Side Image */}
                            <div className="flex-1 flex justify-center lg:justify-end">
                                <img
                                    className="w-full max-w-[600px] h-auto"
                                    alt="Desktop"
                                    src="https://c.animaapp.com/NlTnxe1m/img/desktop.svg"
                                />
                            </div>
                        </div>
                    </div>
                </section>
            </div>

            {/* Features Section */}
            <div>
                <section className="w-full min-h-screen mt-[120px] bg-white">
                <div className="text-center mb-16 px-4 sm:px-8">
                        <h2 className="font-h3-bold-32-40-0-1px text-light-black py-8 sm:py-10 md:py-12 lg:py-16 text-xl sm:text-2xl md:text-3xl lg:text-4xl">
                            We offer a complete
                            <br />
                            range of features
                        </h2>
                    </div>



                    <div className="w-full max-w-[1110px] mx-auto flex justify-between gap-8 px-4">
                        {featureCards.map((card, index) => (
                            <div key={index} className="w-[350px] h-[400px] border-none shadow-none p-4 bg-white rounded-lg flex flex-col items-center justify-center">
                                {/* Custom card content */}
                                <div className="flex justify-center mb-6">
                                    {card.icon}
                                </div>
                                <h3 className="font-h6-bold-18-24-0-1px text-light-black text-center mb-3">
                                    {card.title}
                                </h3>
                                <p className="font-body-2-regular-14-20-0-2px text-light-black text-center">
                                    {card.description}
                                </p>
                            </div>
                        ))}
                    </div>

                </section>
            </div>

            {/* Get Started */}
            <div>
                <section className="w-full mt-[120px] ">
                    <div className="text-center mb-16 px-4 sm:px-8">
                        <h2 className="font-h3-bold-32-40-0-1px text-light-black text-xl sm:text-2xl md:text-3xl lg:text-4xl">
                            Ready to get started?
                        </h2>
                        <p className="font-body-2-regular-14-20-0-2px text-light-black text-center sm:py-2 md:py-4 lg:py-6 "> </p>
                        {/* Register Button */}
                        <Button
                            className="w-[158px] h-[42px] rounded-[25px]  #F3F6FA text-accent-foreground"
                            onClick={handleRegisterClick}
                        >
                                    <span className="font-button-bold-14-18-0-3px text-white">
                                      Get Started
                                    </span>
                        </Button>
                    </div>

                </section>
            </div>
        </div>
    );
};

export default Home;
