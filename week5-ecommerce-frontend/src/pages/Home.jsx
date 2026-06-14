import { Link } from "react-router-dom";
import ProductCard from "../components/ProductCard/ProductCard";

export default function Home({ products }) {
    return (
        <>
            <section className="hero">
                <div className="hero-copy"><p className="eyebrow">Objects for everyday rituals</p><h1>Live with <em>intention.</em></h1><p>Considered tools for calmer homes, clearer desks, and lighter journeys.</p><Link className="primary-button link-button" to="/products">Shop the collection</Link></div>
                <div className="hero-art"><span>F</span><i>&</i><span>F</span></div>
            </section>
            <section className="benefits"><div><strong>Built to last</strong><span>Materials selected for years of use</span></div><div><strong>Designed with care</strong><span>Every detail has a reason</span></div><div><strong>Simple delivery</strong><span>Free shipping over $75</span></div></section>
            <section className="page-section"><div className="section-heading"><div><p className="eyebrow">Most loved</p><h2>Good choices, made easy</h2></div><Link to="/products">View all products</Link></div><div className="product-grid">{products.slice(0, 4).map(product => <ProductCard key={product.id} product={product} />)}</div></section>
            <section className="editorial"><div><p className="eyebrow">Our approach</p><h2>Less, but <em>better.</em></h2></div><p>We choose useful, expressive objects that earn their place in daily life. No endless aisles, no disposable trends.</p></section>
        </>
    );
}
