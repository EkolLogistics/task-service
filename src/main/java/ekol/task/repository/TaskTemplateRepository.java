package ekol.task.repository;

import ekol.task.domain.TaskTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskTemplateRepository extends MongoRepository<TaskTemplate, String> {

}
