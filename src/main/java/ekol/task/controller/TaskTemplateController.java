package ekol.task.controller;

import ekol.task.domain.TaskTemplate;
import ekol.task.service.CreateTaskTemplateService;
import ekol.task.service.FetchTaskTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/templates")
public class TaskTemplateController {

    @Autowired
    private CreateTaskTemplateService createTaskTemplateService;

    @Autowired
    private FetchTaskTemplateService fetchTaskTemplateService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public TaskTemplate create(@RequestBody TaskTemplate template){
        return createTaskTemplateService.create(template);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TaskTemplate get(@PathVariable String id){
        return fetchTaskTemplateService.fetch(id);
    }
}
