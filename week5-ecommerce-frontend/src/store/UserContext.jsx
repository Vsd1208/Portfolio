import { createContext, useContext, useMemo, useState } from "react";
import { storage } from "../utils/storage";

const UserContext = createContext(null);

export function UserProvider({ children }) {
    const [user, setUser] = useState(() => storage.read("week5.user", null));

    const value = useMemo(() => ({
        user,
        login(email) {
            const next = { email, name: email.split("@")[0].replace(/[._-]/g, " ") };
            storage.write("week5.user", next);
            setUser(next);
        },
        logout() {
            localStorage.removeItem("week5.user");
            setUser(null);
        }
    }), [user]);

    return <UserContext.Provider value={value}>{children}</UserContext.Provider>;
}

export const useUser = () => useContext(UserContext);
