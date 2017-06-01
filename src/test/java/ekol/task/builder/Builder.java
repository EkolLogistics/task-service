package ekol.task.builder;

import ekol.task.domain.TaskStatus;

import java.time.Duration;
import java.time.ZonedDateTime;

public class Builder {

    public static TaskBuilder aValidTask(){
        return TaskBuilder.aTask().withName("test").withStatus(TaskStatus.NEW).withCreatedAt(ZonedDateTime.now());
    }
    public static TaskBuilder anInvalidTask(){
        return TaskBuilder.aTask();
    }

    public static TaskBuilder aValidTaskWithAssignment(){
        return aValidTask().withAssignment(aValidAssignment().build());
    }

    public static TaskTemplateBuilder aValidTemplate(){
        return TaskTemplateBuilder.aTaskTemplate().withName("test").withDuration(Duration.ofMinutes(1));
    }

    public static TaskTemplateBuilder anInvalidTemplate(){
        return TaskTemplateBuilder.aTaskTemplate();
    }

    public static AssigneeBuilder aValidAssignee(){
        return AssigneeBuilder.anAssignee().withName("test").withId("1");
    }
    public static AssigneeBuilder anInvalidAssignee(){
        return AssigneeBuilder.anAssignee();
    }

    public static AssignmentBuilder aValidAssignment(){
        return AssignmentBuilder.anAssignment().withAssignedAt(ZonedDateTime.now()).withAssignee(aValidAssignee().build());
    }
    public static AssignmentBuilder anInvalidAssignment(){
        return AssignmentBuilder.anAssignment();
    }
}
