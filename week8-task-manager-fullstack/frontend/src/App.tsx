import {useState} from 'react'
import {AuthForm} from './components/AuthForm'
import {Board} from './components/Board'
import {clearAuth} from './services/api'
export default function App(){const [signedIn,setSignedIn]=useState(!!localStorage.getItem('accessToken'));return signedIn?<Board logout={()=>{clearAuth();setSignedIn(false)}}/>:<AuthForm onDone={()=>setSignedIn(true)}/>}
