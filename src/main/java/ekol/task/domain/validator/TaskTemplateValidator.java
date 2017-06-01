package ekol.task.domain.validator;

import ekol.task.domain.TaskTemplate;
import ekol.task.domain.exception.ValidationError;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class TaskTemplateValidator {

    public void validateNew(TaskTemplate taskTemplate){
        if(StringUtils.isBlank(taskTemplate.getName())){
            throw new ValidationError("New template should have a name");
        }
        if(taskTemplate.getDuration() == null){
            throw new ValidationError("New template should have a duration");
        }
    }
}
