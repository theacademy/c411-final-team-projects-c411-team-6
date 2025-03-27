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
            "Eos tota dicunt democritum no. Has natum gubergren ne. Est viris soleat sadipscing cu. Legere epicuri insolens eu nec, dicit virtute urbanitas id nam, qui in habeo semper eligendi.",
    },
    {
        icon: <TagIcon className="w-10 h-10" />,
        title: "Financial Statement Generator",
        description:
            "Ne nam phaedrum consequat, adhuc aliquid ea pri, eum eligendi incorrupte referrentur in. Vix ad senserit salutandi argumentum. Ei eam definiebas reformidans, exerci persecuti no ius.",
    },
    {
        icon: <LifeBuoyIcon className="w-10 h-10" />,
        title: "Business Financial Forecasting",
        description:
            "Assum suavitate ea vel, vero erat doming cu cum. Zril ornatus sea cu. Pro ex integre pertinax. Cu cum harum paulo legendos, mei et quod enim mnesarchum, habeo affert laoreet sea ei.",
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
            <LandingHeader></LandingHeader>

            {/* Main Content Section */}
            <div className="w-full max-w-[1440px] relative mt-[50px]">
                <section className="w-full h-[600px] relative">
                    <div className="absolute w-full h-full bg-accent-default opacity-[0.06]" />

                    <div className="relative h-full flex items-center">
                        <div className="w-full max-w-[1110px] mx-auto flex flex-col lg:flex-row items-center justify-between px-4 lg:px-8 gap-8">

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
                        <p className="font-body-2-regular-14-20-0-2px text-light-black text-center sm:py-2 md:py-4 lg:py-6 "> Take control of your Business finances.</p>
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
