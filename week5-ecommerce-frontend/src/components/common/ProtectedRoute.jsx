import { Navigate, useLocation } from "react-router-dom";
import { useUser } from "../../store/UserContext";

export default function ProtectedRoute({ children }) {
    const { user } = useUser();
    const location = useLocation();
    return user ? children : <Navigate to="/account" replace state={{ from: location.pathname }} />;
}
