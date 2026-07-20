# ERP System for Inventory and Sales Management

Full-stack ERP project built from the PDF brief using Java 17, Spring Boot, JWT security, JPA persistence, and React 18.

## Modules

- JWT login/register with role-based access
- Products with SKU, category, unit price, stock, and reorder level
- Customer and supplier management
- Sales orders with auto-calculated totals and stock deduction
- Purchase orders with status tracking
- GRNs that increment product stock on receipt
- Invoices generated from approved/dispatched sales orders
- Dashboard reports for monthly sales, purchases, low stock, pending invoices, and top products
- Swagger UI API documentation

## Demo Users

All seeded users use password `password`.

| Username | Role |
| --- | --- |
| `admin` | Admin |
| `sales` | Sales Executive |
| `purchase` | Purchase Manager |
| `inventory` | Inventory Manager |
| `accounts` | Accountant |

## Run Locally

Backend:

```bash
cd backend
mvn spring-boot:run
```

Frontend:

```bash
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173`.

Swagger UI is available at `http://localhost:8081/swagger-ui/index.html`.

The local backend defaults to an in-memory H2 database for quick demos. Docker Compose runs PostgreSQL.

## Docker

```bash
docker compose up --build
```

Frontend: `http://localhost:5173`

Backend: `http://localhost:8081`

## API Summary

- `POST /api/auth/login`
- `POST /api/auth/register`
- `GET|POST /api/products`
- `PUT|DELETE /api/products/{id}`
- `GET|POST /api/customers`
- `GET|POST /api/suppliers`
- `GET|POST /api/sales-orders`
- `PUT /api/sales-orders/{id}/status`
- `GET|POST /api/purchase-orders`
- `PUT /api/purchase-orders/{id}/status`
- `GET|POST /api/grns`
- `GET|POST /api/invoices`
- `GET /api/invoices/{id}/pdf`
- `GET /api/dashboard/sales-summary`
- `GET /api/dashboard/purchase-summary`
- `GET /api/dashboard/stock-alerts`
- `GET /api/dashboard/top-products`

## Notes

- Sales orders deduct stock immediately to keep inventory available quantities current.
- GRNs add received quantities back into stock and mark linked purchase orders as received.
- Invoice creation requires a sales order in `APPROVED` or `DISPATCHED` status.
- The invoice PDF endpoint returns a lightweight generated document response for download.
