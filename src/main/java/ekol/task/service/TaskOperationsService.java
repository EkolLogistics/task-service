package ekol.task.service;

import ekol.task.domain.Assignee;
import ekol.task.domain.Assignment;
import ekol.task.domain.Task;
import ekol.task.domain.exception.ValidationError;
import ekol.task.domain.validator.AssignmentValidator;
import ekol.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskOperationsService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AssignmentValidator assignmentValidator;

    @Autowired
    private FetchTaskService fetchTaskService;

    public Task assign(String taskId, Assignee assignee){
        Task task = fetchTaskService.fetch(taskId);
        return assign(task, assignee);
    }

    public Task assign(Task task, Assignee assignee){
        if(task.getAssignment() != null){
            throw new ValidationError("This task is already assigned to another assignee");
        }
        Assignment assignment = Assignment.withAssignee(assignee);
        assignmentValidator.validate(assignment);
        task.setAssignment(assignment);
        return taskRepository.save(task);
    }
}
