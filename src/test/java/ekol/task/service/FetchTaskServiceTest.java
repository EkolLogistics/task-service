package ekol.task.service;

import ekol.task.builder.Builder;
import ekol.task.domain.exception.ResourceNotFoundError;
import ekol.task.repository.TaskRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FetchTaskServiceTest {

    @MockBean
    private TaskRepository taskRepository;

    @Autowired
    private FetchTaskService fetchTaskService;

    @Test
    public void shouldFetchWithId(){
        String id = "1";
        when(taskRepository.findOne(id)).thenReturn(Builder.aValidTask().but().withId(id).build());
        when(taskRepository.exists(id)).thenReturn(true);

        fetchTaskService.fetch(id);

        verify(taskRepository).findOne(id);
    }

    @Test(expected = ResourceNotFoundError.class)
    public void shouldThrowResourceNotFoundWithNotExistingId(){
        String id = "1";
        when(taskRepository.exists(id)).thenReturn(false);

        fetchTaskService.fetch(id);
    }

    @Test
    public void shouldFindAll(){
        int page = 1, size = 1;
        when(taskRepository.findAll(any(PageRequest.class))).thenReturn(null);

        fetchTaskService.findAll(page, size);

        ArgumentCaptor<PageRequest> captor = ArgumentCaptor.forClass(PageRequest.class);
        verify(taskRepository).findAll(captor.capture());

        assertThat(captor.getValue().getPageNumber(), equalTo(page));
        assertThat(captor.getValue().getPageSize(), equalTo(size));
    }

    @Test
    public void shouldNotAllowInvalidPageRequest(){
        int page = -1, size = -1;

        fetchTaskService.findAll(page, size);

        ArgumentCaptor<PageRequest> captor = ArgumentCaptor.forClass(PageRequest.class);
        verify(taskRepository).findAll(captor.capture());
        assertThat(captor.getValue().getPageNumber(), greaterThan(0));
        assertThat(captor.getValue().getPageSize(), greaterThan(0));
    }
}
