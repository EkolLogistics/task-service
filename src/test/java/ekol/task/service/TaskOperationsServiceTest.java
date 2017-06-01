package ekol.task.service;

import ekol.task.domain.Assignee;
import ekol.task.domain.Assignment;
import ekol.task.domain.Task;
import ekol.task.domain.exception.ResourceNotFoundError;
import ekol.task.domain.exception.ValidationError;
import ekol.task.domain.validator.AssignmentValidator;
import ekol.task.repository.TaskRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static ekol.task.builder.Builder.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskOperationsServiceTest {

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private AssignmentValidator assignmentValidator;

    @MockBean
    private FetchTaskService fetchTaskService;

    @Autowired
    private TaskOperationsService taskOperationsService;

    @Test
    public void shouldAssignTaskToAssignee(){
        String taskId = "1";
        Task validTask = aValidTask().build();
        Assignee validAssignee = aValidAssignee().build();
        when(fetchTaskService.fetch(taskId)).thenReturn(validTask);
        when(taskRepository.save(validTask)).thenReturn(validTask);

        taskOperationsService.assign(taskId, validAssignee);

        ArgumentCaptor<Assignment> assignmentCaptor = ArgumentCaptor.forClass(Assignment.class);
        verify(assignmentValidator).validate(assignmentCaptor.capture());
        assertThat(assignmentCaptor.getValue().getAssignee().getId(), equalTo(validAssignee.getId()));
        assertThat(assignmentCaptor.getValue().getAssignee().getName(), equalTo(validAssignee.getName()));

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(taskCaptor.capture());
        assertThat(taskCaptor.getValue().getAssignment(), notNullValue());
        assertThat(taskCaptor.getValue().getAssignment().getAssignedAt(), notNullValue());
        assertThat(taskCaptor.getValue().getAssignment().getAssignee(), notNullValue());
        assertThat(taskCaptor.getValue().getAssignment().getAssignee().getName(), equalTo(validAssignee.getName()));
        assertThat(taskCaptor.getValue().getAssignment().getAssignee().getId(), equalTo(validAssignee.getId()));
    }

    @Test(expected = ValidationError.class)
    public void shouldNotAssignToTaskWithAssignment(){
        String taskId = "1";
        Task taskWithAssignment = aValidTaskWithAssignment().build();
        Assignee validAssignee = aValidAssignee().build();
        when(fetchTaskService.fetch(taskId)).thenReturn(taskWithAssignment);
        when(taskRepository.save(taskWithAssignment)).thenReturn(taskWithAssignment);

        taskOperationsService.assign(taskId, validAssignee);
    }

    @Test(expected = ValidationError.class)
    public void shouldNotAssignToInvalidAssignee(){
        String taskId = "1";
        Task validTask = aValidTask().build();
        Assignee invalidAssignee = anInvalidAssignee().build();
        doThrow(new ValidationError("assignee is not valid")).when(assignmentValidator).validate(any(Assignment.class));
        when(fetchTaskService.fetch(taskId)).thenReturn(validTask);
        when(taskRepository.save(validTask)).thenReturn(validTask);

        taskOperationsService.assign(taskId, invalidAssignee);
    }

    @Test(expected = ResourceNotFoundError.class)
    public void shouldNotAssignToNonExistingTask(){
        String taskId = "1";
        Assignee validAssignee = aValidAssignee().build();
        doThrow(new ResourceNotFoundError("task not found")).when(fetchTaskService).fetch(taskId);

        taskOperationsService.assign(taskId, validAssignee);
    }
}
