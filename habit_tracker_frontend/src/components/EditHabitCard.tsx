import {useEffect, useState} from "react";
import IconSelector from "./IconSelector.tsx";
import * as Icons from 'lucide-react';
import {FaChevronDown, FaChevronUp} from "react-icons/fa";
import {useNavigate} from "react-router-dom";
import {toast} from 'react-toastify';
import "../css/HabitCard.css";
import {IoMdClose} from "react-icons/io";

interface Card {
    id: number;
    name: string,
    iconName: string,
    frequencySet: string,
    color: string,
     onHabitClose?: (habit) => void
}

export function EditHabitCard({id,name: initialName, iconName, frequencySet, color, onHabitClose}: Card) {
    const navigate = useNavigate();
    const [name, setName] = useState(initialName || "");
    const [icon, setIcon] = useState(iconName || 'Home');
    const [openIconSelect, setOpenIconSelect] = useState<boolean>(false);
    const SelectedIconComponent = (Icons as any)[icon] || Icons.HelpCircle;
    const [frequencyOpen, setFrequencyOpen] = useState(false);
    const [frequency, setFrequency] = useState<string>(frequencySet || "DAILY");

    const handleFrequencySet = (freq: string) => {
        setFrequencyOpen(false);
        setFrequency(freq);

    }
    useEffect(() => {
        setName(initialName || '');
        setIcon(iconName || 'Home');
        setFrequency(frequencySet || 'DAILY');
    }, [initialName, iconName, frequencySet]);

    // console.log("Props received:", {
    //     initialName,
    //     iconName,
    //     frequencySet
    // });
    //
    // console.log("Current state:", {
    //     name,
    //     icon
    // });
    const handleSubmit = async (e: { preventDefault: () => void }) => {
        e.preventDefault();
        frequencySet = frequency;
        const habitData = {
            id:id,
            title: name,
            iconName: icon,
            frequency: frequency,
            color: color,


        }

        // const url = newHabit ? "http://localhost:8080/api/habits" : `http://localhost:8080/api/habits/${id}`;

        try {
            const token = localStorage.getItem("token");
            const response = await fetch("http://localhost:8080/api/habits", {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    "Authorization": `Bearer ${token}`,
                },
                body: JSON.stringify(habitData),
            });
            const notify = (msg: string) => toast(msg);
            if (response.ok) {

                const data = await response.json();


                notify(`${data.title} Successfully Edited!`);
                // onHabitCreated(data);
                navigate('/my-habits', {replace: true});
                window.location.reload();

                return;


            } else {
                notify("Habit could not be updated!");


            }
        } catch (err) {
            console.error("Network error:", err);
            alert("Could not connect to the backend server.");
        }


    }
    return (
        <div className={"w-full max-w-md rounded-2xl bg-white p-6 shadow-2xl dark:bg-zinc-900 "}>
            <form onSubmit={handleSubmit}>

                <div>


                    <div>
                        <SelectedIconComponent className="w-6 h-6 text-blue-600" onClick={() => {
                            setOpenIconSelect(!openIconSelect)
                        }}/>
                        <input
                            type="text"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            className="habit-card-input border p-1 rounded"
                        />
                    </div>
                    {openIconSelect && <IconSelector selectedIcon={icon} onSelect={(newIcon) => setIcon(newIcon)}/>}
                    <div onClick={() => setFrequencyOpen(!frequencyOpen)}>
                        {!frequencyOpen ? (
                            <div className={"flex gap-1"}>
                                <h4>Frequency</h4>
                                <FaChevronDown/>
                                <p>{frequency}</p>
                            </div>
                        ) : (
                            <div>
                                <div className={"flex gap-1"}>
                                    <h4>Frequency</h4>
                                    <FaChevronUp/>
                                    <p>{frequency}</p>
                                </div>
                                <div className={"flex flex-col"}>
                                    <div onClick={() => handleFrequencySet("DAILY")}><p>Daily</p></div>
                                    <div onClick={() => handleFrequencySet("WEEKLY")}><p>Weekly</p></div>
                                </div>
                            </div>

                        )}
                    </div>
                </div>

                <button type={"submit"}>Update</button>
            </form>
            <button
                onClick={() => onHabitClose(false)}
                className="mt-4 rounded bg-gray-200 px-4 py-2 text-black dark:bg-zinc-800 dark:text-white">
                <IoMdClose/>
            </button>

        </div>
    )
}