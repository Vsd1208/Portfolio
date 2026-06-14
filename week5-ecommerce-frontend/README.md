# Advanced E-commerce Frontend

## Project Description

Form & Function is a modern React e-commerce frontend featuring a responsive product catalog, product detail pages, shopping cart, wishlist, comparison selection, simulated authentication, and validated checkout. It demonstrates component architecture, shared state, routing, local persistence, lazy loading, code splitting, and offline caching.

## Features

- Responsive product catalog with category, price, search, wishlist, and sorting filters
- Search autocomplete with debounced simulated API calls
- Product detail pages with gallery, features, reviews, and purchase actions
- Interactive cart with quantity validation, totals, tax, shipping, and localStorage persistence
- Wishlist and comparison selection
- Simulated user authentication with protected checkout route
- Responsive checkout form with validation and order confirmation
- Lazy-loaded routes and manually separated vendor bundle
- Service worker for offline app-shell and visited-resource caching

## Technologies

- React 19
- Context API and `useReducer`
- React Router
- Vite
- CSS Grid and Flexbox
- localStorage
- Service Worker and Cache API

## Setup Instructions

1. Open the `week5-ecommerce-frontend` folder.
2. Run `npm install`.
3. Run `npm run dev`.
4. Open the local URL shown by Vite.
5. Run `npm run build` to create the optimized production build.

## Project Structure

```text
week5-ecommerce-frontend/
|-- public/
|   |-- manifest.webmanifest
|   `-- sw.js
|-- src/
|   |-- components/
|   |   |-- Cart/
|   |   |-- Checkout/
|   |   |-- Footer/
|   |   |-- Header/
|   |   |-- ProductCard/
|   |   `-- common/
|   |-- pages/
|   |-- services/
|   |-- store/
|   |-- utils/
|   |-- App.jsx
|   |-- index.jsx
|   `-- styles.css
|-- index.html
|-- package.json
|-- vite.config.js
|-- eslint.config.js
|-- README.md
`-- .gitignore
```

## Component Architecture

```text
App
|-- Header
|   `-- Search autocomplete
|-- Lazy-loaded Routes
|   |-- Home -> ProductCard
|   |-- ProductList -> ProductCard
|   |-- ProductDetail -> ProductVisual
|   |-- CartPage -> CartItem
|   |-- ProtectedRoute -> Checkout -> CheckoutForm
|   |-- Account
|   `-- Success
`-- Footer
```

`StoreProvider` owns cart, wishlist, and comparison state. `UserProvider` owns simulated authentication. Components dispatch actions to the store and automatically receive updated totals and counts. Cart, wishlist, and user state persist to localStorage.

## Technical Details

- A reducer makes cart updates predictable and keeps quantity validation centralized.
- Derived cart count and subtotal values are memoized.
- Product filtering and sorting are memoized to avoid unnecessary recalculation.
- Search calls use a short debounce delay.
- Route components use `React.lazy` and `Suspense` for code splitting.
- Vite separates React dependencies into a reusable vendor chunk.
- Product visuals are lightweight CSS illustrations, avoiding large image downloads.
- The service worker caches the application shell and previously visited resources.

## Testing Evidence

- Added the same product repeatedly and confirmed quantity and totals update
- Decreased quantities and confirmed they never fall below one
- Removed cart items and confirmed localStorage persistence
- Filtered by category and maximum price, then sorted results
- Searched from the header and opened an autocomplete result
- Saved and unsaved products
- Confirmed checkout redirects signed-out users to account login
- Submitted invalid checkout data and confirmed field errors display
- Submitted valid demo checkout data and confirmed cart clears
- Ran ESLint and the optimized Vite production build
- Checked layouts at desktop, tablet, and mobile widths

## Quality Standards Checklist

- [x] Clear project overview and setup instructions
- [x] Component hierarchy and shared state architecture
- [x] Product filtering, sorting, search, wishlist, and comparison
- [x] Interactive persistent shopping cart
- [x] Simulated authentication and protected route
- [x] Validated checkout process
- [x] Responsive CSS Grid and Flexbox layouts
- [x] Lazy loading, code splitting, optimized visuals, and service worker
- [x] Technical details and testing evidence
