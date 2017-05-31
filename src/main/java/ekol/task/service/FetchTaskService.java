package ekol.task.service;

import ekol.task.domain.Task;
import ekol.task.domain.exception.ResourceNotFoundError;
import ekol.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class FetchTaskService {

    @Autowired
    private TaskRepository taskRepository;

    private final static Integer DEFAULT_PAGE = 1;
    private final static Integer DEFAULT_SIZE = 20;

    public Task fetch(String id){
        if(!taskRepository.exists(id)){
            throw new ResourceNotFoundError("Task with id {0} does not exist", id);
        }
        return taskRepository.findOne(id);
    }

    private PageRequest createPageRequest(Integer page, Integer size){
        if(page == null || page <= 0){
            page = DEFAULT_PAGE;
        }
        if(size == null || size <= 0){
            size = DEFAULT_SIZE;
        }
        return new PageRequest(page, size);
    }


    public Page<Task> findAll(Integer page, Integer size){
        PageRequest pageRequest = createPageRequest(page, size);
        return taskRepository.findAll(pageRequest);
    }
}
