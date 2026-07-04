import axios from 'axios'
import type {AuthResponse,Status,Task,TaskInput} from '../types'
const baseURL=import.meta.env.VITE_API_URL||'http://localhost:8080/api'
const api=axios.create({baseURL})
api.interceptors.request.use(config=>{const token=localStorage.getItem('accessToken');if(token)config.headers.Authorization=`Bearer ${token}`;return config})
api.interceptors.response.use(r=>r,async error=>{
  const request=error.config
  if(error.response?.status===401&&!request._retry&&localStorage.getItem('refreshToken')){
    request._retry=true
    try{const {data}=await axios.post<AuthResponse>(`${baseURL}/auth/refresh`,{refreshToken:localStorage.getItem('refreshToken')});saveAuth(data);request.headers.Authorization=`Bearer ${data.accessToken}`;return api(request)}
    catch{clearAuth();window.location.reload()}
  }
  return Promise.reject(error)
})
export const saveAuth=(a:AuthResponse)=>{localStorage.setItem('accessToken',a.accessToken);localStorage.setItem('refreshToken',a.refreshToken);localStorage.setItem('user',JSON.stringify(a.user))}
export const clearAuth=()=>{localStorage.removeItem('accessToken');localStorage.removeItem('refreshToken');localStorage.removeItem('user')}
export const authApi={
  login:async(email:string,password:string)=>(await api.post<AuthResponse>('/auth/login',{email,password})).data,
  register:async(name:string,email:string,password:string)=>(await api.post<AuthResponse>('/auth/register',{name,email,password})).data
}
export const taskApi={
  all:async()=>(await api.get<Task[]>('/tasks')).data,
  create:async(input:TaskInput)=>(await api.post<Task>('/tasks',input)).data,
  update:async(id:number,input:TaskInput)=>(await api.put<Task>(`/tasks/${id}`,input)).data,
  status:async(id:number,status:Status)=>(await api.put<Task>(`/tasks/${id}/status`,{status})).data,
  remove:async(id:number)=>api.delete(`/tasks/${id}`)
}
