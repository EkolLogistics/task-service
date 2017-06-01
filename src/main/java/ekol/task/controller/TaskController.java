package ekol.task.controller;

import ekol.task.domain.Assignee;
import ekol.task.domain.Task;
import ekol.task.service.CreateTaskService;
import ekol.task.service.FetchTaskService;
import ekol.task.service.TaskOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private CreateTaskService createTaskService;

    @Autowired
    private TaskOperationsService taskOperationsService;

    @Autowired
    private FetchTaskService fetchTaskService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Task create(@RequestBody Task task){
        return createTaskService.create(task);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<Task> create(@RequestParam(name = "page", required = false) Integer page,
                             @RequestParam(name = "size", required = false) Integer size){
        return fetchTaskService.findAll(page, size);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Task get(@PathVariable String id){
        return fetchTaskService.fetch(id);
    }

    @RequestMapping(value = "/with-template/{templateId}", method = RequestMethod.POST)
    public Task createWithTemplate(@PathVariable String templateId){
        return createTaskService.createWithTemplate(templateId);
    }

    @RequestMapping(value = "/{id}/assign-to", method = RequestMethod.POST)
    public Task createWithTemplate(@PathVariable String id, @RequestBody Assignee assignee){
        return taskOperationsService.assign(id, assignee);
    }

}
