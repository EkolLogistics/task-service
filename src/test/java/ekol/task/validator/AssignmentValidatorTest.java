package ekol.task.validator;

import ekol.task.domain.Assignment;
import ekol.task.domain.exception.ValidationError;
import ekol.task.domain.validator.AssignmentValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static ekol.task.builder.Builder.aValidAssignee;
import static ekol.task.builder.Builder.aValidAssignment;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AssignmentValidatorTest {

    @Autowired
    private AssignmentValidator assignmentValidator;

    @Test
    public void shouldValidate() {
        Assignment validAssignment = aValidAssignment().build();
        assignmentValidator.validate(validAssignment);
    }

    @Test(expected = ValidationError.class)
    public void shouldNotValidateEmptyAssignee() {
        Assignment invalidAssignment = aValidAssignment().but().withAssignee(null).build();
        assignmentValidator.validate(invalidAssignment);
    }

    @Test(expected = ValidationError.class)
    public void shouldNotValidateEmptyAssigneeId() {
        Assignment invalidAssignment = aValidAssignment().but().withAssignee(aValidAssignee().but().withId(null).build()).build();
        assignmentValidator.validate(invalidAssignment);
    }

    @Test(expected = ValidationError.class)
    public void shouldNotValidateEmptyAssigneeName() {
        Assignment invalidAssignment = aValidAssignment().but().withAssignee(aValidAssignee().but().withName(null).build()).build();
        assignmentValidator.validate(invalidAssignment);
    }

    @Test(expected = ValidationError.class)
    public void shouldNotValidateEmptyAssignedDate() {
        Assignment invalidAssignment = aValidAssignment().but().withAssignedAt(null).build();
        assignmentValidator.validate(invalidAssignment);
    }
}
