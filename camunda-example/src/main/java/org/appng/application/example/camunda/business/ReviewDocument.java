package org.appng.application.example.camunda.business;

import java.util.HashMap;
import java.util.Map;

import org.appng.api.ActionProvider;
import org.appng.api.Environment;
import org.appng.api.FieldProcessor;
import org.appng.api.FormValidator;
import org.appng.api.Options;
import org.appng.api.Request;
import org.appng.api.model.Application;
import org.appng.api.model.Site;
import org.appng.application.example.camunda.service.DocumentService;
import org.appng.camunda.bpm.TaskWrapper;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * The user task "Review Document"
 */
@Slf4j
@Service
public class ReviewDocument implements ActionProvider<TaskWrapper>, FormValidator {

	private TaskService taskService;
	private FormService formService;
	private DocumentService service;

	@Autowired
	public ReviewDocument(DocumentService service, TaskService taskService, FormService formService) {
		this.taskService = taskService;
		this.formService = formService;
		this.service = service;
	}

	public void perform(Site site, Application application, Environment environment, Options options, Request request,
			TaskWrapper formBean, FieldProcessor fieldProcessor) {
		log.debug("Review Document");
		String taskId = options.getString("task", "id");

		// handle additional form fields
		Map<String, Object> formFields = formBean.getFormFields();

		Map<String, Object> variables = new HashMap<>();
		variables.put("assignee", formBean.getAssignee());
		taskService.complete(taskId, variables);
		fieldProcessor.addOkMessage("Task " + taskId + " completed!");
	}

	public void validate(Site site, Application application, Environment environment, Options options, Request request,
			FieldProcessor fp) {
		String taskId = options.getString("task", "id");
		TaskWrapper task = service.getTask(taskId);
		TaskFormData taskFormData = formService.getTaskFormData(taskId);
		// set custom form fields from request before calling task.validate()
		for (FormField field : taskFormData.getFormFields()) {
			String parameter = request.getParameter("formFields['" + field.getId() + "']");
			task.getFormFields().put(field.getId(), parameter);
		}
		task.validate(site, application, environment, options, request, fp, taskFormData, "Mandatory!");
	}

}
