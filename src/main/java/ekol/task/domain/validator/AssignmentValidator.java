package ekol.task.domain.validator;

import ekol.task.domain.Assignment;
import ekol.task.domain.exception.ValidationError;
import org.springframework.stereotype.Component;

@Component
public class AssignmentValidator {

    public void validate(Assignment assignment){
        if(assignment.getAssignedAt() == null){
            throw new ValidationError("Assignment should have an assigned date");
        }
        if(assignment.getAssignee() == null){
            throw new ValidationError("Assignment should have an assignee");
        }
        if(assignment.getAssignee().getId() == null || assignment.getAssignee().getName() == null){
            throw new ValidationError("Assignment should have a valid assignee with id and name");
        }
    }
}
