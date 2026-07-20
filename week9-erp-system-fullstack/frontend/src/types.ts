export type Role = 'ADMIN' | 'SALES_EXECUTIVE' | 'PURCHASE_MANAGER' | 'INVENTORY_MANAGER' | 'ACCOUNTANT';
export type OrderStatus = 'PENDING' | 'APPROVED' | 'DISPATCHED' | 'ORDERED' | 'RECEIVED';
export type InvoiceStatus = 'PAID' | 'UNPAID';

export interface Product {
  id?: number;
  name: string;
  sku: string;
  category: string;
  unitPrice: number;
  currentStock: number;
  reorderLevel: number;
}

export interface Partner {
  id?: number;
  type?: 'CUSTOMER' | 'SUPPLIER';
  name: string;
  email: string;
  phone: string;
  address: string;
  gstin?: string;
}

export interface LineItem {
  productId: number;
  quantity: number;
}

export interface SalesOrder {
  id: number;
  customer: Partner;
  orderDate: string;
  status: OrderStatus;
  totalAmount: number;
}

export interface PurchaseOrder {
  id: number;
  supplier: Partner;
  expectedDeliveryDate: string;
  status: OrderStatus;
  totalAmount: number;
}

export interface Invoice {
  id: number;
  customer: Partner;
  salesOrder: SalesOrder;
  tax: number;
  totalPayable: number;
  status: InvoiceStatus;
  invoiceDate: string;
}

export interface DashboardSummary {
  totalSalesThisMonth: number;
  totalPurchasesThisMonth: number;
  pendingInvoices: number;
  pendingSalesOrders: number;
}
