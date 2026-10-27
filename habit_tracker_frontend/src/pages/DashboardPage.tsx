// interface DashboardPageProps {
//     username: string;

import { useAuth } from '../context/AuthContext';
import {HabitSelector} from "../components/HabitSelector.tsx";
import {useEffect, useState} from "react";
import {IoAddOutline} from "react-icons/io5";
import "../css/DashBoard.css";
import {toast} from "react-toastify";
import {useNavigate} from "react-router-dom";
import {RecentHabits} from "../components/RecentHabits.tsx";
import * as Icons from "lucide-react";

export function DashboardPage(){
    const navigate = useNavigate();
    const {user} = useAuth();
    const[openHabit, setOpenHabit] = useState(false);
    const[logHabit, setLogHabit] = useState(true);
    const [habits,setHabits] = useState([]);
    const [loggedHabit, setLoggedHabit] = useState<string | null>(null);
    const [recentHabits, setRecentHabits] = useState([]);
    const handleHabitCreated = (newHabit) => {
        setHabits(prevHabits => [...prevHabits, newHabit]); // Instant UI update!
    };
    useEffect(()=>{
        if(logHabit && habits.length == 0){
            const loadHabits = async ()=>{
                const token = localStorage.getItem('token');
                try {

                    const response = await fetch("/api/habits",{
                        method:"GET",
                        headers: {

                            Authorization: `Bearer ${token}`
                        }
                    });
                    if (response.ok) {

                        const data = await response.json();
                        console.log(data);
                        setHabits(data);



                    } else {
                        console.error("Failed to load habits");
                    }
                } catch (err) {
                    console.error("Network error:", err);
                    alert("Could not connect to the backend server.");
                }}
            loadHabits();
            }


        },[logHabit])
    useEffect(() => {
        const loadRecentHabits = async()=>{
            const token = localStorage.getItem('token');
            try {

                const response = await fetch("/api/habit/logs/recent",{
                    method:"GET",
                    headers: {

                        Authorization: `Bearer ${token}`
                    }
                });
                if (response.ok) {

                    const data = await response.json();
                    console.log(data);
                    setRecentHabits(data);



                } else {
                    console.error("Failed to load habits");
                }
            } catch (err) {
                console.error("Network error:", err);
                alert("Could not connect to the backend server.");
            }
        }
        loadRecentHabits();

    }, []);
    const handleHabitLogSubmit =async (e:{preventDefault:()=>void})=>{
        e.preventDefault();
        const token = localStorage.getItem('token');
        const payload = { habitId: Number(loggedHabit) };
        console.log(loggedHabit);
        console.log(payload);
        try {

            const response = await fetch("/api/habit/log",{
                method:"POST",
                headers: {
                    "Content-Type": 'application/json',
                    Authorization: `Bearer ${token}`
                },
                body:JSON.stringify(payload)
            });
            const notify = (msg:string) => toast(msg);
            if (response.ok) {

                // const data = await response.json();


                notify(`Successfully Logged!`);
                navigate("/",{replace:true});


                return;


            } else {
                notify("Habit could not be logged!");


            }
        } catch (err) {
            console.error("Network error:", err);
            alert("Could not connect to the backend server.");
        }
    }
    return(
        <div >
            <h1 className={"text-left text-3xl m-2"}>Hi, {user?.username || user?.name || "Guest"}</h1>
            <div className={"habit-sections m-10 flex flex-wrap flex-col md:flex-row grow-0 shrink-0 items-center justify-center md:justify-normal gap-2"}>
                <div className={"recent-view w-full transition-all duration-300 ease-in-out max-w-60 md:max-w-xs"}>
                    <div className={"section-div shadow-lg transition-transform duration-200 ease-in  max-w-xs w-full md:max-w-80 min-h-20 md:min-h-40 rounded-xl flex flex-col items-center justify-center"}>
                        <RecentHabits/>
                    </div>

                </div>
                <div className={"log-selector w-full transition-all duration-300 ease-in-out max-w-60 md:max-w-xs"}>
                    <div className={"section-div shadow-lg transition-transform duration-200 ease-in  max-w-xs w-full md:max-w-80 min-h-20 md:min-h-40 rounded-xl flex flex-col items-center justify-center"}>
                    <h3 className={"text-2xl"}>Log a Habit</h3>
                    <div>
                        <div className={" w-full max-w-40 md:max-w-xs rounded-full"} >
                            <form onSubmit={handleHabitLogSubmit} className="flex flex-col gap-2">
                                <select
                                    className="w-full rounded-lg border border-gray-200 bg-gray-50 p-2 text-sm text-gray-800 focus:border-indigo-500 focus:outline-none dark:border-zinc-700 dark:bg-zinc-800 dark:text-zinc-100"
                                    onChange={(e) => {
                                        const selectedId = e.target.value;
                                        setLoggedHabit(selectedId);

                                        // Log it to verify immediately
                                        console.log("Selected Habit ID:", selectedId);

                                        setLogHabit(false);


                                    }}

                                >
                                    <option value="" disabled>
                                        {habits.length == 0 ? "Loading habits..." : "Choose from your habits..."}
                                    </option>
                                    {habits.length > 0 && habits.map((habit) => (
                                        <option key={habit.id} value={habit.id} >
                                            {habit.title} ({habit.frequency})
                                        </option>))}

                                </select>
                                <button type={"submit"} className={"submit  rounded-full"}>Log</button>
                            </form>
                        </div>

                    </div>

                </div></div>

                <div className={"habit-selector w-full  transition-all duration-300 ease-in-out  max-w-60 md:max-w-xs"}  >
                    {!openHabit ? (<div className={"section-div  transition-transform duration-200 ease-in shadow-lg p-4 max-w-xs w-full md:max-w-80 min-h-20 md:min-h-40  flex flex-row items-center justify-center rounded-xl "} onClick={() => setOpenHabit(!openHabit)}>
                        <IoAddOutline color={"black"}/>
                        <h3 className={"text-2xl"}>Add a Habit</h3>
                    </div>) : (<div className={"w-full animate-in fade-in zoom-in-95 duration-300 ease-in-out"}><HabitSelector onHabitCreated ={handleHabitCreated} onClose={()=>setOpenHabit(!openHabit)}/></div>)}
                </div>
                <div className={"recent-div w-full  transition-all duration-300 ease-in-out  max-w-60 md:max-w-xs"}>
                    <div className={"section-div  transition-transform duration-200 ease-in shadow-lg p-4 max-w-xs w-full md:max-w-80 min-h-20 md:min-h-40  flex flex-col items-center justify-center rounded-xl "}>
                        <h3 className={"text-2xl"}>Recent Habits</h3>
                        <div className={"flex flex-col items-center justify-center max-w-2xs md:max-w-xs w-full"}>
                            {recentHabits.map(habitData =>{
                                const IconComponent = (Icons as any)[habitData.habit.iconName] || Icons.HelpCircle;
                                return (
                                    <div key={habitData.habit.id} className={"recent-habit w-full rounded-xl max-w-2xs md:max-w-xs flex flex-col md:flex-row items-center justify-center"}>
                                        <div className={"icon m-2 p-2 rounded-full flex shrink-0 items-center justify-center"}>
                                            <IconComponent className=" w-7 h-7 text-blue-600" />
                                        </div>
                                        <p>{habitData.habit.title}</p>

                                    </div>
                                )
                            })}
                        </div>

                    </div>
                </div>
            </div>


        </div>

    )
}