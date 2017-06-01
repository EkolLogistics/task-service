package ekol.task.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Document(collection = "tasks")
public class Task {

    @Id
    private String id;
    private String name;
    private TaskStatus status;
    private ZonedDateTime createdAt;
    private ZonedDateTime due;
    private Assignment assignment;

    public static Task withTemplate(TaskTemplate template){
        Task newTask = new Task();
        newTask.setStatusNew();
        newTask.setCreatedAtNow();
        newTask.setName(template.getName());
        newTask.setDue(newTask.getCreatedAt().plus(template.getDuration()));
        return newTask;
    }

    public void setStatusNew(){
        setStatus(TaskStatus.NEW);
    }
    public void setCreatedAtNow(){
        setCreatedAt(ZonedDateTime.now(ZoneId.of("UTC")));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getDue() {
        return due;
    }

    public void setDue(ZonedDateTime due) {
        this.due = due;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }
}
