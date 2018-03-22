package org.appng.application.example.camunda.business;

import org.appng.api.ActionProvider;
import org.appng.api.Environment;
import org.appng.api.FieldProcessor;
import org.appng.api.Options;
import org.appng.api.Request;
import org.appng.api.model.Application;
import org.appng.api.model.Site;
import org.appng.camunda.bpm.TaskWrapper;
import org.camunda.bpm.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * The user task "Approve Document" 
 */
@Slf4j
@Service
public class ApproveDocument implements ActionProvider<TaskWrapper> {

	private TaskService taskService;

	@Autowired
	public ApproveDocument(TaskService taskService) {
		this.taskService = taskService;
	}

	public void perform(Site site, Application application, Environment environment, Options options, Request request,
			TaskWrapper formBean, FieldProcessor fieldProcessor) {
		log.info("Called User Task 'Approve Document'");
		String taskId = options.getString("task", "id");
		taskService.complete(taskId);
		fieldProcessor.addOkMessage("Task " + taskId + " completed!");
	}

}
