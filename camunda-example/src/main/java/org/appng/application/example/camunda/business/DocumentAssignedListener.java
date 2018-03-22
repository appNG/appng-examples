package org.appng.application.example.camunda.business;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DocumentAssignedListener implements TaskListener, ExecutionListener {

	public void notify(DelegateTask delegateTask) {
		log.info(delegateTask.getName() + " " + delegateTask.getEventName());
		switch (delegateTask.getEventName()) {
		case EVENTNAME_CREATE:

			break;

		case EVENTNAME_COMPLETE:

			break;

		case EVENTNAME_ASSIGNMENT:
			log.debug("{} has been assigned to {}", delegateTask.getName(), delegateTask.getAssignee());
			break;

		case EVENTNAME_DELETE:

			break;

		}

	}

	public void notify(DelegateExecution execution) throws Exception {
		log.info(execution.getEventName());
		switch (execution.getEventName()) {
		case EVENTNAME_START:

			break;

		case EVENTNAME_TAKE:

			break;

		case EVENTNAME_END:

			break;

		}

	}

}
