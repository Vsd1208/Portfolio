import { createContext, useContext, useEffect, useMemo, useReducer } from "react";
import { storage } from "../utils/storage";

const StoreContext = createContext(null);
const initialState = {
    cart: storage.read("week5.cart", []),
    wishlist: storage.read("week5.wishlist", []),
    compare: []
};

function reducer(state, action) {
    switch (action.type) {
        case "cart/add": {
            const found = state.cart.find(item => item.id === action.product.id);
            return { ...state, cart: found ? state.cart.map(item => item.id === action.product.id ? { ...item, quantity: item.quantity + 1 } : item) : [...state.cart, { ...action.product, quantity: 1 }] };
        }
        case "cart/quantity":
            return { ...state, cart: state.cart.map(item => item.id === action.id ? { ...item, quantity: Math.max(1, action.quantity) } : item) };
        case "cart/remove":
            return { ...state, cart: state.cart.filter(item => item.id !== action.id) };
        case "cart/clear":
            return { ...state, cart: [] };
        case "wishlist/toggle":
            return { ...state, wishlist: state.wishlist.includes(action.id) ? state.wishlist.filter(id => id !== action.id) : [...state.wishlist, action.id] };
        case "compare/toggle":
            return { ...state, compare: state.compare.includes(action.id) ? state.compare.filter(id => id !== action.id) : state.compare.length < 3 ? [...state.compare, action.id] : state.compare };
        default:
            return state;
    }
}

export function StoreProvider({ children }) {
    const [state, dispatch] = useReducer(reducer, initialState);
    useEffect(() => storage.write("week5.cart", state.cart), [state.cart]);
    useEffect(() => storage.write("week5.wishlist", state.wishlist), [state.wishlist]);

    const value = useMemo(() => ({
        ...state,
        dispatch,
        cartCount: state.cart.reduce((sum, item) => sum + item.quantity, 0),
        subtotal: state.cart.reduce((sum, item) => sum + item.price * item.quantity, 0)
    }), [state]);

    return <StoreContext.Provider value={value}>{children}</StoreContext.Provider>;
}

export const useStore = () => useContext(StoreContext);
