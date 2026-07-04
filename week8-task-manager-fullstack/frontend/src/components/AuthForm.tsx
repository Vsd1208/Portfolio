import {useState} from 'react'
import {authApi,saveAuth} from '../services/api'
export function AuthForm({onDone}:{onDone:()=>void}){
  const [register,setRegister]=useState(false),[busy,setBusy]=useState(false),[error,setError]=useState('')
  async function submit(e:React.FormEvent<HTMLFormElement>){e.preventDefault();setBusy(true);setError('');const data=new FormData(e.currentTarget)
    try{const result=register?await authApi.register(String(data.get('name')),String(data.get('email')),String(data.get('password'))):await authApi.login(String(data.get('email')),String(data.get('password')));saveAuth(result);onDone()}
    catch(e:any){setError(e.response?.data?.detail||'Unable to sign in')}finally{setBusy(false)}}
  return <main className="auth"><section className="auth-card"><div className="brand">FLOWBOARD</div><h1>{register?'Create your workspace':'Welcome back'}</h1><p>Plan the work. Move it forward.</p>
    <form onSubmit={submit}>{register&&<label>Name<input name="name" required autoFocus/></label>}<label>Email<input name="email" type="email" required autoFocus={!register}/></label><label>Password<input name="password" type="password" minLength={6} required/></label>
      {error&&<div className="error">{error}</div>}<button className="primary" disabled={busy}>{busy?'Please wait…':register?'Create account':'Sign in'}</button></form>
    <button className="link" onClick={()=>setRegister(!register)}>{register?'Already have an account? Sign in':'New here? Create an account'}</button></section></main>
}
