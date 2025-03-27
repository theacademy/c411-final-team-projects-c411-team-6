import React from "react";

export const Input = ({ value, onChange, placeholder = "", type = "text" }) => {
    return (
        <input
            type={type}
            value={value}
            onChange={onChange}
            placeholder={placeholder}
            className="w-[384px] h-[35px] border border-black px-2 rounded-full"
        />
    );
};

