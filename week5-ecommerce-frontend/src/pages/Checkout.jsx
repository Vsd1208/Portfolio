import { Navigate } from "react-router-dom";
import CheckoutForm from "../components/Checkout/CheckoutForm";
import { useStore } from "../store/StoreContext";
import { money } from "../utils/storage";

export default function Checkout() {
    const { cart, subtotal } = useStore();
    if (!cart.length) return <Navigate to="/cart" replace />;
    return <section className="checkout-page page-section"><CheckoutForm /><aside className="order-summary"><p className="eyebrow">Final review</p><h2>Your order</h2>{cart.map(item => <div key={item.id}><span>{item.quantity} x {item.name}</span><strong>{money(item.quantity * item.price)}</strong></div>)}<div className="summary-total"><span>Total before tax</span><strong>{money(subtotal)}</strong></div></aside></section>;
}
