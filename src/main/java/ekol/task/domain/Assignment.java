package ekol.task.domain;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Assignment {

    private Assignee assignee;
    private ZonedDateTime assignedAt;

    public void setAssignedAtNow(){
        setAssignedAt(ZonedDateTime.now(ZoneId.of("UTC")));
    }

    public Assignee getAssignee() {
        return assignee;
    }

    public void setAssignee(Assignee assignee) {
        this.assignee = assignee;
    }

    public ZonedDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(ZonedDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public static Assignment withAssignee(Assignee assignee) {
        Assignment assignment = new Assignment();
        assignment.setAssignee(assignee);
        assignment.setAssignedAtNow();
        return assignment;
    }
}
