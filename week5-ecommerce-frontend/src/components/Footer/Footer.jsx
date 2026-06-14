import { Link } from "react-router-dom";

export default function Footer() {
    return (
        <footer>
            <div><Link className="logo light" to="/">Form <i>&</i> Function</Link><p>Useful objects, considered carefully.</p></div>
            <div><strong>Explore</strong><Link to="/products">All products</Link><Link to="/products?category=Home">Home</Link><Link to="/products?category=Travel">Travel</Link></div>
            <div><strong>Help</strong><a href="mailto:hello@example.com">Contact</a><span>Shipping & returns</span><span>Care guide</span></div>
            <div><strong>Week 5 Project</strong><p>Built with React, Context API, React Router, and Vite.</p></div>
        </footer>
    );
}
