import React from "react";
import { Link } from "react-router-dom";

const Navi = () => {
    return (
        <header className="w-full h-[88px] bg-accent-default">
            <div className="relative h-full flex items-center justify-between px-8">
                {/* Logo Section */}
                <Link to="/statements" className="w-[70px] h-8 flex items-center">
                    <div className="[font-family:'Lato',Helvetica] font-bold text-2xl tracking-[0.20px] leading-8">
                        FlowTrack
                    </div>
                </Link>

                {/* Navigation Links Section */}
                <nav className="flex-1 flex justify-center">
                    <div className="w-[840px] h-[62px] border border-solid border-[#d9d9d9] rounded-[25px] bg-accent-default flex items-center justify-center">
                        <ul className="flex space-x-16">
                            <li>
                                <Link
                                    to="/transactions"
                                    className="font-button-bold-14-18-0-3px text-light-white py-2 px-4 rounded-md hover:bg-accent-light"
                                >
                                    Transactions
                                </Link>
                            </li>
                            <li>
                                <Link
                                    to="/statements"
                                    className="font-button-bold-14-18-0-3px text-light-white py-2 px-4 rounded-md hover:bg-accent-light"
                                >
                                    Statements
                                </Link>
                            </li>
                            <li>
                                <Link
                                    to="/forecast"
                                    className="font-button-bold-14-18-0-3px text-light-white py-2 px-4 rounded-md hover:bg-accent-light"
                                >
                                    Forecast
                                </Link>
                            </li>
                        </ul>
                    </div>
                </nav>
            </div>
        </header>
    );
};

export default Navi;
