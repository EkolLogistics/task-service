package ekol.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ekol.task.domain.Assignee;
import ekol.task.domain.Task;
import ekol.task.domain.exception.ResourceNotFoundError;
import ekol.task.domain.exception.ValidationError;
import ekol.task.service.CreateTaskService;
import ekol.task.service.FetchTaskService;
import ekol.task.service.TaskOperationsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static ekol.task.builder.Builder.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateTaskService createTaskService;

    @MockBean
    private FetchTaskService fetchTaskService;

    @MockBean
    private TaskOperationsService taskOperationsService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String ENDPOINT_URL = "/tasks";

    private String getFetchUrl(String id){
        return ENDPOINT_URL + "/" + id;
    }

    private String toJson(Object obj) throws Exception{
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    public void shouldReturnOKForValidTask() throws Exception{
        String id = "1";
        Task validTask = aValidTask().withId(id).build();
        when(createTaskService.create(any(Task.class))).thenReturn(validTask);

        this.mockMvc.perform(post(ENDPOINT_URL)
                .contentType(MediaType.APPLICATION_JSON).content(toJson(validTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("name").value(validTask.getName()));

        verify(createTaskService).create(any(Task.class));
    }

    @Test
    public void shouldReturnBadRequestForInvalidTask() throws Exception{
        Task invalidTask = anInvalidTask().build();
        when(createTaskService.create(any(Task.class))).thenThrow(new ValidationError("not valid"));

        this.mockMvc.perform(post(ENDPOINT_URL)
                .contentType(MediaType.APPLICATION_JSON).content(toJson(invalidTask)))
                .andExpect(status().isBadRequest());

        verify(createTaskService).create(any(Task.class));
    }

    @Test
    public void shouldReturnOKForFetchValidId() throws Exception{
        String id = "1";
        Task validTask = aValidTask().withId(id).build();
        when(fetchTaskService.fetch(id)).thenReturn(validTask);

        this.mockMvc.perform(get(getFetchUrl(id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("name").value(validTask.getName()));

        verify(fetchTaskService).fetch(id);
    }

    @Test
    public void shouldReturnNotFoundForFetchInvalidId() throws Exception{
        String id = "1";
        when(fetchTaskService.fetch(id)).thenThrow(new ResourceNotFoundError("not found"));

        this.mockMvc.perform(get(getFetchUrl(id))).andExpect(status().isNotFound());

        verify(fetchTaskService).fetch(id);
    }

    @Test
    public void shouldReturnOKForFindAll() throws Exception{
        when(fetchTaskService.findAll(1,1)).thenReturn(null);

        this.mockMvc.perform(get(ENDPOINT_URL).param("page","1").param("size","1"))
                .andExpect(status().isOk());

        verify(fetchTaskService).findAll(1,1);
    }

    @Test
    public void shouldReturnOKForFindAllWithoutParams() throws Exception{
        when(fetchTaskService.findAll(null,null)).thenReturn(null);

        this.mockMvc.perform(get(ENDPOINT_URL)).andExpect(status().isOk());

        verify(fetchTaskService).findAll(null,null);
    }

    @Test
    public void shouldReturnOKForCreateWithTemplate() throws Exception{
        String templateId = "1";
        Task validTask = aValidTask().build();
        when(createTaskService.createWithTemplate(templateId)).thenReturn(validTask);

        this.mockMvc.perform(post(ENDPOINT_URL + "/with-template/" + templateId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(validTask.getName()));

        verify(createTaskService).createWithTemplate(templateId);
    }

    @Test
    public void shouldReturnOKForValidAssignment() throws Exception{
        String taskId = "1";
        Assignee validAssignee = aValidAssignee().build();
        Task validTaskWithAssignment = aValidTaskWithAssignment().build();
        when(taskOperationsService.assign(eq(taskId), any(Assignee.class))).thenReturn(validTaskWithAssignment);
        this.mockMvc.perform(post(getFetchUrl(taskId) + "/assign-to")
                .contentType(MediaType.APPLICATION_JSON).content(toJson(validAssignee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(validTaskWithAssignment.getName()))
                .andExpect(jsonPath("assignment").isNotEmpty())
                .andExpect(jsonPath("assignment.assignee").isNotEmpty())
                .andExpect(jsonPath("assignment.assignee.id").value(validAssignee.getId()))
                .andExpect(jsonPath("assignment.assignee.name").value(validAssignee.getName()));

        ArgumentCaptor<Assignee> captor = ArgumentCaptor.forClass(Assignee.class);
        verify(taskOperationsService).assign(eq(taskId), captor.capture());
        assertThat(captor.getValue().getId(), equalTo(validAssignee.getId()));
        assertThat(captor.getValue().getName(), equalTo(validAssignee.getName()));
    }

    @Test
    public void shouldReturnNotFoundForAssignToInvalidTask() throws Exception{
        String taskId = "1";
        Assignee validAssignee = aValidAssignee().build();
        when(taskOperationsService.assign(eq(taskId), any(Assignee.class))).thenThrow(new ResourceNotFoundError("not found"));

        this.mockMvc.perform(post(getFetchUrl(taskId) + "/assign-to")
                .contentType(MediaType.APPLICATION_JSON).content(toJson(validAssignee)))
                .andExpect(status().isNotFound());

        verify(taskOperationsService).assign(eq(taskId), any(Assignee.class));
    }

    @Test
    public void shouldReturnBadRequestForInvalidAssignmentToTask() throws Exception{
        String taskId = "1";
        Assignee invalidAssignee = anInvalidAssignee().build();
        when(taskOperationsService.assign(eq(taskId), any(Assignee.class))).thenThrow(new ValidationError("assignee not valid"));

        this.mockMvc.perform(post(getFetchUrl(taskId) + "/assign-to")
                .contentType(MediaType.APPLICATION_JSON).content(toJson(invalidAssignee)))
                .andExpect(status().isBadRequest());

        verify(taskOperationsService).assign(eq(taskId), any(Assignee.class));
    }
}
