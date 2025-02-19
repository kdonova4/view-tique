import React from "react";
import { Navigate } from "react-router-dom";
import { useAuth } from "./context/AuthContext";


const ProtectedRoute = ({ children, redirectTo, requireAuth }) => {
  const { token } = useAuth();

  if (requireAuth && !token) {
    return <Navigate to={redirectTo || "/login"} replace />;
  }

  if (!requireAuth && token) {
    return <Navigate to={redirectTo || "/"} replace />;
  }

  return children;
};

export default ProtectedRoute;
