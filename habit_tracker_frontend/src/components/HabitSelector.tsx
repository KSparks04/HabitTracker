import {useEffect, useState} from "react";
import * as Icons from 'lucide-react';
import { IoAddOutline } from "react-icons/io5";
import {IoMdClose} from "react-icons/io";
import {HabitCard} from "./HabitCard.tsx";
interface HabitSelectorProps{
    onClose?: () => void;
}
export function HabitSelector({onClose}: HabitSelectorProps){

    const[habitTemplates, setHabitTemplates]= useState([]);

    const [selectedTemplate, setSelectedTemplate] = useState<string | null>(null);
    const [openNewHabit, setOpenNewHabit] = useState(false);
    useEffect(()=>{ async function fetchTemplates(){
        try {
            const response = await fetch("/api/templates");
            if (response.ok) {

                const data = await response.json();
                setHabitTemplates(data);



            } else {
                alert("Failed to send form");
            }
        } catch (err) {
            console.error("Network error:", err);
            alert("Could not connect to the backend server.");
        }}
        fetchTemplates();
    },[])
    return(
        <div className={"transition-all duration-300 ease-in-out"}>

            {habitTemplates.map((template)=>{
                const IconComponent = (Icons as any)[template.iconName] || Icons.HelpCircle;
                return (
                    <div key={template.id}>
                        {selectedTemplate !== template.id ? (
                            <div  className=" flex max-w-20 md:max-w-100 w-full h-6" onClick={() => {
                                setSelectedTemplate(template.id);
                                setOpenNewHabit(false);
                            }}>
                                <IconComponent className="w-5 h-5 text-blue-600" />
                                <p>{template.title}</p>
                            </div>):(
                                <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm" >

                                    {/* 2. The actual modal box */}
                                    <div className="w-full max-w-md rounded-2xl bg-white p-6 shadow-2xl dark:bg-zinc-900" >
                                        <HabitCard   name={template.title} iconName ={template.iconName} frequencySet={template.default_frequency} color={""} />

                                        <button
                                            onClick={() => setSelectedTemplate(null)}
                                            className="mt-4 rounded bg-gray-200 px-4 py-2 text-black dark:bg-zinc-800 dark:text-white">
                                            <IoMdClose/>
                                        </button>
                                    </div>

                                </div>)
                        }
                    </div>
                )
            })}
            {!openNewHabit? (<div className=" flex max-w-20 md:max-w-100 w-full h-6"  onClick={() => {
                setOpenNewHabit(true);
                setSelectedTemplate(null);
            }}>

                <IoAddOutline/>
                <p>Add a new habit</p>

            </div>):((
                <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm">

            {/* 2. The actual modal box */}
            <div className="w-full max-w-md rounded-2xl bg-white p-6 shadow-2xl dark:bg-zinc-900" >
                <HabitCard  name={""} iconName={""} frequencySet={""} color={""}/>

                <button
                    onClick={() => setOpenNewHabit(false)}
                    className="mt-4 rounded bg-gray-200 px-4 py-2 text-black dark:bg-zinc-800 dark:text-white"
                >
                    <IoMdClose/>

                </button>
            </div>

        </div>)

            )}
            {(!selectedTemplate && !openNewHabit) && (
                <button
                    onClick={onClose}
                    className="mt-4 rounded bg-gray-200 px-4 py-2 text-black dark:bg-zinc-800 dark:text-white"
                >
                    <IoMdClose/>

                </button>
            )}

        </div>

    )
}