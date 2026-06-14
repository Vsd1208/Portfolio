const products = [
    { id: 1, name: "Arc Wireless Headphones", category: "Audio", price: 129, rating: 4.8, reviews: 318, color: "sage", badge: "Bestseller", description: "Immersive sound, active noise cancellation, and a soft-touch build designed for long listening.", features: ["40-hour battery", "Active noise cancellation", "Multipoint Bluetooth"], palette: ["#b9c8b4", "#3e493e"] },
    { id: 2, name: "Contour Desk Lamp", category: "Home", price: 89, rating: 4.7, reviews: 184, color: "clay", badge: "New", description: "A sculptural dimmable lamp that brings warm, focused light to desks and reading corners.", features: ["Touch dimmer", "Warm LED", "Recycled aluminum"], palette: ["#c68f72", "#5c372a"] },
    { id: 3, name: "Field Mechanical Keyboard", category: "Workspace", price: 149, rating: 4.9, reviews: 246, color: "stone", badge: "Top rated", description: "A satisfying low-profile mechanical keyboard built for focused work and quiet shared spaces.", features: ["Hot-swappable keys", "Wireless and USB-C", "Mac and Windows"], palette: ["#d4d0c4", "#4b4943"] },
    { id: 4, name: "Still Ceramic Set", category: "Home", price: 64, rating: 4.6, reviews: 97, color: "cream", badge: "", description: "Four hand-finished ceramic cups with calm proportions and a beautifully tactile glaze.", features: ["Set of four", "Dishwasher safe", "Hand finished"], palette: ["#e5dfcf", "#8b7960"] },
    { id: 5, name: "Roam Daypack", category: "Travel", price: 118, rating: 4.8, reviews: 211, color: "forest", badge: "Bestseller", description: "A weather-resistant daily bag with smart organization and a comfortable, minimal profile.", features: ["20L capacity", "Padded laptop sleeve", "Weather resistant"], palette: ["#6f806d", "#25352a"] },
    { id: 6, name: "Orbit Smart Speaker", category: "Audio", price: 99, rating: 4.5, reviews: 153, color: "sand", badge: "", description: "Room-filling sound and simple controls in a compact speaker made to fit anywhere.", features: ["360-degree sound", "12-hour battery", "Stereo pairing"], palette: ["#d2b991", "#54442e"] },
    { id: 7, name: "Frame Monitor Stand", category: "Workspace", price: 79, rating: 4.7, reviews: 126, color: "walnut", badge: "New", description: "Lift your screen and clear your desk with a precise, solid-wood monitor stand.", features: ["Solid walnut", "Cable management", "Non-slip feet"], palette: ["#9c6c4c", "#3d291e"] },
    { id: 8, name: "Trace Travel Bottle", category: "Travel", price: 38, rating: 4.6, reviews: 289, color: "blue", badge: "", description: "A slim insulated bottle that keeps drinks cold and slips easily into a crowded bag.", features: ["18-hour insulation", "Leakproof lid", "BPA free"], palette: ["#91abb3", "#29444b"] }
];

const delay = value => new Promise(resolve => setTimeout(() => resolve(value), 220));

export const productApi = {
    getProducts: () => delay(products),
    getProduct: id => delay(products.find(product => product.id === Number(id))),
    search: query => delay(products.filter(product => product.name.toLowerCase().includes(query.toLowerCase())).slice(0, 5))
};
