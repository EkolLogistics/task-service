package ekol.task.service;

import ekol.task.domain.Task;
import ekol.task.domain.TaskTemplate;
import ekol.task.domain.validator.TaskValidator;
import ekol.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateTaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private FetchTaskTemplateService fetchTaskTemplateService;

    @Autowired
    private TaskValidator taskValidator;

    public Task create(Task newTask){
        newTask.setStatusNew();
        newTask.setCreatedAtNow();
        taskValidator.validateNewTask(newTask);
        return taskRepository.save(newTask);
    }

    public Task createWithTemplate(String templateId){
        TaskTemplate template = fetchTaskTemplateService.fetch(templateId);
        Task newTask = Task.withTemplate(template);
        taskValidator.validateNewTask(newTask);
        return taskRepository.save(newTask);
    }
}
