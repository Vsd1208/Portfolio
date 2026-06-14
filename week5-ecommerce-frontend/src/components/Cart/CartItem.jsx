import { useStore } from "../../store/StoreContext";
import { money } from "../../utils/storage";
import ProductVisual from "../common/ProductVisual";

export default function CartItem({ item }) {
    const { dispatch } = useStore();
    return (
        <article className="cart-item">
            <ProductVisual product={item} compact />
            <div className="cart-item-copy"><p>{item.category}</p><h3>{item.name}</h3><strong>{money(item.price)}</strong></div>
            <div className="quantity-control">
                <button onClick={() => dispatch({ type: "cart/quantity", id: item.id, quantity: item.quantity - 1 })}>-</button>
                <span>{item.quantity}</span>
                <button onClick={() => dispatch({ type: "cart/quantity", id: item.id, quantity: item.quantity + 1 })}>+</button>
            </div>
            <strong>{money(item.price * item.quantity)}</strong>
            <button className="text-button" onClick={() => dispatch({ type: "cart/remove", id: item.id })}>Remove</button>
        </article>
    );
}
