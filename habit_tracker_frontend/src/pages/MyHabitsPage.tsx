import {useEffect, useState} from "react";
import * as Icons from "lucide-react";
import {CiEdit} from "react-icons/ci";
import {MdDelete} from "react-icons/md";
import {toast} from "react-toastify";
import {EditHabitCard} from "../components/EditHabitCard.tsx";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { format } from "date-fns";


// import {useNavigate} from "react-router-dom";

export function MyHabitsPage(){
    const[habits, setHabits] = useState([]);
    const [openEditHabit, setOpenEditHabit] = useState(false);
    const[editHabit,setEditHabit]=useState("");
    const [openNotes, setOpenNotes] = useState("");

    const [selectedDate, setSelectedDate] = useState<Date | null>(new Date());
    const [note, setNote] = useState("");
    const [noteDateName, setNoteDateName] = useState("");

    // const navigate = useNavigate();
    useEffect(() => {
        const fetchHabits = async()=>{
            const token = localStorage.getItem("token");
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
            }

        }
        fetchHabits();
    }, []);
    useEffect(() => {
        const fetchHabitNote = async()=>{
            const token = localStorage.getItem("token");
            const isoString = format(selectedDate, "yyyy-MM-dd");
            try {

                const response = await fetch(`/api/habits/logs/notes/${openNotes}/find?date=${isoString}`,{
                    method:"GET",
                    headers: {

                        Authorization: `Bearer ${token}`
                    }
                });
                if (response.ok) {

                    const data = await response.json();
                    console.log(data);
                    if(data.error === "NO LOG FOR DATE"){
                        setNote("No note for this date");
                    }else{
                        setNote(data.note);
                        setNoteDateName(format(selectedDate, "EEE"));
                    }




                } else {
                    console.error("Failed to load habits");
                }
            } catch (err) {
                console.error("Network error:", err);
                alert("Could not connect to the backend server.");
            }

        }
        fetchHabitNote();
    }, [selectedDate]);
    const handleDelete = async(habitId:string,habitName:string) => {
        try{
            const token = localStorage.getItem("token");

            const response = await fetch("http://localhost:8080/api/habits/"+habitId, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    "Authorization": `Bearer ${token}`,
                },

            });const notify = (msg: string) => toast(msg);
            if (response.ok) {




                notify(`${habitName} Successfully Deleted!`);

                // navigate('/', {replace: true});

                window.location.reload();
                return;


            } else {
                notify("Habit could not be deleted!");


            }
        }catch(err) {
            console.error("Network error:", err);
            alert("Could not connect to the backend server.");
        }
    }

    return(
        <div className={"flex justify-center m-2"}>
            <div className={"rounded-xl p-4 max-w-xs md:max-w-sm w-full bg-accent"}>
            <h2 className={"text-black font-bold text-xl"}>Manage your habits</h2>
            {habits.map((habit)=>{
                const IconComponent = (Icons as any)[habit.iconName] || Icons.HelpCircle;
                return (
                    <div key={habit.id} className={"flex flex-col gap-2 items-center justify-center w-full"}>
                        <div className={"flex gap-2 items-center justify-center w-full"}>
                            <IconComponent className=" w-7 h-7 text-blue-600" />
                            <p className={"text-lg text-black"}>{habit.title}</p>
                            <div className={"ml-auto flex items-center gap-1"}>
                                <button className={" text-black hover:text-primary-hover"} onClick={()=>setOpenNotes(habit.id)}>Notes</button>
                                <button  onClick={()=>{setOpenEditHabit(!openEditHabit);setEditHabit(habit.id);}} ><CiEdit  className={"w-7 h-7 text-black hover:text-primary-hover"} /></button>
                                <button onClick={(e)=>{e.preventDefault(); handleDelete(habit.id,habit.title);}}><MdDelete className={"w-7 h-7 text-black hover:text-primary-hover"}  /></button>

                            </div>
                            {openEditHabit && editHabit===habit.id && (
                                <div className={"fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm"}>
                                    <EditHabitCard id={habit.id} name={habit.title} iconName={habit.iconName} frequencySet={habit.frequency} color={""}
                                                   onHabitClose={setOpenEditHabit}/>

                                </div>)}
                        </div>

                        {openNotes === habit.id && <div className={"bg-white border w-full  rounded-xl p-4 shadow-sm flex flex-col gap-2 my-2 transition-all"}>
                            <p className={"font-semibold text-black"}>Select Date to View Notes</p>
                            <DatePicker showIcon  toggleCalendarOnIconClick selected={selectedDate} onChange={setSelectedDate} className="w-full px-3 py-2 text-sm bg-zinc-50 dark:bg-zinc-800 border border-zinc-300 dark:border-zinc-700 rounded-lg text-zinc-800 dark:text-zinc-100 focus:outline-none focus:ring-2 focus:ring-purple-500"/>
                            <p className={"text-black"}><span className={"font-semibold"}>{noteDateName}:</span> {note}</p>
                        </div>}
                    </div>
                )
            })}



        </div>
        </div>

    )
}