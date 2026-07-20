import axios from 'axios';
import type { Role } from '../types';

export const api = axios.create({ baseURL: '' });

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('erpToken');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export async function login(username: string, password: string) {
  const { data } = await api.post('/api/auth/login', { username, password });
  localStorage.setItem('erpToken', data.token);
  localStorage.setItem('erpUser', JSON.stringify({ username: data.username, role: data.role as Role }));
  return data;
}

export async function register(username: string, email: string, password: string, role: Role) {
  const { data } = await api.post('/api/auth/register', { username, email, password, role });
  localStorage.setItem('erpToken', data.token);
  localStorage.setItem('erpUser', JSON.stringify({ username: data.username, role: data.role as Role }));
  return data;
}

export function currentUser(): { username: string; role: Role } | null {
  const raw = localStorage.getItem('erpUser');
  return raw ? JSON.parse(raw) : null;
}

export function logout() {
  localStorage.removeItem('erpToken');
  localStorage.removeItem('erpUser');
}
