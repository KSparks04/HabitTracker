// interface DashboardPageProps {
//     username: string;

import { useAuth } from '../context/AuthContext';
import {HabitSelector} from "../components/HabitSelector.tsx";
import {useEffect, useState} from "react";
import {IoAddOutline} from "react-icons/io5";
import "../css/DashBoard.css";
export function DashboardPage(){
    const {user} = useAuth();
    const[openHabit, setOpenHabit] = useState(false);
    const[logHabit, setLogHabit] = useState(false);
    const [habits,setHabits] = useState([]);
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
            };


        },[logHabit])
    return(
        <div >
            <h1 className={"text-left text-3xl m-2"}>Hi, {user?.username || user?.name || "Guest"}</h1>
            <div className={"habit-sections m-10 flex flex-wrap flex-col md:flex-row grow-0 shrink-0 items-center justify-center md:justify-normal"}>
                <div className={"log-selector w-full transition-all duration-300 ease-in-out"}>
                    <div className={"section-div shadow-lg transition-transform duration-200 ease-in  max-w-xs w-full md:max-w-80 min-h-20 md:min-h-40 rounded-xl flex flex-col items-center justify-center"}>
                    <h3 className={"text-2xl"}>Log a Habit</h3>
                    <div>
                        <div className={" w-full max-w-40 md:max-w-xs rounded-full"} >
                            <form onSubmit={(e) => e.preventDefault()} className="flex flex-col gap-2">
                                <label className="text-xs font-semibold text-gray-500 dark:text-zinc-400">
                                    Pick a Habit
                                </label>
                                <select
                                    className="w-full rounded-lg border border-gray-200 bg-gray-50 p-2 text-sm text-gray-800 focus:border-indigo-500 focus:outline-none dark:border-zinc-700 dark:bg-zinc-800 dark:text-zinc-100"
                                    // onChange={() => {
                                    //
                                    //     setLogHabit(false);
                                    // }}
                                    onClick={()=>setLogHabit(!logHabit)}
                                >
                                    <option value="" disabled>
                                        {habits.length == 0 ? "Loading habits..." : "Choose from your habits..."}
                                    </option>
                                    {habits.length > 0 && habits.map((habit) => (
                                        <option key={habit.id} value={habit.id}>
                                            {habit.title} ({habit.frequency})
                                        </option>))}

                                </select>
                            </form>
                        </div>

                    </div>

                </div></div>

                <div className={"habit-selector w-full transition-all duration-300 ease-in-out"}  >
                    {!openHabit ? (<div className={"section-div transition-transform duration-200 ease-in shadow-lg p-4 max-w-xs w-full md:max-w-80 min-h-20 md:min-h-40  flex flex-row items-center justify-center rounded-xl "} onClick={() => setOpenHabit(!openHabit)}>
                        <IoAddOutline color={"black"}/>
                        <h3 className={"text-2xl"}>Add a Habit</h3>
                    </div>) : (<div className={"w-full animate-in fade-in zoom-in-95 duration-300 ease-in-out"}><HabitSelector onClose={()=>setOpenHabit(!openHabit)}/></div>)}
                </div>
            </div>


        </div>

    )
}