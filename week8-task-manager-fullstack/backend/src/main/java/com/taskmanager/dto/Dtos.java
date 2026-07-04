package com.taskmanager.dto;
import com.taskmanager.model.Task;
import jakarta.validation.constraints.*;
import java.time.*;
public final class Dtos {
  private Dtos(){}
  public record Register(@NotBlank String name,@Email String email,@Size(min=6) String password){}
  public record Login(@Email String email,@NotBlank String password){}
  public record Refresh(@NotBlank String refreshToken){}
  public record Auth(String accessToken,String refreshToken,UserView user){}
  public record UserView(Long id,String name,String email){}
  public record TaskRequest(@NotBlank @Size(max=160) String title,@Size(max=2000) String description,
    Task.Status status,Task.Priority priority,LocalDate dueDate){}
  public record StatusRequest(@NotNull Task.Status status){}
  public record TaskView(Long id,String title,String description,Task.Status status,Task.Priority priority,LocalDate dueDate,Instant createdAt){}
  public static TaskView view(Task t){return new TaskView(t.getId(),t.getTitle(),t.getDescription(),t.getStatus(),t.getPriority(),t.getDueDate(),t.getCreatedAt());}
}
