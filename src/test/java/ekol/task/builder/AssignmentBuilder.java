package ekol.task.builder;

import ekol.task.domain.Assignee;
import ekol.task.domain.Assignment;

import java.time.ZonedDateTime;

/**
 * Created by kilimci on 01/06/2017.
 */
public final class AssignmentBuilder {
    private Assignee assignee;
    private ZonedDateTime assignedAt;

    private AssignmentBuilder() {
    }

    public static AssignmentBuilder anAssignment() {
        return new AssignmentBuilder();
    }

    public AssignmentBuilder withAssignee(Assignee assignee) {
        this.assignee = assignee;
        return this;
    }

    public AssignmentBuilder withAssignedAt(ZonedDateTime assignedAt) {
        this.assignedAt = assignedAt;
        return this;
    }

    public AssignmentBuilder but() {
        return anAssignment().withAssignee(assignee).withAssignedAt(assignedAt);
    }

    public Assignment build() {
        Assignment assignment = new Assignment();
        assignment.setAssignee(assignee);
        assignment.setAssignedAt(assignedAt);
        return assignment;
    }
}
