package ekol.task.service;

import ekol.task.domain.Task;
import ekol.task.domain.TaskStatus;
import ekol.task.domain.validator.TaskValidator;
import ekol.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Created by kilimci on 30/05/2017.
 */
@Service
public class CreateTaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskValidator taskValidator;

    public Task create(Task newTask){
        newTask.setStatus(TaskStatus.NEW);
        newTask.setCreatedAt(ZonedDateTime.now(ZoneId.of("UTC")));

        taskValidator.validateNewTask(newTask);
        return taskRepository.save(newTask);
    }
}
