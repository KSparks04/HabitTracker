import {useEffect, useState} from "react";
import * as Icons from 'lucide-react';
import {IoAddOutline} from "react-icons/io5";
import {IoMdClose} from "react-icons/io";
import {HabitCard} from "./HabitCard.tsx";
import "../css/HabitSelector.css";

interface HabitSelectorProps {
    onClose?: () => void,

    onHabitCreated?: (newHabit) => void
}

export function HabitSelector({onClose, onHabitCreated}: HabitSelectorProps) {

    const [habitTemplates, setHabitTemplates] = useState([]);

    const [selectedTemplate, setSelectedTemplate] = useState<string | null>(null);
    const [openNewHabit, setOpenNewHabit] = useState(false);
    useEffect(() => {
        async function fetchTemplates() {
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
            }
        }

        fetchTemplates();
    }, [])

    return (
        <div className={"select-div shadow-lg w-full max-w-60 md:max-w-80 flex flex-col gap-1 rounded-xl"}>
            <h2 className={"text-2xl"}>Add a Habit</h2>
            <div className={"overflow-scroll overflow-x-hidden"}>
                {habitTemplates.map((template) => {
                    const IconComponent = (Icons as any)[template.iconName] || Icons.HelpCircle;
                    return (
                        <div key={template.id}>
                            {selectedTemplate !== template.id ? (
                                <div
                                    className="template-div m-2 transition-all duration-200 ease-in-out flex max-w-40 md:max-w-70 w-full  hover:bg-gray-300 rounded-xl items-center"
                                    onClick={() => {
                                        setSelectedTemplate(template.id);
                                        setOpenNewHabit(false);
                                    }}>
                                    <div
                                        className={"icon m-2 p-2 rounded-full flex shrink-0 items-center justify-center"}>
                                        <IconComponent className=" w-7 h-7 text-blue-600"/>
                                    </div>

                                    <p className={"habit-name text-xl"}>{template.title}</p>
                                </div>) : (
                                <div
                                    className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm">


                                    <div
                                        className="w-full max-w-md rounded-2xl bg-white p-6 shadow-2xl dark:bg-zinc-900 ">
                                        <HabitCard onHabitCreated ={onHabitCreated} name={template.title} iconName={template.iconName}
                                                   frequencySet={template.default_frequency} color={""}/>

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
            </div>

            {!openNewHabit ? (
                <div className="m-2 flex max-w-40 md:max-w-70 w-full  hover:bg-gray-300 rounded-xl items-center"
                     onClick={() => {
                         setOpenNewHabit(true);
                         setSelectedTemplate(null);
                     }}>
                    <div className={"icon m-2 p-2 rounded-full flex shrink-0 items-center justify-center"}>
                        <IoAddOutline className={"w-7 h-7"} color={"black"}/>
                    </div>

                    <p className={"habit-name text-xl"}>Add a new habit</p>

                </div>) : ((
                    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm">


                        <div className="w-full max-w-md rounded-2xl bg-white p-6 shadow-2xl dark:bg-zinc-900">
                            <HabitCard onHabitCreated ={onHabitCreated} name={""} iconName={""} frequencySet={""} color={""}/>

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
                <div className={"flex items-center justify-center m-2"}>
                    <button
                        onClick={onClose}
                        className="flex items-center justify-center  max-w-10 animate-in fade-in zoom-in-95 duration-200 mt-4 rounded bg-gray-200 p-2.5 text-black dark:bg-zinc-800 dark:text-white"
                    >
                        <IoMdClose/>

                    </button>
                </div>

            )}

        </div>

    )
}