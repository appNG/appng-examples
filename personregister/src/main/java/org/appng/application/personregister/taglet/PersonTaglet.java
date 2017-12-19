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
package org.appng.application.personregister.taglet;

import java.util.List;
import java.util.Map;

import org.appng.api.Request;
import org.appng.api.Taglet;
import org.appng.api.model.Application;
import org.appng.api.model.Site;
import org.appng.application.personregister.model.Person;
import org.appng.application.personregister.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonTaglet implements Taglet {

	private PersonService service;

	@Autowired
	public PersonTaglet(PersonService service) {
		this.service = service;
	}

	@Override
	public String processTaglet(Site site, Application application, Request request,
			Map<String, String> tagletAttributes) {
		List<String> urlParameters = request.getUrlParameters();
		String greeting = tagletAttributes.get("greeting");
		if (0 == urlParameters.size()) {
			return greeting;
		}
		Person person = service.getPerson(Integer.valueOf(urlParameters.get(0)));
		return greeting + " " + person.getName() + " " + person.getLastname();
	}

}
