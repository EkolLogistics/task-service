package ekol.task.service;

import ekol.task.domain.Task;
import ekol.task.domain.TaskStatus;
import ekol.task.domain.TaskTemplate;
import ekol.task.domain.exception.ValidationError;
import ekol.task.domain.validator.TaskValidator;
import ekol.task.repository.TaskRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static ekol.task.builder.Builder.aValidTask;
import static ekol.task.builder.Builder.aValidTemplate;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CreateTaskServiceTest {

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private TaskValidator taskValidator;

    @MockBean
    private FetchTaskTemplateService fetchTaskTemplateService;

    @Autowired
    private CreateTaskService createTaskService;

    @Test
    public void shouldLoadContext() throws Exception {
        assertThat(taskRepository, notNullValue());
        assertThat(taskValidator, notNullValue());
        assertThat(createTaskService, notNullValue());
    }

    @Test
    public void shouldCreateTask(){
        Task validTask = aValidTask().build();
        when(taskRepository.save(validTask)).thenReturn(validTask);

        createTaskService.create(validTask);

        verify(taskValidator).validateNewTask(validTask);
        verify(taskRepository).save(validTask);
    }

    @Test
    public void shouldSetNewStatusToTask(){
        Task validTask = aValidTask().but().withStatus(null).build();
        when(taskRepository.save(validTask)).thenReturn(validTask);
        createTaskService.create(validTask);

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskValidator).validateNewTask(validTask);
        verify(taskRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus(), equalTo(TaskStatus.NEW));
    }

    @Test
    public void shouldSetCreatedDateToTask(){
        Task validTask = aValidTask().but().withCreatedAt(null).build();
        when(taskRepository.save(validTask)).thenReturn(validTask);
        createTaskService.create(validTask);

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskValidator).validateNewTask(validTask);
        verify(taskRepository).save(captor.capture());
        assertThat(captor.getValue().getCreatedAt(), notNullValue());
    }

    @Test(expected = ValidationError.class)
    public void shouldNotCreateTaskWhenValidationFails(){
        Task validTask = aValidTask().build();
        doThrow(new ValidationError("task is not valid")).when(taskValidator).validateNewTask(validTask);
        when(taskRepository.save(validTask)).thenReturn(validTask);

        createTaskService.create(validTask);
    }

    @Test
    public void shouldCreateTaskWithTemplate(){
        String id = "1";
        TaskTemplate validTemplate = aValidTemplate().build();
        when(fetchTaskTemplateService.fetch(id)).thenReturn(validTemplate);
        when(taskRepository.save(any(Task.class))).thenReturn(null);

        createTaskService.createWithTemplate(id);

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskValidator).validateNewTask(any(Task.class));
        verify(taskRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus(), equalTo(TaskStatus.NEW));
        assertThat(captor.getValue().getName(), equalTo(validTemplate.getName()));
        assertThat(captor.getValue().getCreatedAt(), notNullValue());
        assertThat(captor.getValue().getDue(), equalTo(captor.getValue().getCreatedAt().plus(validTemplate.getDuration())));
    }

    @Test(expected = ValidationError.class)
    public void shouldNotCreateTaskWithNonExistingTemplateId(){
        String id = "1";
        doThrow(new ValidationError("template not found")).when(fetchTaskTemplateService).fetch(id);
        when(taskRepository.save(any(Task.class))).thenReturn(null);

        createTaskService.createWithTemplate(id);
    }
}
