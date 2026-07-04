import {useCallback,useEffect,useState} from 'react'
import {taskApi} from '../services/api'
import type {Status,Task,TaskInput} from '../types'
export function useTasks(){
  const [tasks,setTasks]=useState<Task[]>([]),[loading,setLoading]=useState(true),[error,setError]=useState('')
  const run=useCallback(async<T,>(work:()=>Promise<T>)=>{setError('');try{return await work()}catch(e:any){setError(e.response?.data?.detail||e.message||'Something went wrong');throw e}},[])
  const load=useCallback(async()=>{setLoading(true);try{setTasks(await run(taskApi.all))}catch{}finally{setLoading(false)}},[run])
  useEffect(()=>{load()},[load])
  return {tasks,loading,error,load,
    create:(v:TaskInput)=>run(async()=>{const t=await taskApi.create(v);setTasks(x=>[t,...x])}),
    update:(id:number,v:TaskInput)=>run(async()=>{const t=await taskApi.update(id,v);setTasks(x=>x.map(i=>i.id===id?t:i))}),
    move:(id:number,status:Status)=>run(async()=>{setTasks(x=>x.map(i=>i.id===id?{...i,status}:i));try{const t=await taskApi.status(id,status);setTasks(x=>x.map(i=>i.id===id?t:i))}catch(e){load();throw e}}),
    remove:(id:number)=>run(async()=>{await taskApi.remove(id);setTasks(x=>x.filter(i=>i.id!==id))})
  }
}
