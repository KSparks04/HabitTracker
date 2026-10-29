import {useEffect, useState} from "react";
import IconSelector from "./IconSelector.tsx";
import * as Icons from 'lucide-react';
import {FaChevronDown, FaChevronUp} from "react-icons/fa";
import {useNavigate} from "react-router-dom";
import {toast} from 'react-toastify';
import "../css/HabitCard.css";

interface Card {
    name: string,
    iconName: string,
    frequencySet: string,
    color: string,
    onHabitCreated?: (newHabit) => void
}

export function HabitCard({name: initialName, iconName, frequencySet, color, onHabitCreated}: Card) {
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

            title: name,
            iconName: icon,
            frequency: frequency,
            color: color,


        }

        // const url = newHabit ? "http://localhost:8080/api/habits" : `http://localhost:8080/api/habits/${id}`;

        try {
            const token = localStorage.getItem("token");
            console.log(habitData);
            const response = await fetch("http://localhost:8080/api/habits", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    "Authorization": `Bearer ${token}`,
                },
                body: JSON.stringify(habitData),
            });
            const notify = (msg: string) => toast(msg);
            if (response.ok) {

                const data = await response.json();


                notify(`${data.title} Successfully Created!`);
                onHabitCreated(data);
                navigate('/', {replace: true});


                return;


            } else {
                notify("Habit could not be created!");

                navigate('/register', {replace: true});
            }
        } catch (err) {
            console.error("Network error:", err);
            alert("Could not connect to the backend server.");
        }


    }
    return (
        <div>
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

                <button type={"submit"}>Add</button>
            </form>


        </div>
    )
}