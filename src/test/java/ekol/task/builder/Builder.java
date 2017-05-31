package ekol.task.builder;

import ekol.task.domain.TaskStatus;

import java.time.ZonedDateTime;

public class Builder {

    public static TaskBuilder aValidTask(){
        return TaskBuilder.aTask().withName("test").withStatus(TaskStatus.NEW).withCreatedAt(ZonedDateTime.now());
    }
    public static TaskBuilder anInvalidTask(){
        return TaskBuilder.aTask();
    }
}
