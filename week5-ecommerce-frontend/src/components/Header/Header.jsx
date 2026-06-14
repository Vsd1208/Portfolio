import { useEffect, useState } from "react";
import { Link, NavLink, useNavigate } from "react-router-dom";
import { productApi } from "../../services/api";
import { useStore } from "../../store/StoreContext";
import { useUser } from "../../store/UserContext";

export default function Header() {
    const { cartCount, wishlist } = useStore();
    const { user } = useUser();
    const navigate = useNavigate();
    const [query, setQuery] = useState("");
    const [results, setResults] = useState([]);
    const [menu, setMenu] = useState(false);

    useEffect(() => {
        const timer = setTimeout(async () => setResults(query.length > 1 ? await productApi.search(query) : []), 180);
        return () => clearTimeout(timer);
    }, [query]);

    const submit = event => {
        event.preventDefault();
        navigate(`/products?q=${encodeURIComponent(query)}`);
        setResults([]);
        setMenu(false);
    };

    return (
        <header className="site-header">
            <div className="announcement">Free shipping on thoughtful orders over $75</div>
            <div className="header-inner">
                <button className="menu-button" type="button" onClick={() => setMenu(!menu)} aria-label="Toggle navigation">Menu</button>
                <Link className="logo" to="/">Form <i>&</i> Function</Link>
                <nav className={menu ? "open" : ""}>
                    <NavLink to="/products" onClick={() => setMenu(false)}>Shop</NavLink>
                    <NavLink to="/products?category=Home" onClick={() => setMenu(false)}>Home</NavLink>
                    <NavLink to="/products?category=Workspace" onClick={() => setMenu(false)}>Workspace</NavLink>
                </nav>
                <form className="header-search" onSubmit={submit}>
                    <input value={query} onChange={event => setQuery(event.target.value)} placeholder="Search products" aria-label="Search products" />
                    {results.length > 0 && <div className="search-results">{results.map(product => <Link key={product.id} to={`/products/${product.id}`} onClick={() => { setQuery(""); setResults([]); }}>{product.name}<span>${product.price}</span></Link>)}</div>}
                </form>
                <div className="header-links">
                    <Link to="/account">{user ? user.name : "Sign in"}</Link>
                    <Link to="/products?wishlist=true">Saved <span>{wishlist.length}</span></Link>
                    <Link to="/cart">Bag <span>{cartCount}</span></Link>
                </div>
            </div>
        </header>
    );
}
