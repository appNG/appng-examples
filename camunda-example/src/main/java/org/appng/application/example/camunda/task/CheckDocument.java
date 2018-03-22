package org.appng.application.example.camunda.task;

import java.util.Arrays;

import org.apache.commons.io.FilenameUtils;
import org.appng.application.example.camunda.domain.Document;
import org.appng.application.example.camunda.domain.Document.State;
import org.appng.application.example.camunda.service.DocumentService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Service Task "Check Document"
 */
@Service
@Data
@Slf4j
public class CheckDocument implements JavaDelegate {

	private Expression allowedExtensions;
	private DocumentService service;

	@Autowired
	public CheckDocument(DocumentService service) {
		this.service = service;
	}

	public void execute(DelegateExecution execution) throws Exception {
		log.info("Called Task 'Check Document'");
		boolean accepted = false;
		Integer id = execution.getVariablesTyped().getValue("id", Integer.class);
		String extensions = (String) allowedExtensions.getValue(null);
		log.info("CheckDocument invoked with allowedExtensions : {}, id: {}", extensions, id);
		Document d = service.getDocumentById(id);
		String extension = FilenameUtils.getExtension(d.getName());
		if (Arrays.asList(extensions.split(",")).contains(extension)) {
			d.setState(State.PENDING);
			service.saveDocument(d);
			accepted = true;
		} else {
			d.setState(State.DECLINED);
		}
		// set the variable used in the flow condition after the exclusive gateway
		execution.setVariable("accepted", accepted);
	}

}