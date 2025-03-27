import React from "react";
import { Button } from "./button";

const LandingHeader = ({ title = "FlowTrack", onLoginClick, buttonText = "Log In", bgColor = "bg-white" }) => {
    return (
        <header className="w-full h-[88px] ${bgColor}">
            <div className="relative h-full flex items-center justify-between px-8">
                {/* Logo */}
                <div className="w-[70px] h-8 flex items-center">
                    <div className="[font-family:'Lato',Helvetica] font-bold text-2xl tracking-[0.20px] leading-8">
                        {title}
                    </div>
                </div>

                {/* Login Button */}
                <Button
                    className="w-[83px] h-[42px] rounded-[25px] text-accent-foreground"
                    onClick={onLoginClick}
                >
                    <span className="font-button-bold-14-18-0-3px text-white">
                        {buttonText}
                    </span>
                </Button>
            </div>
        </header>
    );
};

export default LandingHeader;
