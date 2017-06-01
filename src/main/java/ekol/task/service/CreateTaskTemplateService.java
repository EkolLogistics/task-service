package ekol.task.service;

import ekol.task.domain.TaskTemplate;
import ekol.task.domain.validator.TaskTemplateValidator;
import ekol.task.repository.TaskTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateTaskTemplateService {

    @Autowired
    private TaskTemplateValidator taskTemplateValidator;

    @Autowired
    private TaskTemplateRepository taskTemplateRepository;

    public TaskTemplate create(TaskTemplate validTemplate) {
        taskTemplateValidator.validateNew(validTemplate);
        return taskTemplateRepository.save(validTemplate);
    }
}
