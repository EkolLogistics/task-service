package ekol.task.service;

import ekol.task.domain.exception.ResourceNotFoundError;
import ekol.task.repository.TaskTemplateRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static ekol.task.builder.Builder.aValidTemplate;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FetchTaskTemplateServiceTest {

    @MockBean
    private TaskTemplateRepository taskTemplateRepository;

    @Autowired
    private FetchTaskTemplateService fetchTaskTemplateService;

    @Test
    public void shouldFetchWithId(){
        String id = "1";
        when(taskTemplateRepository.findOne(id)).thenReturn(aValidTemplate().but().withId(id).build());
        when(taskTemplateRepository.exists(id)).thenReturn(true);

        fetchTaskTemplateService.fetch(id);

        verify(taskTemplateRepository).findOne(id);
    }

    @Test(expected = ResourceNotFoundError.class)
    public void shouldThrowResourceNotFoundWithNotExistingId(){
        String id = "1";
        when(taskTemplateRepository.exists(id)).thenReturn(false);

        fetchTaskTemplateService.fetch(id);
    }
}
