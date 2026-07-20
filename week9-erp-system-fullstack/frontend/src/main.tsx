import React, { useEffect, useMemo, useState } from 'react';
import { createRoot } from 'react-dom/client';
import { api, currentUser, login, logout, register } from './services/api';
import type { DashboardSummary, Invoice, Partner, Product, PurchaseOrder, Role, SalesOrder } from './types';
import './styles.css';

type Page = 'dashboard' | 'products' | 'customers' | 'suppliers' | 'sales' | 'purchases' | 'grns' | 'invoices' | 'reports';

const rolePages: Record<Role, Page[]> = {
  ADMIN: ['dashboard', 'products', 'customers', 'suppliers', 'sales', 'purchases', 'grns', 'invoices', 'reports'],
  SALES_EXECUTIVE: ['customers', 'sales', 'invoices'],
  PURCHASE_MANAGER: ['suppliers', 'products', 'purchases', 'grns'],
  INVENTORY_MANAGER: ['dashboard', 'products', 'suppliers', 'purchases', 'grns', 'reports'],
  ACCOUNTANT: ['dashboard', 'customers', 'sales', 'invoices', 'reports']
};

const pageLabels: Record<Page, string> = {
  dashboard: 'Dashboard',
  products: 'Products',
  customers: 'Customers',
  suppliers: 'Suppliers',
  sales: 'Sales Orders',
  purchases: 'Purchase Orders',
  grns: 'GRNs',
  invoices: 'Invoices',
  reports: 'Reports'
};

function App() {
  const [user, setUser] = useState(currentUser());
  const [page, setPage] = useState<Page>('dashboard');

  if (!user) return <AuthScreen onAuthed={() => setUser(currentUser())} />;

  const pages = rolePages[user.role];
  const activePage = pages.includes(page) ? page : pages[0];

  return (
    <div className="app">
      <aside className="sidebar">
        <div>
          <h1>ERP</h1>
          <p>{user.username} · {labelRole(user.role)}</p>
        </div>
        <nav>
          {pages.map((item) => (
            <button key={item} className={activePage === item ? 'active' : ''} onClick={() => setPage(item)}>{pageLabels[item]}</button>
          ))}
        </nav>
        <button className="ghost" onClick={() => { logout(); setUser(null); }}>Logout</button>
      </aside>
      <main>
        {activePage === 'dashboard' && <Dashboard />}
        {activePage === 'products' && <Products />}
        {activePage === 'customers' && <Partners kind="customers" />}
        {activePage === 'suppliers' && <Partners kind="suppliers" />}
        {activePage === 'sales' && <SalesOrders />}
        {activePage === 'purchases' && <PurchaseOrders />}
        {activePage === 'grns' && <Grns />}
        {activePage === 'invoices' && <Invoices />}
        {activePage === 'reports' && <Reports />}
      </main>
    </div>
  );
}

function AuthScreen({ onAuthed }: { onAuthed: () => void }) {
  const [mode, setMode] = useState<'login' | 'register'>('login');
  const [form, setForm] = useState({ username: 'admin', email: 'admin@erp.local', password: 'password', role: 'ADMIN' as Role });
  const [error, setError] = useState('');

  async function submit(event: React.FormEvent) {
    event.preventDefault();
    setError('');
    try {
      if (mode === 'login') await login(form.username, form.password);
      else await register(form.username, form.email, form.password, form.role);
      onAuthed();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Authentication failed');
    }
  }

  return (
    <div className="auth">
      <form className="panel auth-panel" onSubmit={submit}>
        <h1>ERP Operations</h1>
        <div className="segmented">
          <button type="button" className={mode === 'login' ? 'active' : ''} onClick={() => setMode('login')}>Login</button>
          <button type="button" className={mode === 'register' ? 'active' : ''} onClick={() => setMode('register')}>Register</button>
        </div>
        <input placeholder="Username" value={form.username} onChange={(e) => setForm({ ...form, username: e.target.value })} />
        {mode === 'register' && <input placeholder="Email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />}
        <input placeholder="Password" type="password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} />
        {mode === 'register' && <select value={form.role} onChange={(e) => setForm({ ...form, role: e.target.value as Role })}>{Object.keys(rolePages).map((role) => <option key={role}>{role}</option>)}</select>}
        {error && <p className="error">{error}</p>}
        <button className="primary">{mode === 'login' ? 'Login' : 'Create Account'}</button>
        <p className="hint">Demo users: admin, sales, purchase, inventory, accounts. Password: password.</p>
      </form>
    </div>
  );
}

function Dashboard() {
  const [summary, setSummary] = useState<DashboardSummary | null>(null);
  const [alerts, setAlerts] = useState<Product[]>([]);
  useEffect(() => {
    api.get('/api/dashboard/sales-summary').then((r) => setSummary(r.data));
    api.get('/api/dashboard/stock-alerts').then((r) => setAlerts(r.data));
  }, []);
  return <section><Header title="Dashboard" /><div className="metrics">
    <Metric label="Sales This Month" value={money(summary?.totalSalesThisMonth)} />
    <Metric label="Purchases This Month" value={money(summary?.totalPurchasesThisMonth)} />
    <Metric label="Pending Invoices" value={summary?.pendingInvoices ?? 0} />
    <Metric label="Pending Sales Orders" value={summary?.pendingSalesOrders ?? 0} />
  </div><Table title="Low Stock Alerts" rows={alerts} columns={['name', 'sku', 'currentStock', 'reorderLevel']} /></section>;
}

function Products() {
  const empty = { name: '', sku: '', category: '', unitPrice: 0, currentStock: 0, reorderLevel: 0 };
  const [rows, setRows] = useState<Product[]>([]);
  const [form, setForm] = useState<Product>(empty);
  const load = () => api.get('/api/products').then((r) => setRows(r.data));
  useEffect(() => { load(); }, []);
  async function save(event: React.FormEvent) {
    event.preventDefault();
    await api.post('/api/products', form);
    setForm(empty);
    load();
  }
  return <section><Header title="Products" /><FormGrid onSubmit={save}>
    <Input label="Name" value={form.name} onChange={(v) => setForm({ ...form, name: v })} />
    <Input label="SKU" value={form.sku} onChange={(v) => setForm({ ...form, sku: v })} />
    <Input label="Category" value={form.category} onChange={(v) => setForm({ ...form, category: v })} />
    <Input label="Unit Price" type="number" value={form.unitPrice} onChange={(v) => setForm({ ...form, unitPrice: Number(v) })} />
    <Input label="Stock" type="number" value={form.currentStock} onChange={(v) => setForm({ ...form, currentStock: Number(v) })} />
    <Input label="Reorder Level" type="number" value={form.reorderLevel} onChange={(v) => setForm({ ...form, reorderLevel: Number(v) })} />
  </FormGrid><Table title="Product Catalog" rows={rows} columns={['name', 'sku', 'category', 'unitPrice', 'currentStock', 'reorderLevel']} /></section>;
}

function Partners({ kind }: { kind: 'customers' | 'suppliers' }) {
  const empty = { name: '', email: '', phone: '', address: '', gstin: '' };
  const [rows, setRows] = useState<Partner[]>([]);
  const [form, setForm] = useState<Partner>(empty);
  const load = () => api.get(`/api/${kind}`).then((r) => setRows(r.data));
  useEffect(() => { load(); }, [kind]);
  async function save(event: React.FormEvent) {
    event.preventDefault();
    await api.post(`/api/${kind}`, form);
    setForm(empty);
    load();
  }
  return <section><Header title={kind === 'customers' ? 'Customers' : 'Suppliers'} /><FormGrid onSubmit={save}>
    <Input label="Name" value={form.name} onChange={(v) => setForm({ ...form, name: v })} />
    <Input label="Email" value={form.email} onChange={(v) => setForm({ ...form, email: v })} />
    <Input label="Phone" value={form.phone} onChange={(v) => setForm({ ...form, phone: v })} />
    <Input label="Address" value={form.address} onChange={(v) => setForm({ ...form, address: v })} />
    <Input label="GSTIN" value={form.gstin || ''} onChange={(v) => setForm({ ...form, gstin: v })} />
  </FormGrid><Table title="Directory" rows={rows} columns={['name', 'email', 'phone', 'address', 'gstin']} /></section>;
}

function SalesOrders() {
  const [orders, setOrders] = useState<SalesOrder[]>([]);
  const [customers, setCustomers] = useState<Partner[]>([]);
  const [products, setProducts] = useState<Product[]>([]);
  const [form, setForm] = useState({ customerId: 0, productId: 0, quantity: 1 });
  const load = () => Promise.all([api.get('/api/sales-orders').then((r) => setOrders(r.data)), api.get('/api/customers').then((r) => setCustomers(r.data)), api.get('/api/products').then((r) => setProducts(r.data))]);
  useEffect(() => { void load(); }, []);
  async function save(event: React.FormEvent) {
    event.preventDefault();
    await api.post('/api/sales-orders', { customerId: form.customerId, orderDate: today(), items: [{ productId: form.productId, quantity: form.quantity }] });
    load();
  }
  return <section><Header title="Sales Orders" /><OrderForm onSubmit={save} partnerLabel="Customer" partners={customers} products={products} form={form} setForm={setForm} />
    <OrderTable rows={orders} statusOptions={['PENDING', 'APPROVED', 'DISPATCHED']} endpoint="/api/sales-orders" reload={load} columns={['id', 'customer.name', 'orderDate', 'status', 'totalAmount']} /></section>;
}

function PurchaseOrders() {
  const [orders, setOrders] = useState<PurchaseOrder[]>([]);
  const [suppliers, setSuppliers] = useState<Partner[]>([]);
  const [products, setProducts] = useState<Product[]>([]);
  const [form, setForm] = useState({ supplierId: 0, productId: 0, quantity: 1 });
  const load = () => Promise.all([api.get('/api/purchase-orders').then((r) => setOrders(r.data)), api.get('/api/suppliers').then((r) => setSuppliers(r.data)), api.get('/api/products').then((r) => setProducts(r.data))]);
  useEffect(() => { load(); }, []);
  async function save(event: React.FormEvent) {
    event.preventDefault();
    await api.post('/api/purchase-orders', { supplierId: form.supplierId, expectedDeliveryDate: today(), items: [{ productId: form.productId, quantity: form.quantity }] });
    load();
  }
  return <section><Header title="Purchase Orders" /><OrderForm onSubmit={save} partnerLabel="Supplier" partners={suppliers} products={products} form={form} setForm={setForm} />
    <OrderTable rows={orders} statusOptions={['ORDERED', 'RECEIVED']} endpoint="/api/purchase-orders" reload={load} columns={['id', 'supplier.name', 'expectedDeliveryDate', 'status', 'totalAmount']} /></section>;
}

function Grns() {
  const [rows, setRows] = useState<any[]>([]);
  const [suppliers, setSuppliers] = useState<Partner[]>([]);
  const [products, setProducts] = useState<Product[]>([]);
  const [form, setForm] = useState({ supplierId: 0, productId: 0, quantity: 1 });
  const load = () => Promise.all([api.get('/api/grns').then((r) => setRows(r.data)), api.get('/api/suppliers').then((r) => setSuppliers(r.data)), api.get('/api/products').then((r) => setProducts(r.data))]);
  useEffect(() => { load(); }, []);
  async function save(event: React.FormEvent) {
    event.preventDefault();
    await api.post('/api/grns', { supplierId: form.supplierId, receivedDate: today(), items: [{ productId: form.productId, quantity: form.quantity }] });
    load();
  }
  return <section><Header title="Goods Receipt Notes" /><OrderForm onSubmit={save} partnerLabel="Supplier" partners={suppliers} products={products} form={form} setForm={setForm} submitLabel="Receive Goods" />
    <Table title="GRN Register" rows={rows} columns={['id', 'supplier.name', 'receivedDate']} /></section>;
}

function Invoices() {
  const [invoices, setInvoices] = useState<Invoice[]>([]);
  const [orders, setOrders] = useState<SalesOrder[]>([]);
  const [form, setForm] = useState({ salesOrderId: 0, tax: 0, status: 'UNPAID' });
  const load = () => Promise.all([api.get('/api/invoices').then((r) => setInvoices(r.data)), api.get('/api/sales-orders').then((r) => setOrders(r.data))]);
  useEffect(() => { load(); }, []);
  async function save(event: React.FormEvent) {
    event.preventDefault();
    await api.post('/api/invoices', form);
    load();
  }
  return <section><Header title="Invoices" /><form className="panel form-grid" onSubmit={save}>
    <label>Approved Sales Order<select value={form.salesOrderId} onChange={(e) => setForm({ ...form, salesOrderId: Number(e.target.value) })}><option value={0}>Select</option>{orders.filter((o) => o.status === 'APPROVED' || o.status === 'DISPATCHED').map((order) => <option key={order.id} value={order.id}>#{order.id} {order.customer.name}</option>)}</select></label>
    <Input label="Tax" type="number" value={form.tax} onChange={(v) => setForm({ ...form, tax: Number(v) })} />
    <label>Status<select value={form.status} onChange={(e) => setForm({ ...form, status: e.target.value })}><option>UNPAID</option><option>PAID</option></select></label>
    <button className="primary">Generate Invoice</button>
  </form><Table title="Invoice List" rows={invoices} columns={['id', 'customer.name', 'salesOrder.id', 'tax', 'totalPayable', 'status', 'invoiceDate']} action={(row) => <a className="link-button" href={`/api/invoices/${row.id}/pdf`} target="_blank">PDF</a>} /></section>;
}

function Reports() {
  const [top, setTop] = useState<{ productName: string; quantitySold: number }[]>([]);
  useEffect(() => { api.get('/api/dashboard/top-products').then((r) => setTop(r.data)); }, []);
  const max = Math.max(1, ...top.map((item) => item.quantitySold));
  return <section><Header title="Reports" /><div className="panel"><h2>Top-Selling Products</h2>{top.length === 0 && <p className="hint">Create sales orders to populate this report.</p>}{top.map((item) => <div className="bar-row" key={item.productName}><span>{item.productName}</span><div><i style={{ width: `${(item.quantitySold / max) * 100}%` }} /></div><b>{item.quantitySold}</b></div>)}</div></section>;
}

function Header({ title }: { title: string }) {
  return <header className="page-header"><h1>{title}</h1><span>Inventory and Sales Management</span></header>;
}

function Metric({ label, value }: { label: string; value: React.ReactNode }) {
  return <div className="metric"><span>{label}</span><strong>{value}</strong></div>;
}

function FormGrid({ children, onSubmit }: { children: React.ReactNode; onSubmit: (event: React.FormEvent) => void }) {
  return <form className="panel form-grid" onSubmit={onSubmit}>{children}<button className="primary">Save</button></form>;
}

function Input({ label, value, onChange, type = 'text' }: { label: string; value: string | number; type?: string; onChange: (value: string) => void }) {
  return <label>{label}<input required type={type} value={value} onChange={(e) => onChange(e.target.value)} /></label>;
}

function OrderForm({ onSubmit, partnerLabel, partners, products, form, setForm, submitLabel = 'Create Order' }: any) {
  useEffect(() => {
    if (!form[`${partnerLabel.toLowerCase()}Id`] && partners[0]) setForm((f: any) => ({ ...f, [`${partnerLabel.toLowerCase()}Id`]: partners[0].id }));
    if (!form.productId && products[0]) setForm((f: any) => ({ ...f, productId: products[0].id }));
  }, [partners, products]);
  const key = `${partnerLabel.toLowerCase()}Id`;
  return <form className="panel form-grid" onSubmit={onSubmit}>
    <label>{partnerLabel}<select value={form[key] || 0} onChange={(e) => setForm({ ...form, [key]: Number(e.target.value) })}>{partners.map((p: Partner) => <option key={p.id} value={p.id}>{p.name}</option>)}</select></label>
    <label>Product<select value={form.productId || 0} onChange={(e) => setForm({ ...form, productId: Number(e.target.value) })}>{products.map((p: Product) => <option key={p.id} value={p.id}>{p.name}</option>)}</select></label>
    <Input label="Quantity" type="number" value={form.quantity} onChange={(v) => setForm({ ...form, quantity: Number(v) })} />
    <button className="primary">{submitLabel}</button>
  </form>;
}

function OrderTable({ rows, columns, statusOptions, endpoint, reload }: any) {
  return <Table title="Orders" rows={rows} columns={columns} action={(row) => <select value={row.status} onChange={async (e) => { await api.put(`${endpoint}/${row.id}/status`, { status: e.target.value }); reload(); }}>{statusOptions.map((status: string) => <option key={status}>{status}</option>)}</select>} />;
}

function Table({ title, rows, columns, action }: { title: string; rows: any[]; columns: string[]; action?: (row: any) => React.ReactNode }) {
  return <div className="panel"><h2>{title}</h2><div className="table-wrap"><table><thead><tr>{columns.map((c) => <th key={c}>{c.split('.').pop()}</th>)}{action && <th>Action</th>}</tr></thead><tbody>{rows.map((row) => <tr key={row.id || JSON.stringify(row)}>{columns.map((c) => <td key={c}>{formatCell(read(row, c))}</td>)}{action && <td>{action(row)}</td>}</tr>)}</tbody></table></div></div>;
}

function read(row: any, path: string) {
  return path.split('.').reduce((value, key) => value?.[key], row);
}

function formatCell(value: any) {
  if (typeof value === 'number' && value > 99) return value.toLocaleString('en-IN');
  return value ?? '';
}

function money(value = 0) {
  return `Rs. ${Number(value).toLocaleString('en-IN')}`;
}

function today() {
  return new Date().toISOString().slice(0, 10);
}

function labelRole(role: Role) {
  return role.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, (letter: string) => letter.toUpperCase());
}

createRoot(document.getElementById('root')!).render(<App />);
