package ekol.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ekol.task.domain.Task;
import ekol.task.domain.exception.ResourceNotFoundError;
import ekol.task.domain.exception.ValidationError;
import ekol.task.service.CreateTaskService;
import ekol.task.service.FetchTaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static ekol.task.builder.Builder.aValidTask;
import static ekol.task.builder.Builder.anInvalidTask;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Autowired
    private ObjectMapper objectMapper;

    private static final String ENDPOINT_URL = "/tasks";

    private String getFetchUrl(String id){
        return ENDPOINT_URL + "/" + id;
    }

    private String toJson(Task task) throws Exception{
        return objectMapper.writeValueAsString(task);
    }

    @Test
    public void shouldReturnOKForValidTask() throws Exception{
        Task validTask = aValidTask().build();
        when(createTaskService.create(any(Task.class))).thenReturn(validTask);

        this.mockMvc.perform(post(ENDPOINT_URL)
                .contentType(MediaType.APPLICATION_JSON).content(toJson(validTask)))
                .andExpect(status().isOk());

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
        when(fetchTaskService.fetch(id)).thenReturn(aValidTask().build());

        this.mockMvc.perform(get(getFetchUrl(id))).andExpect(status().isOk());

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

        this.mockMvc.perform(get(ENDPOINT_URL).param("page","1").param("size","1")).andExpect(status().isOk());

        verify(fetchTaskService).findAll(1,1);
    }

    @Test
    public void shouldReturnOKForFindAllWithoutParams() throws Exception{
        when(fetchTaskService.findAll(null,null)).thenReturn(null);

        this.mockMvc.perform(get(ENDPOINT_URL)).andExpect(status().isOk());

        verify(fetchTaskService).findAll(null,null);
    }
}
