import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import LandingHeader from "./ui/LandingHeader";
import {Button} from "./ui/button";
import { Input } from "./ui/input";

const Login = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    const userData = { username, password };

    try {
      // Send POST request to login the user
      const response = await fetch("http://localhost:8080/users/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(userData),
      });

      if (response.ok) {
        // If login is successful, redirect to Plaid link page
        // In Login Component after successful login:
        const data = await response.json();
        localStorage.setItem("userID", data.id);
        navigate("/link-account");
      } else {
        const error = await response.text();
        alert(error);
      }
    } catch (error) {
      console.error("Error occurred during login", error);
      alert("An error occurred during login");
    }
  };

  const handleLoginClick = () => {
    navigate("/login");
  };

  const handleRegisterClick = () => {
    navigate("/register");
  };

  return (
      <div className="bg-white flex flex-col justify-center w-full mb-16 px-8 sm:px-12 md:px-20 lg:px-32 xl:px-48">
        {/* Header Section */}
        <LandingHeader onLoginClick={handleLoginClick} bgColor="bg-white" />
        {/* Heading */}
        <div className="w-full bg-muted">
          <section className="w-full mt-[120px]">
            <div className="text-center mb-4 px-4 sm:px-8">
              <h2 className="font-h3-bold-32-40-0-1px text-light-black  text-xl sm:text-2xl md:text-3xl lg:text-4xl">
                Welcome back!
              </h2>
            </div>
          </section>

          <section className="w-full bg-muted">
            <div className="text-center mb-16 px-4 sm:px-8">
              <form onSubmit={handleLogin}>
                <div className={"mb-2"}>
                  <Input
                      type="text"
                      value={username}
                      placeholder={"Username"}
                      onChange={(e) => setUsername(e.target.value)}
                  />
                </div>

                <div className="mb-2">
                  <Input
                      type="text"
                      value={password}
                      placeholder={"Password"}
                      onChange={(e) => setPassword(e.target.value)}
                  />
                </div>

                <div className="flex flex-row justify-center items-center gap-4 mt-6 mb-16">

                <Button
                      className="w-[170px] h-[42px] rounded-[25px] text-white"
                      onClick={handleLogin}
                  >
                    <span className="font-button-bold-14-18-0-3px">Log In</span>
                  </Button>

                  <Button
                      className="w-[170px] h-[42px] rounded-[25px] text-white"
                      onClick={handleRegisterClick}
                  >
                    <span className="font-button-bold-14-18-0-3px">Don't have an account?</span>
                  </Button>
                </div>

              </form>


            </div>

          </section>
        </div>


        <div>

        </div>


      </div>
  );
};

export default Login;