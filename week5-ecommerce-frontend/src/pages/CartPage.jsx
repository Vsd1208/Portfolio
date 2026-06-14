import { Link } from "react-router-dom";
import CartItem from "../components/Cart/CartItem";
import { useStore } from "../store/StoreContext";
import { money } from "../utils/storage";

export default function CartPage() {
    const { cart, subtotal } = useStore();
    const shipping = subtotal >= 75 || subtotal === 0 ? 0 : 5.99;
    const tax = subtotal * 0.08;
    if (!cart.length) return <section className="empty-state tall"><p className="eyebrow">Your bag</p><h1>A little room for something good.</h1><p>Your shopping bag is currently empty.</p><Link className="primary-button link-button" to="/products">Browse the collection</Link></section>;
    return (
        <section className="cart-page page-section"><div><p className="eyebrow">Your selection</p><h1>Shopping bag</h1><div className="cart-list">{cart.map(item => <CartItem key={item.id} item={item} />)}</div></div><aside className="order-summary"><h2>Order summary</h2><div><span>Subtotal</span><strong>{money(subtotal)}</strong></div><div><span>Shipping</span><strong>{shipping ? money(shipping) : "Free"}</strong></div><div><span>Estimated tax</span><strong>{money(tax)}</strong></div><div className="summary-total"><span>Total</span><strong>{money(subtotal + shipping + tax)}</strong></div><Link className="primary-button link-button wide" to="/checkout">Proceed to checkout</Link><p>Secure demo checkout. No payment will be processed.</p></aside></section>
    );
}
