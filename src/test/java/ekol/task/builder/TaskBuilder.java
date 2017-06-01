package ekol.task.builder;

import ekol.task.domain.Assignment;
import ekol.task.domain.Task;
import ekol.task.domain.TaskStatus;

import java.time.ZonedDateTime;

/**
 * Created by kilimci on 01/06/2017.
 */
public final class TaskBuilder {
    private String id;
    private String name;
    private TaskStatus status;
    private ZonedDateTime createdAt;
    private ZonedDateTime due;
    private Assignment assignment;

    private TaskBuilder() {
    }

    public static TaskBuilder aTask() {
        return new TaskBuilder();
    }

    public TaskBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public TaskBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TaskBuilder withStatus(TaskStatus status) {
        this.status = status;
        return this;
    }

    public TaskBuilder withCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public TaskBuilder withDue(ZonedDateTime due) {
        this.due = due;
        return this;
    }

    public TaskBuilder withAssignment(Assignment assignment) {
        this.assignment = assignment;
        return this;
    }

    public TaskBuilder but() {
        return aTask().withId(id).withName(name).withStatus(status).withCreatedAt(createdAt).withDue(due).withAssignment(assignment);
    }

    public Task build() {
        Task task = new Task();
        task.setId(id);
        task.setName(name);
        task.setStatus(status);
        task.setCreatedAt(createdAt);
        task.setDue(due);
        task.setAssignment(assignment);
        return task;
    }
}
