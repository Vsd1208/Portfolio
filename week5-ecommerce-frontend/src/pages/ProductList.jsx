import { useMemo, useState } from "react";
import { useSearchParams } from "react-router-dom";
import ProductCard from "../components/ProductCard/ProductCard";
import { useStore } from "../store/StoreContext";

export default function ProductList({ products }) {
    const [params, setParams] = useSearchParams();
    const { wishlist, compare } = useStore();
    const [maxPrice, setMaxPrice] = useState(160);
    const category = params.get("category") || "All";
    const query = params.get("q") || "";
    const wishlistOnly = params.get("wishlist") === "true";
    const sort = params.get("sort") || "featured";

    const filtered = useMemo(() => products
        .filter(product => category === "All" || product.category === category)
        .filter(product => product.price <= maxPrice)
        .filter(product => !wishlistOnly || wishlist.includes(product.id))
        .filter(product => product.name.toLowerCase().includes(query.toLowerCase()))
        .sort((a, b) => sort === "low" ? a.price - b.price : sort === "high" ? b.price - a.price : sort === "rating" ? b.rating - a.rating : a.id - b.id), [products, category, maxPrice, wishlistOnly, wishlist, query, sort]);

    const changeParam = (key, value) => {
        const next = new URLSearchParams(params);
        value === "All" ? next.delete(key) : next.set(key, value);
        setParams(next);
    };

    return (
        <section className="catalog page-section">
            <div className="catalog-header"><div><p className="eyebrow">The collection</p><h1>{wishlistOnly ? "Saved pieces" : "Shop all"}</h1><p>{filtered.length} carefully selected products</p></div><select value={sort} onChange={event => changeParam("sort", event.target.value)}><option value="featured">Featured</option><option value="low">Price: low to high</option><option value="high">Price: high to low</option><option value="rating">Highest rated</option></select></div>
            <div className="catalog-layout">
                <aside><strong>Category</strong>{["All", "Audio", "Home", "Workspace", "Travel"].map(item => <button className={category === item ? "active" : ""} key={item} onClick={() => changeParam("category", item)}>{item}</button>)}<strong>Maximum price: ${maxPrice}</strong><input type="range" min="40" max="160" value={maxPrice} onChange={event => setMaxPrice(Number(event.target.value))} /><p className="compare-note">{compare.length}/3 products selected to compare</p></aside>
                <div>{filtered.length ? <div className="product-grid">{filtered.map(product => <ProductCard key={product.id} product={product} />)}</div> : <div className="empty-state"><h2>No products found</h2><p>Adjust the filters or save a few pieces first.</p></div>}</div>
            </div>
        </section>
    );
}
