import {startOfWeek, addDays, format, isSameDay, endOfWeek} from 'date-fns';
import { Check, Flame } from 'lucide-react';
import Confetti from "react-confetti";
import {useEffect, useState} from "react";
import * as Icons from "lucide-react";
import {CiCircleInfo} from "react-icons/ci";
import {IoMdClose} from "react-icons/io";

export function RecentHabits(){
    const [habits, setHabits]=useState([]);
    const [showConfetti, setShowConfetti] = useState(false);
    // const [showNotes, setShowNotes]=useState(false);
    // const [notesModal, setNotesModal] = useState(false);
    const[selectedHabit,setSelectedHabit]=useState<string | null>(null);
    const today = new Date();
    const startOfCurrentWeek = startOfWeek(today, { weekStartsOn: 0 });
    const weekDays = Array.from({ length: 7 }).map((_, index) => {
        const date = addDays(startOfCurrentWeek, index);
        return {
            date,
            dayName: format(date, 'EEE'), // "Sun", "Mon", "Tue"...
            dayNumber: format(date, 'd'), // "18", "19"...
            isoString: format(date, 'yyyy-MM-dd'),
            isToday: isSameDay(date, today)
        };
    })
    const toggleDate = async(habitId, isoString) => {
        const habit = habits.find(h => h.id === habitId);

        console.log("Before toggle:", habit?.completedDates);
        console.log("Selected date:", isoString);
        console.log(
            "Is selected date completed:",
            habit?.completedDates.includes(isoString)
        );
        setHabits(prev => prev.map(habit => {
            if (habit.id !== habitId) return habit;
            const exists = habit.completedDates.includes(isoString);
            const updated = exists
                ? habit.completedDates.filter(d => d !== isoString)
                : [...habit.completedDates, isoString];
            return { ...habit, completedDates: updated };
        }));
        const token = localStorage.getItem('token');

        try {
            const response = await fetch(`/api/habits/logs/${habitId}/toggle?date=${isoString}`, {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            if (!response.ok) {
                console.error("Failed to sync toggle with backend");
                // Optional: Revert UI state here if the request failed
            }
        } catch (err) {
            console.error("Network error during toggle:", err);
        }
    };
    useEffect(()=>{

            const loadHabitsAndLogs = async ()=>{
                const token = localStorage.getItem('token');
                const sunday = startOfWeek(today, { weekStartsOn: 0 });
                const saturday = endOfWeek(today, { weekStartsOn: 0 });


                const startDate = format(sunday, "yyyy-MM-dd'T'00:00:00");
                const endDate = format(saturday, "yyyy-MM-dd'T'23:59:59");
                try {
                    const response = await fetch(`/api/habits/weekly?startDate=${startDate}&endDate=${endDate}`, {
                        method: "GET",
                        headers: { Authorization: `Bearer ${token}` }
                    });

                    if (response.ok) {
                        const data = await response.json();
                        console.log(data);
                        setHabits(data); // Directly sets habits with their completedDates array!
                    } else {
                        console.error("Failed to load weekly habits");
                    }
                } catch (err) {
                    console.error("Network error:", err);
                }}
            loadHabitsAndLogs();



    },[])
    useEffect(() => {
        let timer;
        if (showConfetti) {
            timer = setTimeout(() => setShowConfetti(false), 5000); // 3 seconds
        }
        return () => clearTimeout(timer);
    }, [showConfetti]);




    return (
        <div>
            {showConfetti && (
                <Confetti
                    width={window.innerWidth}
                    height={window.innerHeight}
                    numberOfPieces={200}
                    recycle={false} // stop after animation
                />
            )}
            <div className="flex justify-between items-center mb-6">
                <div>
                    <h2 className="text-xl font-bold text-white">Weekly Habits</h2>
                    <p className="text-sm text-slate-400">
                        Week of {format(startOfCurrentWeek, 'MMM d, yyyy')}
                    </p>
                </div>
                <span className="text-xs bg-indigo-500/10 text-indigo-400 border border-indigo-500/20 px-3 py-1.5 rounded-full font-medium">
              Sun - Sat View
            </span>
            </div>
            <div className={"overflow-scroll overflow-x-hidden scrollbar-thin"}>
                {habits.map(habit => {
                    const IconComponent = (Icons as any)[habit.iconName] || Icons.HelpCircle;
                    return (
                    <div key={habit.id} className="bg-slate-800/80 border border-slate-700/60 rounded-xl p-4 shadow-lg backdrop-blur">
                        <div className="flex justify-between items-center mb-3">
                            <h3 className="font-semibold text-slate-200">{habit.title}</h3>
                            <div className="flex items-center space-x-1 text-amber-400 text-xs font-semibold bg-amber-400/10 px-2.5 py-1 rounded-md">
                                <Flame size={14} className="fill-amber-400" />
                                <span>{habit.streak} day streak</span>
                            </div>
                        </div>


                        <div className="grid grid-cols-7 gap-2 text-center">
                            {selectedHabit == habit.id && <div className={"fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm"}>
                                <div className={"w-full max-w-sm rounded-2xl bg-white p-6 shadow-2xl dark:bg-zinc-900 "}>
                                    <h3>Add Notes</h3>
                                    <div>
                                        <IconComponent/>
                                        <p>{habit.title}</p>
                                    </div>


                                    <form>
                                        <input type={"hidden"} value={habit.id}/>
                                        <div className={"flex"}>
                                            <p>Notes</p>
                                            <CiCircleInfo />
                                        </div>
                                        <input type={"textarea"} />
                                    </form>
                                    <button
                                        onClick={() => setSelectedHabit(null)}
                                        className="mt-4 rounded bg-gray-200 px-4 py-2 text-black dark:bg-zinc-800 dark:text-white"
                                    >
                                        <IoMdClose/>

                                    </button>

                                </div>
                            </div>}
                            {weekDays.map(day => {
                                const isCompleted = habit.completedDates.includes(day.isoString);

                                return (
                                    <div key={`${habit.id}-${day.isoString}`} className="flex flex-col items-center">
                                    <span className={`text-[11px] font-medium mb-1 ${day.isToday ? 'text-indigo-400 font-bold' : 'text-slate-400'}`}>
                        {day.dayName}</span>
                                        <button
                                            onClick={() => {toggleDate(habit.id, day.isoString); if(!isCompleted){setShowConfetti(true);setSelectedHabit(habit.id);}}}
                                            className={`w-10 h-10 rounded-lg flex items-center justify-center transition-all duration-200 border ${
                                                isCompleted
                                                    ? 'bg-emerald-500 text-white border-emerald-400 shadow-md shadow-emerald-500/20 scale-100'
                                                    : day.isToday
                                                        ? 'bg-slate-700/50 text-slate-300 border-indigo-500/50 hover:border-indigo-400'
                                                        : 'bg-slate-900/60 text-slate-500 border-slate-700/40 hover:border-slate-600'
                                            }`}
                                            title={`${day.dayName}, ${day.isoString}`}
                                        >
                                            {isCompleted ? (
                                                <Check size={20} strokeWidth={2.5} />
                                            ) : (
                                                <span className="text-xs">{day.dayNumber}</span>
                                            )}
                                        </button>

                                    </div>
                                );
                            })}

                        </div>

                        <button className={"max-w-xs w-full"} onClick={()=>setSelectedHabit(habit.id)}>Add Notes</button>
                    </div>)
                })}


            </div>


        </div>)
}