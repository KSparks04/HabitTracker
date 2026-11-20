import {useEffect, useState} from "react";
import { endOfWeek, format, startOfWeek} from "date-fns";

import * as Icons from "lucide-react";


export function StatsPage(){
    const[weeklyData, setWeeklyData] = useState([]);
    const[streakHabits,setStreakHabits] = useState([]);

    const today = new Date();
    const [loading, setLoading] = useState(true);

    useEffect(()=>{
        const loadHabitsAndLogs = async ()=>{
            const token = localStorage.getItem('token');
            const sunday = startOfWeek(today, { weekStartsOn: 0 });
            const saturday = endOfWeek(today, { weekStartsOn: 0 });


            const startDate = format(sunday, "yyyy-MM-dd'T'00:00:00");
            const endDate = format(saturday, "yyyy-MM-dd'T'23:59:59");
            try {
                const response = await fetch(`/api/habits/week/completed?startDate=${startDate}&endDate=${endDate}`, {
                    method: "GET",
                    headers: { Authorization: `Bearer ${token}` }
                });

                if (response.ok) {
                    const data = await response.json();
                    console.log(data);
                    // const habitArray = data.map(item => ({
                    //     name: item.title,
                    //     value: item.streak
                    // }));

                    setWeeklyData(data);
                } else {
                    console.error("Failed to load weekly habits");
                }
            } catch (err) {
                console.error("Network error:", err);
            }}
        const loadLongestStreaks = async()=>{
            const token = localStorage.getItem('token');
            try{
                const response = await fetch(`/api/habit/logs/longest-streak`, {
                    method: "GET",
                    headers: { Authorization: `Bearer ${token}` }
                });

                if (response.ok) {
                    const data = await response.json();
                    console.log(data);


                    setStreakHabits(data);
                } else {
                    console.error("Failed to load streaks");
                }
            }catch (err) {
                console.error("Network error:", err);
            }}

        loadHabitsAndLogs();
        loadLongestStreaks();
        setLoading(false);

    },[])

// const changeToPercentage=(streak)=>{
//     const percent = Math.round((streak/7) *100);
//     return percent ;
// }
    if (loading) return <div className="text-center p-8 text-slate-400">Loading stats...</div>;
    return(
        <div className={"flex transition-all duration-300 ease-in-out "}>
            <div className={"transition-transform duration-200 ease-in hover:scale-105 shadow-md rounded-xl max-w-xs md:max-w-sm w-full m-3 bg-surface hover:bg-accent "}>
                <h2 className="text-2xl font-bold m-4 mb-4 text-black">Weekly Habit Breakdown</h2>
                <div className={"grid grid-cols-2 gap-2 p-2"}>
                    {weeklyData.map((item,index) => {
                        const IconComponent = (Icons as any)[item.iconName] || Icons.HelpCircle;
                        return (
                            <div key={index} className={"flex bg-off-purple shadow-sm w-45 h-20 flex-col items-center justify-start p-1 rounded-lg"}>
                                <div className={"flex items-center justify-center gap-1"}>
                                    <h3 className={"text-xl text-left text-black font-medium"}>{item.title}</h3>
                                    <IconComponent className=" w-5 h-5 text-text-main"/>
                                </div>
                                <span className={"text-black text-lg font-medium"}>{item.streak}/7</span>

                            </div>)
                    })}
                </div>

            </div>
            <div className={"max-w-md md:max-w-lg w-full flex flex-col items-center justify-start transition-transform duration-200 ease-in hover:scale-105 shadow-md rounded-xl bg-surface p-2 m-3 hover:bg-accent"}>
                <h2 className="text-2xl font-bold mb-4 text-black">Longest Streak</h2>
                <div className={"grid grid-cols-3 "}>
                    {streakHabits.map((habit,index) => (
                        <div key={index} className={" rounded-full border-lilac border-2 p-2 m-1 w-40 h-40 shadow-sm "}>
                            <div className={"flex flex-col items-center justify-start"}>
                                <h2 className={"text-2xl text-black font-semibold"}>Days:</h2>
                                <span className={"text-2xl text-black"}>{habit.longestStreak}</span>
                                <h3 className={"text-xl text-black"}>{habit.title}</h3>
                            </div>


                        </div>
                    ))}
                </div>

            </div>


        </div>
    )
}