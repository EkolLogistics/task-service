package ekol.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ekol.task.domain.TaskTemplate;
import ekol.task.domain.exception.ResourceNotFoundError;
import ekol.task.domain.exception.ValidationError;
import ekol.task.service.CreateTaskTemplateService;
import ekol.task.service.FetchTaskTemplateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static ekol.task.builder.Builder.aValidTemplate;
import static ekol.task.builder.Builder.anInvalidTemplate;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskTemplateController.class)
public class TaskTemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateTaskTemplateService createTaskTemplateService;

    @MockBean
    private FetchTaskTemplateService fetchTaskTemplateService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String ENDPOINT_URL = "/templates";

    private String toJson(TaskTemplate template) throws Exception{
        return objectMapper.writeValueAsString(template);
    }

    private String getFetchUrl(String id){
        return ENDPOINT_URL + "/" + id;
    }

    @Test
    public void shouldReturnOKForValidTemplate() throws Exception{
        TaskTemplate validTemplate = aValidTemplate().build();
        String id = "1";
        when(createTaskTemplateService.create(any(TaskTemplate.class))).thenReturn(aValidTemplate().but().withId(id).build());

        this.mockMvc.perform(post(ENDPOINT_URL)
                .contentType(MediaType.APPLICATION_JSON).content(toJson(validTemplate))).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("name").value(validTemplate.getName()));

        verify(createTaskTemplateService).create(any(TaskTemplate.class));
    }

    @Test
    public void shouldReturnBadRequestForInvalidTask() throws Exception{
        TaskTemplate invalidTemplate = anInvalidTemplate().build();
        when(createTaskTemplateService.create(any(TaskTemplate.class))).thenThrow(new ValidationError("not valid"));

        this.mockMvc.perform(post(ENDPOINT_URL)
                .contentType(MediaType.APPLICATION_JSON).content(toJson(invalidTemplate)))
                .andExpect(status().isBadRequest());

        verify(createTaskTemplateService).create(any(TaskTemplate.class));
    }

    @Test
    public void shouldReturnOKForFetchValidId() throws Exception{
        String id = "1";
        TaskTemplate validTemplate = aValidTemplate().withId(id).build();
        when(fetchTaskTemplateService.fetch(id)).thenReturn(validTemplate);

        this.mockMvc.perform(get(getFetchUrl(id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(validTemplate.getId()))
                .andExpect(jsonPath("name").value(validTemplate.getName()));

        verify(fetchTaskTemplateService).fetch(id);
    }

    @Test
    public void shouldReturnNotFoundForFetchInvalidId() throws Exception{
        String id = "1";
        when(fetchTaskTemplateService.fetch(id)).thenThrow(new ResourceNotFoundError("not found"));

        this.mockMvc.perform(get(getFetchUrl(id))).andExpect(status().isNotFound());

        verify(fetchTaskTemplateService).fetch(id);
    }
}
