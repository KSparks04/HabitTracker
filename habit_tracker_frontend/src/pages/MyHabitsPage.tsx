import {useEffect, useState} from "react";
import * as Icons from "lucide-react";
import {CiEdit} from "react-icons/ci";
import {MdDelete} from "react-icons/md";
import {toast} from "react-toastify";
import {EditHabitCard} from "../components/EditHabitCard.tsx";

// import {useNavigate} from "react-router-dom";

export function MyHabitsPage(){
    const[habits, setHabits] = useState([]);
    const [openEditHabit, setOpenEditHabit] = useState(false);

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
        <div className={"flex justify-center "}>
            <div className={"rounded-xl p-4 max-w-xs md:max-w-sm w-full bg-accent"}>
            <h2>Manage your habits</h2>
            {habits.map((habit)=>{
                const IconComponent = (Icons as any)[habit.iconName] || Icons.HelpCircle;
                return (
                    <div key={habit.id} className={"flex gap-2 items-center justify-center w-full"}>
                        <IconComponent className=" w-7 h-7 text-blue-600" />
                        <p className={"text-lg"}>{habit.title}</p>
                        <div className={"ml-auto"}>
                            <button className={"hover:text-primary-hover"}onClick={()=>{setOpenEditHabit(!openEditHabit)}} ><CiEdit  className={"w-7 h-7 "} color={"black"} /></button>
                            <button className={"hover:text-primary-hover"} onClick={(e)=>{e.preventDefault(); handleDelete(habit.id,habit.title);}}><MdDelete className={"w-7 h-7 "} color={"black"} /></button>

                        </div>
                        {openEditHabit && (
                            <div className={"fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm"}>
                                <EditHabitCard id={habit.id} name={habit.title} iconName={habit.iconName} frequencySet={habit.frequency} color={""}
                                               onHabitClose={setOpenEditHabit}/>

                        </div>)}
                    </div>
                )
            })}



        </div>
        </div>

    )
}