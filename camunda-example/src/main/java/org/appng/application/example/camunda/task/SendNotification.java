package org.appng.application.example.camunda.task;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

import org.appng.application.example.camunda.domain.Document;
import org.appng.application.example.camunda.service.DocumentService;
import org.appng.mail.Mail;
import org.appng.mail.Mail.RecipientType;
import org.appng.mail.MailTransport;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Send Task "Send Notifications"
 *
 */
@Service
@Data
@Slf4j
public class SendNotification implements JavaDelegate {

	private Expression allowedExtensions;
	private MailTransport mailTransport;
	private DocumentService service;
	private RuntimeService runtimeService;

	@Autowired
	public SendNotification(DocumentService service, MailTransport mailTransport, RuntimeService runtimeService) {
		this.service = service;
		this.mailTransport = mailTransport;
		this.runtimeService = runtimeService;
	}

	public void execute(DelegateExecution execution) throws Exception {
		log.info("Called Task 'Send Notifications'");
		VariableMap variables = execution.getVariablesTyped();
		Integer id = variables.getValue("id", Integer.class);
		Document d = service.getDocumentById(id);
		Mail mail = mailTransport.createMail();
		String contentType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(d.getName());
		mail.addAttachment(new ByteArrayInputStream(d.getData()), d.getName(), contentType);
		mail.setSubject(variables.getValue("Subject", String.class));
		mail.setFrom(variables.getValue("Sender", String.class));
		mail.setTextContent(variables.getValue("Content", String.class));
		@SuppressWarnings("unchecked")
		List<String> receivers = variables.getValue("Receiver", List.class);
		receivers.forEach(r -> mail.addReceiver(r, RecipientType.TO));
		mailTransport.send(mail);
	}

}