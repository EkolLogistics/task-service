package ekol.task.validator;


import ekol.task.domain.Task;
import ekol.task.domain.TaskStatus;
import ekol.task.domain.exception.ValidationError;
import ekol.task.domain.validator.TaskValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static ekol.task.builder.Builder.aValidTask;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskValidatorTest {

    @Autowired
    private TaskValidator taskValidator;

    @Test
    public void shouldValidate() {
        Task validTask = aValidTask().build();
        taskValidator.validateNewTask(validTask);
    }

    @Test(expected = ValidationError.class)
    public void shouldValidateName() {
        Task validTask = aValidTask().but().withName("").build();
        taskValidator.validateNewTask(validTask);
    }

    @Test(expected = ValidationError.class)
    public void shouldValidateCreateDate() {
        Task validTask = aValidTask().but().withCreatedAt(null).build();
        taskValidator.validateNewTask(validTask);
    }

    @Test(expected = ValidationError.class)
    public void shouldValidateNullStatus() {
        Task validTask = aValidTask().but().withStatus(null).build();
        taskValidator.validateNewTask(validTask);
    }

    @Test(expected = ValidationError.class)
    public void shouldValidateDoneStatus() {
        Task validTask = aValidTask().but().withStatus(TaskStatus.DONE).build();
        taskValidator.validateNewTask(validTask);
    }

    @Test(expected = ValidationError.class)
    public void shouldValidateInProgressStatus() {
        Task validTask = aValidTask().but().withStatus(TaskStatus.INPROGRESS).build();
        taskValidator.validateNewTask(validTask);
    }
}
