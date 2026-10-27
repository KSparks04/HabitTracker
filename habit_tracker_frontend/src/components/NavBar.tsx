import {NavLink, useNavigate} from 'react-router-dom';
import "../css/NavBar.css";
import {useAuth} from "../context/AuthContext.tsx";
export function NavBar(){
    // const scrollTo = (id) => {
    //     document.getElementById(id)?.scrollIntoView({ behavior: 'smooth' });
    // };
    const navigate = useNavigate();
    const {user} = useAuth();
    const logout = ()=>{
        user.logout();
        navigate("/",{replace:true});
    }
    return (
        <div className={" flex flex-column md:flex-row justify-evenly items-center text-xl "}>

            <NavLink className={"nav-elem"} to={"/"}>Home</NavLink>
            <NavLink className={"nav-elem"} to={"/register"}>Register</NavLink>
            <NavLink className={"nav-elem"} to={"/login"}>Login</NavLink>
            <a className={"nav-elem"} onClick={logout}>Logout</a>

        </div>
    )
}