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

import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.appng.api.Environment;
import org.appng.api.FormProcessProvider;
import org.appng.api.model.Application;
import org.appng.api.model.Site;
import org.appng.application.personregister.service.PersonService;
import org.appng.formtags.Form;
import org.appng.formtags.FormElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PersonFormProvider implements FormProcessProvider {

	private PersonService service;

	@Autowired
	public PersonFormProvider(PersonService service) {
		this.service = service;
	}

	@Override
	public void onFormSuccess(Environment environment, Site site, Application application, Writer writer, Form form,
			Map<String, Object> properties) {
		List<FormElement> elements = form.getFormData().getElements();
		for (FormElement formElement : elements) {
			log.debug("{} : {}", formElement.getName(), formElement.getValue());
		}
	}

}
