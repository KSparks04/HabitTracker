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
        <div className={"flex "}>
            <div className={"max-w-xs md:max-w-sm w-full m-2"}>
                <h2 className="text-2xl font-bold mb-4">Weekly Habit Breakdown</h2>
                {weeklyData.map((item,index) => {
                    const IconComponent = (Icons as any)[item.iconName] || Icons.HelpCircle;
                    return (
                        <div key={index} className={"flex flex-col items-center justify-start"}>
                            <div className={"flex"}>
                                <h3 className={"text-xl text-left"}>{item.title}</h3>
                                <IconComponent/>
                            </div>
                            <span>{item.streak}/7</span>

                        </div>)
                })}
            </div>
            <div className={"max-w-sm md:max-w-md w-full flex flex-col items-center justify-start"}>
                <h2 className="text-2xl font-bold mb-4">Longest Streak</h2>
                {streakHabits.map((habit,index) => (
                    <div key={index} className={" rounded-full border-primary border-2 p-2 m-1 w-40 h-40 "}>
                        <div className={"flex flex-col items-center justify-start"}>
                            <h2 className={"text-2xl text-text-main"}>Days:</h2>
                            <span className={"text-2xl text-text-main"}>{habit.longestStreak}</span>
                            <h3 className={"text-xl"}>{habit.title}</h3>
                        </div>


                    </div>
                ))}
            </div>


        </div>
    )
}