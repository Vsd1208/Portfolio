import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useStore } from "../../store/StoreContext";

export default function CheckoutForm() {
    const { dispatch } = useStore();
    const navigate = useNavigate();
    const [errors, setErrors] = useState({});

    const submit = event => {
        event.preventDefault();
        const data = Object.fromEntries(new FormData(event.currentTarget));
        const next = {};
        if (data.name.trim().length < 3) next.name = "Enter your full name.";
        if (!/^\S+@\S+\.\S+$/.test(data.email)) next.email = "Enter a valid email.";
        if (data.address.trim().length < 8) next.address = "Enter a complete address.";
        if (!/^\d{16}$/.test(data.card.replace(/\s/g, ""))) next.card = "Enter a 16-digit demo card.";
        setErrors(next);
        if (!Object.keys(next).length) {
            dispatch({ type: "cart/clear" });
            navigate("/success");
        }
    };

    return (
        <form className="checkout-form" onSubmit={submit} noValidate>
            <h2>Delivery details</h2>
            <label>Full name<input name="name" />{errors.name && <span>{errors.name}</span>}</label>
            <label>Email<input name="email" type="email" />{errors.email && <span>{errors.email}</span>}</label>
            <label>Address<textarea name="address" rows="3"></textarea>{errors.address && <span>{errors.address}</span>}</label>
            <h2>Payment</h2>
            <label>Card number<input name="card" inputMode="numeric" placeholder="4242 4242 4242 4242" />{errors.card && <span>{errors.card}</span>}</label>
            <div className="field-row"><label>Expiry<input name="expiry" placeholder="12/29" required /></label><label>CVC<input name="cvc" inputMode="numeric" required /></label></div>
            <button className="primary-button wide" type="submit">Place demo order</button>
        </form>
    );
}
