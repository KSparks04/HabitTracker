import { useState } from 'react'
import heroImg from './assets/hero.png'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import './App.css'
import {Navigate, Route, Routes} from "react-router-dom";
import {RegisterPage} from "./pages/RegisterPage.tsx";
import{DashboardPage} from "./pages/DashboardPage.tsx";
import {NavBar} from "./components/NavBar.tsx";
import { useAuth } from './context/AuthContext';
import {LoginPage} from "./pages/LoginPage.tsx";
import {Bounce, ToastContainer} from "react-toastify";

function App() {
  const [count, setCount] = useState(0);
  const { user } = useAuth();
  return (
    <>
        <NavBar/>
        <ToastContainer
            position="top-right"
            autoClose={3000}
            hideProgressBar={false}
            newestOnTop={false}
            closeOnClick
            rtl={false}
            pauseOnFocusLoss
            draggable
            pauseOnHover
            theme="light"
            transition={Bounce}
        />
        {/* Same as */}
        <ToastContainer />
        <Routes>
            <Route
                path="/"
                element={user ? <DashboardPage /> : <Navigate to="/register" replace />}
            />
            <Route path="/register" element={!user ? <RegisterPage /> : <Navigate to="/" />} />
            <Route path="/login" element={!user ? <LoginPage /> : <Navigate to="/" />} />
        </Routes>

    </>
  )
}

export default App
