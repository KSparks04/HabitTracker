// interface DashboardPageProps {
//     username: string;

import { useAuth } from '../context/AuthContext';
import {HabitSelector} from "../components/HabitSelector.tsx";
import {useState} from "react";
import {IoAddOutline} from "react-icons/io5";
export function DashboardPage(){
    const {user} = useAuth();
    const[openHabit, setOpenHabit] = useState(false);

    return(
        <div>
            <h1>Hi, {user?.username || user?.name || "Guest"}</h1>

            <div className={"habit-selector"}  >
                {!openHabit ? (<div className={"flex items-center justify-center"} onClick={() => setOpenHabit(!openHabit)}>
                    <IoAddOutline/>
                    <h3>Add a Habit</h3>
                     </div>) : <HabitSelector onClose={()=>setOpenHabit(!openHabit)}/>}
            </div>

        </div>

    )
}