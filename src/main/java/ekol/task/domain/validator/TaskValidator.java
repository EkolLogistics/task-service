package ekol.task.domain.validator;

import ekol.task.domain.Task;
import ekol.task.domain.TaskStatus;
import ekol.task.domain.exception.ValidationError;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class TaskValidator{

    public void validateNewTask(Task task) {

        if(StringUtils.isBlank(task.getName())){
            throw new ValidationError("New task should have a name");
        }
        if (!TaskStatus.NEW.equals(task.getStatus())){
            throw new ValidationError("New task status should be '{0}' ", TaskStatus.NEW);
        }
        if (task.getCreatedAt() == null){
            throw new ValidationError("New task created date should not be empty");
        }
    }
}
