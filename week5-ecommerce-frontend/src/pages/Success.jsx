import { Link } from "react-router-dom";

export default function Success() {
    return <section className="empty-state tall"><p className="eyebrow">Order confirmed</p><h1>Thank you. It is on its way.</h1><p>Your demo order has been placed successfully.</p><Link className="primary-button link-button" to="/products">Keep exploring</Link></section>;
}
