package ekol.task.service;

import ekol.task.domain.TaskTemplate;
import ekol.task.domain.exception.ValidationError;
import ekol.task.domain.validator.TaskTemplateValidator;
import ekol.task.repository.TaskTemplateRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static ekol.task.builder.Builder.aValidTemplate;
import static ekol.task.builder.Builder.anInvalidTemplate;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CreateTaskTemplateServiceTest {

    @MockBean
    private TaskTemplateRepository taskTemplateRepository;

    @MockBean
    private TaskTemplateValidator taskTemplateValidator;

    @Autowired
    private CreateTaskTemplateService createTaskTemplateService;


    @Test
    public void shouldCreateTaskTemplate(){
        TaskTemplate validTemplate = aValidTemplate().build();
        when(taskTemplateRepository.save(validTemplate)).thenReturn(validTemplate);

        createTaskTemplateService.create(validTemplate);

        verify(taskTemplateValidator).validateNew(validTemplate);
        verify(taskTemplateRepository).save(validTemplate);
    }

    @Test(expected = ValidationError.class)
    public void shouldNotCreateTaskTemplateWhenValidationFails(){
        TaskTemplate invalidTemplate = anInvalidTemplate().build();
        doThrow(new ValidationError("task is not valid")).when(taskTemplateValidator).validateNew(invalidTemplate);
        when(taskTemplateRepository.save(invalidTemplate)).thenReturn(invalidTemplate);

        createTaskTemplateService.create(invalidTemplate);
    }
}
