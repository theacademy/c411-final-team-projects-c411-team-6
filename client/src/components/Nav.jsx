import React from "react";
import { Button } from "./ui/button"; // Assuming the Button component is here

// Reusable Nav Component
const Nav = ({ navItems, logoText, loginText }) => {
    return (
        <header className="w-full h-[88px] bg-accent-default">
            <div className="relative h-full flex items-center justify-between px-8">
                {/* Logo Section */}
                <div className="w-[70px] h-8">
                    <div className="[font-family:'Lato',Helvetica] font-bold text-accent-default text-2xl tracking-[0.20px] leading-8">
                        {logoText || "LOGO"}
                    </div>
                </div>

                {/* Navigation Links Section */}
                <nav className="flex-1 flex justify-center">
                    <div className="w-[840px] h-[62px] border border-solid border-[#d9d9d9] rounded-[25px] bg-accent-default flex items-center justify-center">
                        <div className="w-[510px] h-[45px] bg-muted rounded-[25px] flex items-center justify-center">
                            <ul className="flex space-x-16">
                                {navItems.map((item, index) => (
                                    <li key={index} className="font-button-bold-14-18-0-3px text-light-white">
                                        {item}
                                    </li>
                                ))}
                            </ul>
                        </div>
                    </div>
                </nav>

                {/* Login Button */}
                <Button className="w-[83px] h-[42px] rounded-[25px] bg-accent text-accent-foreground">
                    <span className="font-button-bold-14-18-0-3px text-light-white">{loginText || "Log In"}</span>
                </Button>
            </div>
        </header>
    );
};

// Default Props
Nav.defaultProps = {
    navItems: ["Home", "Services", "About Us", "Contact"],  // Default nav items
    logoText: "Company Logo",  // Default logo text
    loginText: "Log In",  // Default login button text
};

export default Nav;
