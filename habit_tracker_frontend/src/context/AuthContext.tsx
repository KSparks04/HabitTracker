import {createContext, useContext, useEffect, useState} from "react";


const AuthContext= createContext(null);
export const AuthProvider=({children})=>{

    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const token = localStorage.getItem("token");

        if (token && isTokenExpired(token)) {
            localStorage.removeItem("token");
            setUser(null);
        }
        setLoading(false);
    }, []);
    useEffect(() => {
        const storedUser = localStorage.getItem("user");
        const token = localStorage.getItem("token");

        if (storedUser && token && !isTokenExpired(token)) {
            setUser(JSON.parse(storedUser));
        } else {
            localStorage.removeItem("user");
            localStorage.removeItem("token");
            setUser(null);
        }

        setLoading(false);
    }, []);
    const login = (userData,token) => {
        setUser(userData);
        localStorage.setItem("user", JSON.stringify(userData));
        localStorage.setItem("token", token);
    }
    const logout = () => {
        setUser(null);
        localStorage.removeItem("user");
        localStorage.removeItem("token");
    }
    return (
        <AuthContext.Provider value={{user,login,logout,loading}}>
            {!loading && children}
        </AuthContext.Provider>
    );

}
export const useAuth = () => useContext(AuthContext);

export function isTokenExpired(token: string): boolean {
    try {
        const payload = JSON.parse(atob(token.split(".")[1]));

        return payload.exp * 1000 < Date.now();
    } catch {
        return true;
    }
}