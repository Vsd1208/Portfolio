package com.taskmanager.controller;
import com.taskmanager.dto.Dtos.*;
import com.taskmanager.model.*;
import com.taskmanager.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@RestController @RequestMapping("/api/tasks")
public class TaskController {
  private final TaskRepository tasks; private final UserRepository users; private final SimpMessagingTemplate events;
  public TaskController(TaskRepository t,UserRepository u,SimpMessagingTemplate e){tasks=t;users=u;events=e;}
  @GetMapping public List<TaskView> all(Authentication a){return tasks.findByOwnerOrderByCreatedAtDesc(user(a)).stream().map(com.taskmanager.dto.Dtos::view).toList();}
  @PostMapping @ResponseStatus(HttpStatus.CREATED) public TaskView create(@Valid @RequestBody TaskRequest r,Authentication a){Task t=new Task();apply(t,r);t.setOwner(user(a));return publish(tasks.save(t));}
  @PutMapping("/{id}") public TaskView update(@PathVariable Long id,@Valid @RequestBody TaskRequest r,Authentication a){Task t=owned(id,a);apply(t,r);return publish(tasks.save(t));}
  @PutMapping("/{id}/status") public TaskView status(@PathVariable Long id,@Valid @RequestBody StatusRequest r,Authentication a){Task t=owned(id,a);t.setStatus(r.status());return publish(tasks.save(t));}
  @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id,Authentication a){Task t=owned(id,a);tasks.delete(t);events.convertAndSend("/topic/tasks",new Event("deleted",id));}
  private User user(Authentication a){return users.findByEmail(a.getName()).orElseThrow();}
  private Task owned(Long id,Authentication a){Task t=tasks.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));if(!t.getOwner().getEmail().equals(a.getName()))throw new ResponseStatusException(HttpStatus.NOT_FOUND);return t;}
  private void apply(Task t,TaskRequest r){t.setTitle(r.title());t.setDescription(r.description()==null?"":r.description());if(r.status()!=null)t.setStatus(r.status());if(r.priority()!=null)t.setPriority(r.priority());t.setDueDate(r.dueDate());}
  private TaskView publish(Task t){TaskView v=com.taskmanager.dto.Dtos.view(t);events.convertAndSend("/topic/tasks",new Event("changed",v));return v;}
  record Event(String type,Object task){}
}
