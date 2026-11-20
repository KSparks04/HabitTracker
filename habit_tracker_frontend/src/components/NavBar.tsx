import {NavLink, useNavigate} from 'react-router-dom';
import "../css/NavBar.css";
import {useAuth} from "../context/AuthContext.tsx";
import * as React from "react";
import {MdOutlineAccountCircle} from "react-icons/md";
import {VscSignOut} from "react-icons/vsc";
import {IoList} from "react-icons/io5";
export function NavBar(){
    // const scrollTo = (id) => {
    //     document.getElementById(id)?.scrollIntoView({ behavior: 'smooth' });
    // };
    const navigate = useNavigate();
    const {user,logout} = useAuth();
    const [isOpen , setOpen] = React.useState(false);
    const handleLogout = (e)=>{
        e.preventDefault();
        logout();
        navigate("/",{replace:true});
        window.location.reload();
    }

    return (
        <div className={" flex flex-row justify-evenly items-center text-xl bg-accent p-4 duration-200 ease-in-out"}>
            <h1 className={"font-semibold text-3xl text-black font-sans "}>Habitual</h1>
            <NavLink className={"nav-elem transition-transform duration-200 ease-in rounded-xl p-2 font-semibold hover:bg-bg hover:text-text-main"} to={"/"}>Dashboard</NavLink>

            <NavLink className={"nav-elem transition-transform duration-200 ease-in  rounded-xl p-2 font-semibold hover:bg-bg hover:text-text-main"} to={"/my-stats"}>Habit Stats</NavLink>
            {/*<NavLink className={"nav-elem"} to={"/login"}>Login</NavLink>*/}
            <div className={"nav-elem transition-transform duration-200 ease-in   relative inline-block rounded-xl p-2  font-semibold hover:bg-bg hover:text-text-main"} onClick={()=>{setOpen(!isOpen)}}><MdOutlineAccountCircle color={"black"} className={"w-7 h-7"} />
            {isOpen && user &&
            <div className={"origin-top-right absolute right-0 mt-2 w-48 rounded-lg shadow-lg bg-white dark:bg-zinc-200 ring-1 ring-black ring-opacity-5 focus:outline-none z-50 animate-in fade-in-0 zoom-in-95 p-1"}  role="menu"
                 aria-orientation="vertical">
                <NavLink to={"/my-habits"} className={" flex items-center gap-1"}><IoList /> My Habits</NavLink>

                <button className={" flex items-center justify-center gap-1"} onClick={handleLogout}><VscSignOut /> Signout</button>

            </div>}
                {isOpen&& !user && <div className={"origin-top-right absolute right-0 mt-2 w-48 rounded-lg shadow-lg bg-white dark:bg-zinc-200 ring-1 ring-black ring-opacity-5 focus:outline-none z-50 animate-in fade-in-0 zoom-in-95 p-1"}>
                     <NavLink className={"text-black font-semibold hover:bg-lilac hover:text-white"} to={"/register"}>Sign up/Login</NavLink>
                </div>}
            </div>


        </div>
    )
}