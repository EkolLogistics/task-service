package ekol.task.builder;

import ekol.task.domain.Assignee;

/**
 * Created by kilimci on 01/06/2017.
 */
public final class AssigneeBuilder {
    private String id;
    private String name;

    private AssigneeBuilder() {
    }

    public static AssigneeBuilder anAssignee() {
        return new AssigneeBuilder();
    }

    public AssigneeBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public AssigneeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public AssigneeBuilder but() {
        return anAssignee().withId(id).withName(name);
    }

    public Assignee build() {
        Assignee assignee = new Assignee();
        assignee.setId(id);
        assignee.setName(name);
        return assignee;
    }
}
