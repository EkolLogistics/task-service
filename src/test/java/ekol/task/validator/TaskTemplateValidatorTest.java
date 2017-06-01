package ekol.task.validator;

import ekol.task.domain.TaskTemplate;
import ekol.task.domain.exception.ValidationError;
import ekol.task.domain.validator.TaskTemplateValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static ekol.task.builder.Builder.aValidTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskTemplateValidatorTest {

    @Autowired
    private TaskTemplateValidator taskTemplateValidator;

    @Test
    public void shouldValidate() {
        TaskTemplate validTemplate = aValidTemplate().build();
        taskTemplateValidator.validateNew(validTemplate);
    }

    @Test(expected = ValidationError.class)
    public void shouldValidateName() {
        TaskTemplate invalidTemplate = aValidTemplate().but().withName("").build();
        taskTemplateValidator.validateNew(invalidTemplate);
    }

    @Test(expected = ValidationError.class)
    public void shouldValidateDuration() {
        TaskTemplate invalidTemplate = aValidTemplate().but().withDuration(null).build();
        taskTemplateValidator.validateNew(invalidTemplate);
    }
}
