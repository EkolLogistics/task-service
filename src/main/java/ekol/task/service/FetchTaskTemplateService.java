package ekol.task.service;


import ekol.task.domain.TaskTemplate;
import ekol.task.domain.exception.ResourceNotFoundError;
import ekol.task.repository.TaskTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FetchTaskTemplateService {

    @Autowired
    private TaskTemplateRepository taskTemplateRepository;

    public TaskTemplate fetch(String id){
        if(!taskTemplateRepository.exists(id)){
            throw new ResourceNotFoundError("Template with id {0} does not exist", id);
        }
        return taskTemplateRepository.findOne(id);
    }
}
