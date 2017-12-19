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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.store.Directory;
import org.appng.api.Environment;
import org.appng.api.model.Application;
import org.appng.api.model.Site;
import org.appng.api.search.Document;
import org.appng.application.personregister.model.Person;
import org.appng.application.personregister.service.PersonService;
import org.appng.search.SearchProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A {@link SearchProvider} adds {@link Document}s at the time of searching. 
 */
@Service
public class PersonSearchProvider implements SearchProvider {

	private PersonService service;
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																										
	@Autowired
	public PersonSearchProvider(PersonService service) {
		this.service = service;
	}

	public Iterable<Document> doSearch(Environment env, Site site, Application application, Directory directory,
			String term, String language, String[] parseFields, Analyzer analyzer, String highlightWith,
			Map<String, String> parameters) throws IOException {
		List<Document> documents = new ArrayList<>();
		
		if("en".equalsIgnoreCase(language) && analyzer instanceof EnglishAnalyzer){
			List<String> fieldList = Arrays.asList(parseFields);
			if(fieldList.contains(Document.FIELD_TITLE)){
				List<Person> persons = service.getPersonsByLastNameLikeOrNameLike(term);
				persons.forEach(p -> documents.add(p.toDocument(site, application)));
			}
		}

		return documents;
	}

}
