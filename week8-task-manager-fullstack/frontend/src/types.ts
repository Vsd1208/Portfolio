export type Status='TODO'|'IN_PROGRESS'|'DONE'
export type Priority='LOW'|'MEDIUM'|'HIGH'
export interface Task {id:number;title:string;description:string;status:Status;priority:Priority;dueDate:string|null;createdAt:string}
export interface TaskInput {title:string;description:string;status:Status;priority:Priority;dueDate:string|null}
export interface User {id:number;name:string;email:string}
export interface AuthResponse {accessToken:string;refreshToken:string;user:User}
