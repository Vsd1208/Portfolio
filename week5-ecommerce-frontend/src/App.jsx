import { lazy, Suspense, useEffect, useState } from "react";
import { Route, Routes } from "react-router-dom";
import Footer from "./components/Footer/Footer";
import Header from "./components/Header/Header";
import ProtectedRoute from "./components/common/ProtectedRoute";
import { productApi } from "./services/api";

const Home = lazy(() => import("./pages/Home"));
const ProductList = lazy(() => import("./pages/ProductList"));
const ProductDetail = lazy(() => import("./pages/ProductDetail"));
const CartPage = lazy(() => import("./pages/CartPage"));
const Checkout = lazy(() => import("./pages/Checkout"));
const Account = lazy(() => import("./pages/Account"));
const Success = lazy(() => import("./pages/Success"));

export default function App() {
    const [products, setProducts] = useState([]);
    useEffect(() => { productApi.getProducts().then(setProducts); }, []);
    return (
        <>
            <Header />
            <main>
                <Suspense fallback={<div className="loading-state">Preparing the collection...</div>}>
                    <Routes>
                        <Route path="/" element={<Home products={products} />} />
                        <Route path="/products" element={<ProductList products={products} />} />
                        <Route path="/products/:id" element={<ProductDetail />} />
                        <Route path="/cart" element={<CartPage />} />
                        <Route path="/checkout" element={<ProtectedRoute><Checkout /></ProtectedRoute>} />
                        <Route path="/account" element={<Account />} />
                        <Route path="/success" element={<Success />} />
                        <Route path="*" element={<ProductList products={products} />} />
                    </Routes>
                </Suspense>
            </main>
            <Footer />
        </>
    );
}
