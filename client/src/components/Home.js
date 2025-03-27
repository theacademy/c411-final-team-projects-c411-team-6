import React from "react";
import { Button } from "./ui/button"; // Assuming the Button component is here
import { useNavigate } from "react-router-dom"; // Importing the useNavigate hook

const Home = () => {
    const navigate = useNavigate();

    const handleLoginClick = () => {
        navigate("/login");
    };

    const handleRegisterClick = () => {
        navigate("/register");
    };

    return (
        <div className="bg-white flex flex-col justify-center w-full">
            {/* Header Section */}
            <header className="w-full h-[88px] bg-accent-default">
                <div className="relative h-full flex items-center justify-between px-8">
                    {/* Logo */}
                    <div className="w-[70px] h-8 flex items-center">
                        <div className="[font-family:'Lato',Helvetica] font-bold text-accent-default text-2xl tracking-[0.20px] leading-8">
                            FlowTrack
                        </div>
                    </div>

                    {/* Login Button */}
                    <Button
                        className="w-[83px] h-[42px] rounded-[25px] bg-accent text-accent-foreground"
                        onClick={handleLoginClick}
                    >
            <span className="font-button-bold-14-18-0-3px text-light-white">
              Log In
            </span>
                    </Button>
                </div>
            </header>

            {/* Main Content Section */}
            <div className="w-full max-w-[1440px] relative mt-[50px]">
                {/* Hero Section */}
                <section className="w-full h-[672px] relative">
                    <div className="absolute w-full h-full bg-accent-default opacity-[0.06]" />

                    <div className="relative h-full flex items-center">
                        <div className="w-full max-w-[1110px] mx-auto flex justify-between">
                            <div className="max-w-[445px] ml-[165px]">
                                <h1 className="font-h2-bold-44-56-0-2px text-foreground mb-8">
                                    Take Control of Your Business Finances
                                </h1>

                                <p className="font-body-1-regular-16-22-0-3px text-muted-foreground mb-12">
                                    Flowtrack is an intuitive financial management tool designed
                                    for small businesses to effortlessly track transactions,
                                    generate insightful financial statements, and forecast future
                                    business performance. With comprehensive features that offer a
                                    clear overview of your business health, FlowTrack ensures
                                    you&apos;re always equipped to make informed financial
                                    decisions.
                                </p>

                                <Button className="w-[158px] h-[42px] bg-accent-default text-accent-foreground rounded-[25px]" onClick={handleRegisterClick}>
                  <span className="font-button-bold-14-18-0-3px text-light-white">
                    Create Account
                  </span>
                                </Button>
                            </div>

                            <div className="mr-[165px]">
                                <img
                                    className="w-[570px] h-[388px]"
                                    alt="Desktop"
                                    src="https://c.animaapp.com/NlTnxe1m/img/desktop.svg"
                                />
                            </div>
                        </div>
                    </div>
                </section>

            </div>
        </div>
    );
};

export default Home;
