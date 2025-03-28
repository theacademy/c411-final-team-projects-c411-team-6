import React, { useState } from 'react';
import axios from 'axios';
import LandingHeader from "./ui/LandingHeader";
import {Input} from "./ui/input";
import {Button} from "./ui/button";
import {useNavigate} from "react-router-dom";

const Register = () => {
  const navigate = useNavigate();

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  const handleRegister = async (e) => {
    e.preventDefault();

    setError('');
    setSuccessMessage('');

    try {
      const response = await axios.post('http://localhost:8080/users/register', {
        username,
        password,
      });

      if (response.status === 201) {
        setSuccessMessage('Registration successful! Redirecting to login...');
        navigate("/login"); // 👈 redirect here
      }
    } catch (err) {
      if (err.response && err.response.data) {
        setError(err.response.data.error || 'An error occurred during registration.');
      } else {
        setError('An unexpected error occurred.');
      }
    }
  };



  const handleLoginClick = () => {
    navigate("/login");
  };


  return (
      <div className="bg-white flex flex-col justify-center w-full mb-16 px-8 sm:px-12 md:px-20 lg:px-32 xl:px-48">
        {/* Header Section */}
        <LandingHeader onLoginClick={handleLoginClick} bgColor="bg-white" />
        {/* Heading */}
        <div className="w-full ">
          <section className="w-full mt-[120px]">
            <div className="text-center mb-4 px-4 sm:px-8">
              <h2 className="font-h3-bold-32-40-0-1px text-light-black  text-xl sm:text-2xl md:text-3xl lg:text-4xl">
                Let's get started...
              </h2>
            </div>
          </section>

          <section className="w-full">
            <div className="text-center mb-16 px-4 sm:px-8">
              <form onSubmit={handleRegister}>
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
                      type="password"
                      value={password}
                      placeholder={"Password"}
                      onChange={(e) => setPassword(e.target.value)}
                  />
                </div>

                <div className="flex flex-row justify-center items-center gap-4 mt-6 mb-16">

                  <Button
                      className="w-[170px] h-[42px] rounded-[25px] text-white"
                      type="submit"
                  >
                    <span className="font-button-bold-14-18-0-3px">Register</span>
                  </Button>

                  <Button
                      type="button"
                      className="w-[170px] h-[42px] rounded-[25px] text-white"
                      onClick={handleLoginClick}
                  >
                    <span className="font-button-bold-14-18-0-3px">Have an account?</span>
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

export default Register;


