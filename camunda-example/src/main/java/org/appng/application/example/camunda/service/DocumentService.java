package org.appng.application.example.camunda.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.appng.api.FieldProcessor;
import org.appng.application.example.camunda.domain.Document;
import org.appng.application.example.camunda.domain.Document.State;
import org.appng.application.example.camunda.repository.DocumentRepository;
import org.appng.camunda.bpm.TaskWrapper;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.IdentityLink;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DocumentService {

	private DocumentRepository documentRepository;
	private RuntimeService runtimeService;
	private TaskService taskService;

	@Autowired
	public DocumentService(DocumentRepository attachmentRepository, RuntimeService runtimeService,
			TaskService taskService) {
		this.documentRepository = attachmentRepository;
		this.runtimeService = runtimeService;
		this.taskService = taskService;
	}

	public void saveDocument(Document upload, FieldProcessor fp) {
		upload.setData(upload.getUpload().getBytes());
		upload.setState(State.NEW);
		documentRepository.save(upload);
		fp.addOkMessage("Document has been uploaded!");
	}

	public void startProcess() {
		List<Document> newOnes = documentRepository.findByState(State.NEW);
		for (Document a : newOnes) {
			Map<String, Object> variables = new HashMap<>();
			variables.put("id", a.getId());
			runtimeService.startProcessInstanceByKey("Document_Upload_Review", variables);
		}
	}

	public Document getDocumentById(Integer id) {
		return documentRepository.findOne(id);
	}

	public TaskWrapper getTask(String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).initializeFormKeys().singleResult();
		if (null == task) {
			return null;
		}
		List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(task.getId());
		VariableMap variables = getTaskVariables(taskId);
		TaskWrapper taskWrapper = new TaskWrapper(task, identityLinks, variables);
		return taskWrapper;
	}

	public void saveDocument(Document a) {
		documentRepository.save(a);
	}

	public List<TaskWrapper> getTasksByAssignee(String assignee) {
		List<TaskWrapper> result = new ArrayList<>();
		TaskQuery tq = taskService.createTaskQuery();
		tq.taskAssignee(assignee);
		List<Task> tasks = tq.list();
		for (Task task : tasks) {
			TaskWrapper taskWrapper = getTaskWrapper(task);
			Integer id = taskWrapper.getVariables().getValue("id", Integer.class);
			if (id != null && StringUtils.isBlank(task.getDescription())) {
				Document attachment = getDocumentById(id);
				task.setDescription(attachment.getName());
				taskService.saveTask(task);

			}
			result.add(taskWrapper);
		}
		return result;
	}

	private TaskWrapper getTaskWrapper(Task task) {
		List<IdentityLink> identityLinks = getIdentitiyLinks(task.getId());
		VariableMap variables = getTaskVariables(task.getId());
		TaskWrapper taskWrapper = new TaskWrapper(task, identityLinks, variables);
		return taskWrapper;
	}

	public VariableMap getTaskVariables(String taskId) {
		return taskService.getVariablesTyped(taskId);
	}

	private List<IdentityLink> getIdentitiyLinks(String taskId) {
		return taskService.getIdentityLinksForTask(taskId);
	}

	public List<TaskWrapper> getAllTasks() {
		List<TaskWrapper> result = new ArrayList<>();
		for (Task task : taskService.createTaskQuery().list()) {
			TaskWrapper taskWrapper = getTaskWrapper(task);
			result.add(taskWrapper);
		}
		return result;
	}

	public void setDocumentState(Integer documentId, State state) {
		getDocumentById(documentId).setState(state);

	}
}
