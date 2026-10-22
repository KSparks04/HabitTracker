import {NavLink} from 'react-router-dom';
import "../css/NavBar.css";
export function NavBar(){
    // const scrollTo = (id) => {
    //     document.getElementById(id)?.scrollIntoView({ behavior: 'smooth' });
    // };
    return (
        <div className={" flex flex-column md:flex-row justify-evenly items-center text-xl "}>

            <NavLink className={"nav-elem"} to={"/"}>Home</NavLink>
            <NavLink className={"nav-elem"} to={"/register"}>Register</NavLink>
            <NavLink className={"nav-elem"} to={"/login"}>Login</NavLink>

        </div>
    )
}