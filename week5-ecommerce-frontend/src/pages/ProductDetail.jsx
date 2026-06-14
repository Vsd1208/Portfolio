import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import ProductVisual from "../components/common/ProductVisual";
import { productApi } from "../services/api";
import { useStore } from "../store/StoreContext";
import { money } from "../utils/storage";

export default function ProductDetail() {
    const { id } = useParams();
    const { dispatch, wishlist } = useStore();
    const [product, setProduct] = useState(null);
    useEffect(() => { productApi.getProduct(id).then(setProduct); }, [id]);
    if (!product) return <div className="loading-state">Loading product...</div>;
    return (
        <section className="detail-page">
            <div className="detail-gallery"><ProductVisual product={product} /><div className="gallery-row"><ProductVisual product={product} compact /><ProductVisual product={{ ...product, palette: [...product.palette].reverse() }} compact /></div></div>
            <div className="detail-copy"><Link to="/products">Back to collection</Link><p className="eyebrow">{product.category}</p><h1>{product.name}</h1><div className="rating">Rated {product.rating} / 5 from {product.reviews} reviews</div><strong className="detail-price">{money(product.price)}</strong><p>{product.description}</p><ul>{product.features.map(feature => <li key={feature}>{feature}</li>)}</ul><button className="primary-button wide" onClick={() => dispatch({ type: "cart/add", product })}>Add to bag</button><button className="secondary-button wide" onClick={() => dispatch({ type: "wishlist/toggle", id: product.id })}>{wishlist.includes(product.id) ? "Remove from saved" : "Save for later"}</button></div>
        </section>
    );
}
