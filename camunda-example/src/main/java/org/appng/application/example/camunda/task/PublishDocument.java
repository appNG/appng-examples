package org.appng.application.example.camunda.task;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Service Task "Publish Document"
 */
@Slf4j
@Service
public class PublishDocument implements JavaDelegate {

	public void execute(DelegateExecution delegate) throws Exception {
		log.info("Called Task 'Publish Document'");
		FlowElement flowElement = delegate.getBpmnModelElementInstance();
		ServiceTask task = (ServiceTask) flowElement;
	}

}