import React, { createContext, useContext, useEffect, useState } from "react"

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [token, setToken] = useState(localStorage.getItem("token"));
    const [role, setRole] = useState(null);
    const [appUserId, setAppUserId] = useState(null);
  
    useEffect(() => {
      if (token) {
        const decodedToken = JSON.parse(atob(token.split(".")[1]));
        setRole(decodedToken.authorities);
        setAppUserId(decodedToken.appUserId)
        console.log(decodedToken.appUserId)
        if (decodedToken.exp * 1000 < Date.now()) {
          logout();
          alert("Your session has expired. Please log in again.");
        } else {
          localStorage.setItem("token", token);
        }
      } else {
        setRole(null);
        localStorage.removeItem("token");
      }
    }, [token]);
  
    const login = (newToken) => {
      setToken(newToken);
    };
  
    const logout = () => {
      setToken(null);
      setRole(null);
    };
  
    return (
      <AuthContext.Provider value={{ token, role, login, logout }}>
        {children}
      </AuthContext.Provider>
    );
  };
  
  export const useAuth = () => {
    return useContext(AuthContext);
  };
  