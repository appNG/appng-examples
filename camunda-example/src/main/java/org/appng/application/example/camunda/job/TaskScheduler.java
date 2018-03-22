package org.appng.application.example.camunda.job;

import java.util.HashMap;
import java.util.Map;

import org.appng.api.ScheduledJob;
import org.appng.api.model.Application;
import org.appng.api.model.Site;
import org.appng.application.example.camunda.domain.Document;
import org.appng.application.example.camunda.service.DocumentService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Data;

/**
 * A job that runs every minute and checks for new {@link Document}s, starting a {@link ProcessInstance} if necessary.
 */
@Data
@Service
public class TaskScheduler implements ScheduledJob {

	private String description;
	private Map<String, Object> jobDataMap;
	private DocumentService service;

	@Autowired
	public TaskScheduler(DocumentService service) {
		this.service = service;
	}

	public void execute(Site site, Application application) throws Exception {
		service.startProcess();
	}

	@Override
	public Map<String, Object> getJobDataMap() {
		jobDataMap = new HashMap<>();
		jobDataMap.put("enabled", "true");
		jobDataMap.put("runOnce", "true");
		jobDataMap.put("cronExpression", "0 0/1 * 1/1 * ? *");
		return jobDataMap;
	}
}
