/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.appng.application.personregister.business;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.appng.api.ActionProvider;
import org.appng.api.Environment;
import org.appng.api.FieldProcessor;
import org.appng.api.FormValidator;
import org.appng.api.Options;
import org.appng.api.Request;
import org.appng.api.model.Application;
import org.appng.api.model.Site;
import org.appng.api.observe.Observable.Event;
import org.appng.api.search.Consumer;
import org.appng.api.search.Document;
import org.appng.api.search.DocumentEvent;
import org.appng.api.search.DocumentProducer;
import org.appng.application.personregister.MessageConstants;
import org.appng.application.personregister.model.Person;
import org.appng.application.personregister.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Performs creating,updating and deleting of {@link Person}s. Also uses a
 * {@link DocumentProducer} to update the {@link Site}'s search index at
 * runtime.
 *
 */
@Slf4j
@Service
public class PersonAction implements ActionProvider<Person>, FormValidator {

	private static final String ACTION = "action";
	private PersonService service;

	@Autowired
	public PersonAction(PersonService service) {
		this.service = service;
	}

	enum Mode {
		CREATE, EDIT, DELETE;
	}

	@Override
	public void perform(Site site, Application application, Environment environment, Options options, Request request,
			Person formBean, FieldProcessor fp) {

		Document doc = null;
		Event event = null;

		Mode mode = options.getEnum(ACTION, "id", Mode.class);
		switch (mode) {
		case CREATE: {
			Person created = service.createPerson(formBean);
			String message = request.getMessage(MessageConstants.PERSON_CREATED);
			fp.addOkMessage(message);
			event = Document.CREATE;
			doc = created.toDocument(site, application);
			break;
		}
		case EDIT: {
			Integer personId = options.getInteger(ACTION, "person");
			formBean.setId(personId);
			Person updated = service.updatePerson(formBean, request, fp);
			String message = request.getMessage(MessageConstants.PERSON_EDITED);
			fp.addOkMessage(message);
			event = Document.UPDATE;
			doc = updated.toDocument(site, application);
			break;
		}
		case DELETE: {
			Integer personId = options.getInteger(ACTION, "person");
			boolean deleted = service.deletePerson(personId);
			if (deleted) {
				String message = request.getMessage(MessageConstants.PERSON_DELETED);
				fp.addOkMessage(message);
				event = Document.DELETE;
			} else {
				String message = request.getMessage(MessageConstants.PERSON_NOT_FOUND);
				fp.addErrorMessage(message);
			}
		}

		}

		// add a Document at runtime using a DocumentProducer
		if (null != event) {
			try {
				Consumer<DocumentEvent, DocumentProducer> indexer = application.getFeatureProvider().getIndexer();
				DocumentProducer documentProducer = new DocumentProducer(EnglishAnalyzer.class, application.getName());
				indexer.put(documentProducer);
				documentProducer.put(new DocumentEvent(doc, event));
			} catch (Exception e) {
				log.error("error adding document", e);
			}
		}

	}

	@Override
	public void validate(Site site, Application application, Environment environment, Options options, Request request,
			FieldProcessor fieldProcessor) {

	}

}
