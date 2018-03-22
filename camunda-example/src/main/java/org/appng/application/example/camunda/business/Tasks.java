package org.appng.application.example.camunda.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.appng.api.DataContainer;
import org.appng.api.DataProvider;
import org.appng.api.Environment;
import org.appng.api.FieldProcessor;
import org.appng.api.Options;
import org.appng.api.Request;
import org.appng.api.model.Application;
import org.appng.api.model.Group;
import org.appng.api.model.Role;
import org.appng.api.model.Site;
import org.appng.api.support.SelectionFactory;
import org.appng.application.example.camunda.service.DocumentService;
import org.appng.camunda.bpm.TaskWrapper;
import org.appng.xml.platform.Selection;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * A {@link DataProvider} showing a list of all {@link Task}s, wrapped as {@link TaskWrapper}.
 */
@Slf4j
@Service
public class Tasks implements DataProvider {

	private static final String EDITOR = "Editor";
	private static final String REVIEWER = "Reviewer";
	private FormService formService;
	private DocumentService service;
	private @Autowired SelectionFactory selectionFactory;

	@Autowired
	public Tasks(DocumentService service, FormService formService) {
		this.service = service;
		this.formService = formService;
	}

	public DataContainer getData(Site site, Application application, Environment environment, Options options,
			Request request, FieldProcessor fp) {
		DataContainer dataContainer = new DataContainer(fp);
		String taskId = options.getString("task", "id");
		if (null == taskId) {
			log.debug("No Task ID given");
			List<TaskWrapper> list;
			if (request.getPermissionProcessor().hasPermission("tasks.showAll")) {
				list = service.getAllTasks();
			} else {
				boolean isReviewer = hasRole(environment, application, REVIEWER);
				boolean isEditor = hasRole(environment, application, EDITOR);
				if (isReviewer) {
					list = service.getTasksByAssignee(REVIEWER);
				} else if (isEditor) {
					list = service.getTasksByAssignee(EDITOR);
				} else {
					list = new ArrayList<>();
				}
			}
			dataContainer.setPage(list, fp.getPageable());
		} else {
			log.debug("retrieving task %s", taskId);
			TaskWrapper task = service.getTask(taskId);
			TaskFormData taskFormData = formService.getTaskFormData(taskId);
			task.addFormFields(fp, taskFormData, "Mandatory!");

			List<String> roleNames = new ArrayList<>();
			Set<Role> roles = application.getRoles();
			roles.forEach(f -> roleNames.add(f.getName()));
			Selection assignees = selectionFactory.fromObjects("assignee", "assignee", roleNames.toArray(),
					task.getAssignee());
			dataContainer.getSelections().add(assignees);
			dataContainer.setItem(task);
		}
		return dataContainer;
	}

	private boolean hasRole(Environment environment, Application application, String roleName) {
		for (Group group : environment.getSubject().getGroups()) {
			for (Role role : group.getRoles()) {
				if (roleName.equals(role.getName()) && application.getName().equals(role.getApplication().getName())) {
					return true;
				}
			}
		}
		return false;
	}

}
