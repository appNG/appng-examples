package org.appng.application.example.camunda.business;

import org.appng.api.ActionProvider;
import org.appng.api.DataContainer;
import org.appng.api.DataProvider;
import org.appng.api.Environment;
import org.appng.api.FieldProcessor;
import org.appng.api.Options;
import org.appng.api.Request;
import org.appng.api.model.Application;
import org.appng.api.model.Site;
import org.appng.application.example.camunda.domain.Document;
import org.appng.application.example.camunda.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentUpload implements ActionProvider<Document>, DataProvider {

	private DocumentService service;

	@Autowired
	public DocumentUpload(DocumentService service) {
		this.service = service;
	}

	public DataContainer getData(Site site, Application application, Environment environment, Options options,
			Request request, FieldProcessor fieldProcessor) {
		DataContainer dataContainer = new DataContainer(fieldProcessor);
		dataContainer.setItem(new Document());
		return dataContainer;
	}

	public void perform(Site site, Application application, Environment environment, Options options, Request request,
			Document upload, FieldProcessor fp) {
		upload.setInitiator(environment.getSubject().getName());
		service.saveDocument(upload, fp);
	}

}
