import {useEffect,useState} from 'react'
import type {Task,TaskInput} from '../types'
const blank:TaskInput={title:'',description:'',status:'TODO',priority:'MEDIUM',dueDate:null}
export function TaskForm({task,onSave,onClose}:{task?:Task;onSave:(v:TaskInput)=>Promise<unknown>;onClose:()=>void}){
  const [value,setValue]=useState<TaskInput>(blank),[busy,setBusy]=useState(false)
  useEffect(()=>{setValue(task?{title:task.title,description:task.description,status:task.status,priority:task.priority,dueDate:task.dueDate}:blank)},[task])
  const set=(key:keyof TaskInput,v:string)=>setValue(x=>({...x,[key]:v||null}))
  return <div className="modal-backdrop" onMouseDown={onClose}><form className="modal" onMouseDown={e=>e.stopPropagation()} onSubmit={async e=>{e.preventDefault();setBusy(true);try{await onSave(value);onClose()}finally{setBusy(false)}}}>
    <header><h2>{task?'Edit task':'New task'}</h2><button type="button" className="icon" onClick={onClose}>×</button></header>
    <label>Title<input value={value.title} onChange={e=>set('title',e.target.value)} maxLength={160} required autoFocus/></label>
    <label>Description<textarea value={value.description} onChange={e=>set('description',e.target.value)} rows={4}/></label>
    <div className="form-row"><label>Priority<select value={value.priority} onChange={e=>set('priority',e.target.value)}><option>LOW</option><option>MEDIUM</option><option>HIGH</option></select></label>
    <label>Due date<input type="date" value={value.dueDate||''} onChange={e=>set('dueDate',e.target.value)}/></label></div>
    <button className="primary" disabled={busy}>{busy?'Saving…':'Save task'}</button></form></div>
}
