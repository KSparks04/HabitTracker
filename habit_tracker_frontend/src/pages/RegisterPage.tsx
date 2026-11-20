import {NavLink, useNavigate} from "react-router-dom";
import {useState} from "react";
import { useAuth } from '../context/AuthContext';


export function RegisterPage(){
    const navigate = useNavigate();
    const [userData,setUserData]=useState<{
        username:string,
        email: string,
        password: string,
    }>({username:'', email:'', password:''});
    const [errors, setErrors] = useState<{username:string,email:string,password:string}>({username:"", email: "",password:""});
    const {login} = useAuth();
    const handleSubmit = async (e:{preventDefault:()=>void}) => {
        e.preventDefault();
        if(validate()){
            try {
                const response = await fetch("http://localhost:8080/api/auth/register", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(userData),
                });
                if (response.ok) {

                    const data = await response.json();
                    if(!data.token){
                        navigate('/register',{replace:true});
                        return;
                    }
                    login(data.user,data.token);
                    navigate('/',{replace:true, state:{username:userData.username}});
                } else {
                    alert("Failed to send form");
                }
            } catch (err) {
                console.error("Network error:", err);
                alert("Could not connect to the backend server.");
            }
        }else{
            console.log("Bad Request");
            return;
        }

    }
    const validate = ()=>{
        let tempErrors = {username:'', email:'', password:''};
        let isValid = true;
        if(!userData.username){
            isValid=false;
            tempErrors.username = "Username is required";
        }
        if(!userData.email){
            isValid=false;
            tempErrors.email = "Email is required";
        }
        if(!userData.password){
            isValid=false;
            tempErrors.password = "Password is required";
        }
        const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if(!emailPattern.test(userData.email)){
            tempErrors.email = "Email is not valid";
            isValid = false;
        }
        if(userData.username.length < 5 || userData.username.length > 10){
            isValid=false;
            tempErrors.username = "Username invalid length";
        }
        if(userData.password.length < 8){
            isValid=false;
            tempErrors.password = "Password is too short";
        }


        setErrors(tempErrors);
        return isValid;

    }
    return(
        <div className={"flex flex-col justify-center items-center "}>

            <form onSubmit={handleSubmit} className={"form-login mt-2 bg-surface shadow-md max-w-md md:max-w-lg w-full rounded-lg p-4 flex flex-col items-center"}>
                <h3 className={"text-black font-bold text-xl"}>Register</h3>
                <div className={"flex flex-col justify-center items-center w-60 gap-2 mt-3"}>
                    <input type={"text"}value={userData.username} placeholder={"YourUsername"} className={"rounded-lg border border-b-text-muted border-[1.5px] w-full px-3.5"} onChange={(e: React.ChangeEvent<HTMLInputElement>) => setUserData({
                    ...userData,
                    username: e.target.value
                })}></input>
                    {errors.username && (<p className={"error-p text-red-500 text-xs italic"}>{errors.username}</p>)}

                    <input type={"text"}value={userData.email} placeholder={"youremail@company.com"} className={"rounded-lg border border-b-text-muted border-[1.5px]  w-full px-3.5"}  onChange={(e: React.ChangeEvent<HTMLInputElement>) => setUserData({
                        ...userData,
                        email: e.target.value
                    })}></input>
                    {errors.email && (<p className={"error-p text-red-500 text-xs italic"}>{errors.email}</p>)}

                    <input type={"password"}value={userData.password} placeholder={"Password"} className={"rounded-lg border border-b-text-muted border-[1.5px]  w-full px-3.5"} onChange={(e: React.ChangeEvent<HTMLInputElement>) => setUserData({
                        ...userData,
                        password: e.target.value
                    })}></input>
                        {errors.password && (<p className={"error-p text-red-500 text-xs italic"}>{errors.password}</p>)}

                </div>

                <button type="submit" className={"text-black font-semibold bg-accent hover:bg-lavender p-2 rounded-lg mt-3 w-30"}>Register</button>
            </form>
            <NavLink to={"/login"}>
                <p className={"text-black"}>Already have an account? Log in</p>
            </NavLink>
        </div>
    )
}