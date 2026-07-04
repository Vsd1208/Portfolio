package com.taskmanager.model;
import jakarta.persistence.*;
import java.time.*;
@Entity @Table(name="tasks")
public class Task {
  public enum Status { TODO, IN_PROGRESS, DONE }
  public enum Priority { LOW, MEDIUM, HIGH }
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private String title;
  @Column(length=2000) private String description="";
  @Enumerated(EnumType.STRING) @Column(nullable=false) private Status status=Status.TODO;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private Priority priority=Priority.MEDIUM;
  private LocalDate dueDate;
  @Column(nullable=false,updatable=false) private Instant createdAt=Instant.now();
  @ManyToOne(optional=false,fetch=FetchType.LAZY) private User owner;
  public Long getId(){return id;} public String getTitle(){return title;} public void setTitle(String v){title=v;}
  public String getDescription(){return description;} public void setDescription(String v){description=v;}
  public Status getStatus(){return status;} public void setStatus(Status v){status=v;}
  public Priority getPriority(){return priority;} public void setPriority(Priority v){priority=v;}
  public LocalDate getDueDate(){return dueDate;} public void setDueDate(LocalDate v){dueDate=v;}
  public Instant getCreatedAt(){return createdAt;} public User getOwner(){return owner;} public void setOwner(User v){owner=v;}
}
