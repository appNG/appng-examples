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
package org.appng.application.personregister.service;

import java.util.Date;

import org.appng.application.personregister.business.PersonTestBase;
import org.appng.application.personregister.model.Person;
import org.appng.application.personregister.service.PersonRestService;
import org.appng.testsupport.validation.WritingJsonValidator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PersonRestServiceTest extends PersonTestBase {

	@Autowired
	PersonRestService service;
	
	@Autowired
	ObjectMapper mapper;

	@Test
	public void test() throws Exception {
		ResponseEntity<Page<Person>> persons = service.getPersons(0);
		Page<Person> page = persons.getBody();
		page.forEach(p -> p.setVersion(new Date(1512055286373L)));
		String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(page);
		WritingJsonValidator.validate(json, "json/PersonRestServiceTest-persons.json");
	}

}
