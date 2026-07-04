import {useEffect,useMemo,useState} from 'react'
import {Client} from '@stomp/stompjs'
import {useTasks} from '../hooks/useTasks'
import type {Status,Task} from '../types'
import {TaskForm} from './TaskForm'
const columns:{status:Status;title:string}[]=[{status:'TODO',title:'To do'},{status:'IN_PROGRESS',title:'In progress'},{status:'DONE',title:'Done'}]
export function Board({logout}:{logout:()=>void}){
  const api=useTasks(),[editing,setEditing]=useState<Task|undefined>(),[form,setForm]=useState(false),[query,setQuery]=useState('')
  const user=JSON.parse(localStorage.getItem('user')||'{}')
  const visible=useMemo(()=>api.tasks.filter(t=>(t.title+' '+t.description).toLowerCase().includes(query.toLowerCase())),[api.tasks,query])
  useEffect(()=>{const client=new Client({brokerURL:import.meta.env.VITE_WS_URL||'ws://localhost:8080/ws',reconnectDelay:5000,onConnect:()=>client.subscribe('/topic/tasks',()=>api.load())});client.activate();return()=>{void client.deactivate()}},[api.load])
  return <><header className="topbar"><div className="brand">FLOWBOARD</div><div className="user"><span>{user.name}</span><button onClick={logout}>Sign out</button></div></header>
    <main className="workspace"><div className="hero"><div><p className="eyebrow">MY WORKSPACE</p><h1>Good work starts with a clear board.</h1><p>{api.tasks.length} tasks across {columns.length} stages</p></div><button className="primary" onClick={()=>{setEditing(undefined);setForm(true)}}>＋ Add task</button></div>
    <div className="toolbar"><input className="search" placeholder="Search tasks…" value={query} onChange={e=>setQuery(e.target.value)}/><button onClick={api.load}>Refresh</button></div>
    {api.error&&<div className="error banner">{api.error}</div>}
    {api.loading?<div className="skeletons">{[1,2,3].map(x=><div className="skeleton" key={x}/>)}</div>:<div className="board">{columns.map(col=><section className="column" key={col.status} onDragOver={e=>e.preventDefault()} onDrop={e=>api.move(Number(e.dataTransfer.getData('task')),col.status)}>
      <header><h2>{col.title}</h2><span>{visible.filter(t=>t.status===col.status).length}</span></header>
      <div className="cards">{visible.filter(t=>t.status===col.status).map(task=><article draggable onDragStart={e=>e.dataTransfer.setData('task',String(task.id))} className="task" key={task.id}>
        <div className={`priority ${task.priority.toLowerCase()}`}>{task.priority}</div><h3>{task.title}</h3>{task.description&&<p>{task.description}</p>}
        <footer><span>{task.dueDate?`Due ${new Date(task.dueDate+'T00:00').toLocaleDateString()}`:'No due date'}</span><div><button className="icon" onClick={()=>{setEditing(task);setForm(true)}}>✎</button><button className="icon danger" onClick={()=>confirm('Delete this task?')&&api.remove(task.id)}>×</button></div></footer>
      </article>)}</div></section>)}</div>}</main>
    {form&&<TaskForm task={editing} onClose={()=>setForm(false)} onSave={v=>editing?api.update(editing.id,v):api.create(v)}/>}</>
}
