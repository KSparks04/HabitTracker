import {startOfWeek, addDays, format, isSameDay, endOfWeek} from 'date-fns';
import { Check, Flame } from 'lucide-react';

import {useEffect, useState} from "react";

export function RecentHabits(){
    const [habits, setHabits]=useState([]);
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
        if(habits.length == 0){
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
                        setHabits(data); // Directly sets habits with their completedDates array!
                    } else {
                        console.error("Failed to load weekly habits");
                    }
                } catch (err) {
                    console.error("Network error:", err);
                }}
            loadHabitsAndLogs();
        }


    },[])




    return (
        <div>
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
                {habits.map(habit => (
                    <div key={habit.id} className="bg-slate-800/80 border border-slate-700/60 rounded-xl p-4 shadow-lg backdrop-blur">
                        <div className="flex justify-between items-center mb-3">
                            <h3 className="font-semibold text-slate-200">{habit.title}</h3>
                            <div className="flex items-center space-x-1 text-amber-400 text-xs font-semibold bg-amber-400/10 px-2.5 py-1 rounded-md">
                                <Flame size={14} className="fill-amber-400" />
                                <span>{habit.streak} day streak</span>
                            </div>
                        </div>


                        <div className="grid grid-cols-7 gap-2 text-center">
                            {weekDays.map(day => {
                                const isCompleted = habit.completedDates.includes(day.isoString);

                                return (
                                    <div key={day.isoString} className="flex flex-col items-center">
                                    <span className={`text-[11px] font-medium mb-1 ${day.isToday ? 'text-indigo-400 font-bold' : 'text-slate-400'}`}>
                        {day.dayName}</span>
                                        <button
                                            onClick={() => toggleDate(habit.id, day.isoString)}
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
                    </div>
                ))}
            </div>


        </div>)
}