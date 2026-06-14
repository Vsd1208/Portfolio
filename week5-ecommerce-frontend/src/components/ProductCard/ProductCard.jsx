import { Link } from "react-router-dom";
import { useStore } from "../../store/StoreContext";
import { money } from "../../utils/storage";
import ProductVisual from "../common/ProductVisual";

export default function ProductCard({ product }) {
    const { dispatch, wishlist, compare } = useStore();
    return (
        <article className="product-card">
            <Link to={`/products/${product.id}`}><ProductVisual product={product} /></Link>
            {product.badge && <span className="product-badge">{product.badge}</span>}
            <button className={`heart ${wishlist.includes(product.id) ? "active" : ""}`} onClick={() => dispatch({ type: "wishlist/toggle", id: product.id })} aria-label="Toggle wishlist">Save</button>
            <div className="product-copy">
                <div><p>{product.category}</p><Link to={`/products/${product.id}`}><h3>{product.name}</h3></Link></div>
                <strong>{money(product.price)}</strong>
            </div>
            <div className="card-actions">
                <button className="primary-button" onClick={() => dispatch({ type: "cart/add", product })}>Add to bag</button>
                <button className={compare.includes(product.id) ? "active secondary-button" : "secondary-button"} onClick={() => dispatch({ type: "compare/toggle", id: product.id })}>Compare</button>
            </div>
        </article>
    );
}
