import { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useUser } from "../store/UserContext";

export default function Account() {
    const { user, login, logout } = useUser();
    const [email, setEmail] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();
    const location = useLocation();
    const submit = event => {
        event.preventDefault();
        if (!/^\S+@\S+\.\S+$/.test(email)) return setError("Enter a valid email address.");
        login(email);
        navigate(location.state?.from || "/");
    };
    if (user) return <section className="account-card"><p className="eyebrow">Your account</p><h1>Welcome, {user.name}.</h1><p>Signed in as {user.email}. Your checkout and saved products are ready.</p><button className="secondary-button" onClick={logout}>Sign out</button></section>;
    return <section className="account-card"><p className="eyebrow">Member access</p><h1>Sign in to continue.</h1><p>This is a simulated authentication flow. Any valid email will work.</p><form onSubmit={submit} noValidate><label>Email address<input type="email" value={email} onChange={event => setEmail(event.target.value)} placeholder="you@example.com" />{error && <span>{error}</span>}</label><button className="primary-button wide">Continue</button></form></section>;
}
