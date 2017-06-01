package ekol.task.builder;

import ekol.task.domain.TaskTemplate;

import java.time.Duration;

/**
 * Created by kilimci on 01/06/2017.
 */
public final class TaskTemplateBuilder {
    private String id;
    private String name;
    private Duration duration;

    private TaskTemplateBuilder() {
    }

    public static TaskTemplateBuilder aTaskTemplate() {
        return new TaskTemplateBuilder();
    }

    public TaskTemplateBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public TaskTemplateBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TaskTemplateBuilder withDuration(Duration duration) {
        this.duration = duration;
        return this;
    }

    public TaskTemplateBuilder but() {
        return aTaskTemplate().withId(id).withName(name).withDuration(duration);
    }

    public TaskTemplate build() {
        TaskTemplate taskTemplate = new TaskTemplate();
        taskTemplate.setId(id);
        taskTemplate.setName(name);
        taskTemplate.setDuration(duration);
        return taskTemplate;
    }
}
