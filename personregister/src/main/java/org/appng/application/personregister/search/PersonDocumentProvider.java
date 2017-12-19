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
package org.appng.application.personregister.search;

import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.appng.api.model.Application;
import org.appng.api.model.Site;
import org.appng.api.search.Document;
import org.appng.api.search.DocumentEvent;
import org.appng.api.search.DocumentProducer;
import org.appng.application.personregister.model.Person;
import org.appng.application.personregister.service.PersonService;
import org.appng.search.DocumentProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A {@link DocumentProvider} provides a list of {@link DocumentProducer}s at
 * the time building the search index for the {@link Site}.
 */
@Service
public class PersonDocumentProvider implements DocumentProvider {

	private PersonService service;

	@Autowired
	public PersonDocumentProvider(PersonService service) {
		this.service = service;
	}

	public Iterable<DocumentProducer> getDocumentProducers(Site site, Application application)
			throws InterruptedException, TimeoutException {
		DocumentProducer documentProducer = new DocumentProducer(EnglishAnalyzer.class, application.getName());
		List<Person> all = service.getAllPersons();
		for (Person person : all) {
			Document document = person.toDocument(site, application);
			// the index has been cleared anyways, so we can use create here
			documentProducer.put(new DocumentEvent(document, Document.CREATE));
		}
		return java.util.Arrays.asList(documentProducer);
	}

}
