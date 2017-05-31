package ekol.task.builder;

import ekol.task.domain.Task;
import ekol.task.domain.TaskStatus;

import java.time.ZonedDateTime;

/**
 * Created by kilimci on 31/05/2017.
 */
public final class TaskBuilder {
    private String id;
    private String name;
    private TaskStatus status;
    private ZonedDateTime createdAt;

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

    public TaskBuilder but() {
        return aTask().withId(id).withName(name).withStatus(status).withCreatedAt(createdAt);
    }

    public Task build() {
        Task task = new Task();
        task.setId(id);
        task.setName(name);
        task.setStatus(status);
        task.setCreatedAt(createdAt);
        return task;
    }
}
