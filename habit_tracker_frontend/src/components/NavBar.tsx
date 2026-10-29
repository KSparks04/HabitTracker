import {NavLink, useNavigate} from 'react-router-dom';
import "../css/NavBar.css";
import {useAuth} from "../context/AuthContext.tsx";
import * as React from "react";
import {MdOutlineAccountCircle} from "react-icons/md";
import {VscSignOut} from "react-icons/vsc";
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
        <div className={" flex flex-row justify-evenly items-center text-xl "}>

            <NavLink className={"nav-elem"} to={"/"}>Dashboard</NavLink>
            {!user && <NavLink className={"nav-elem"} to={"/register"}>Register</NavLink>}
            <NavLink className={"nav-elem"} to={"/stats"}>Habit Stats</NavLink>
            {/*<NavLink className={"nav-elem"} to={"/login"}>Login</NavLink>*/}
            <div className={"relative inline-block"} onClick={()=>{setOpen(!isOpen)}}><MdOutlineAccountCircle color={"black"} className={"w-5 h-5"} />
            {isOpen &&
            <div className={"origin-top-right absolute right-0 mt-2 w-48 rounded-lg shadow-lg bg-white dark:bg-zinc-600 ring-1 ring-black ring-opacity-5 focus:outline-none z-50 animate-in fade-in-0 zoom-in-95 p-1"}  role="menu"
                 aria-orientation="vertical">
                <NavLink to={"/my-habits"}>My Habits</NavLink>

                <button className={"nav-elem flex items-center justify-center"} onClick={handleLogout}><VscSignOut /> Signout</button>

            </div>}
            </div>


        </div>
    )
}