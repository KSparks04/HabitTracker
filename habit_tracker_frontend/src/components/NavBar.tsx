import {NavLink} from 'react-router-dom';

export function NavBar(){
    // const scrollTo = (id) => {
    //     document.getElementById(id)?.scrollIntoView({ behavior: 'smooth' });
    // };
    return (
        <div className={" flex flex-column md:flex-row justify-evenly items-center text-xl"}>

            <NavLink className={""} to={"/"}>Home</NavLink>
            <NavLink className={""} to={"/register"}>Register</NavLink>
            <NavLink className={""} to={"/login"}>Login</NavLink>

        </div>
    )
}