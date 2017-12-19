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

import org.appng.api.Environment;
import org.appng.application.personregister.business.PersonFilter;
import org.appng.application.personregister.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonRestService {

	private PersonService service;

	@Autowired
	public PersonRestService(PersonService service) {
		this.service = service;
	}

	@RequestMapping(value = "/person", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<Person>> getPersons(
			@RequestParam(name = "p", required = false, defaultValue = "0") int pageNo) {
		Page<Person> page = service.getPersons(new PageRequest(pageNo, 2), new PersonFilter());
		return new ResponseEntity<Page<Person>>(page, HttpStatus.OK);
	}

	@RequestMapping(value = "/person/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Person> getPerson(@PathVariable("id") Integer id, Environment environment) {
		Person person = service.getPerson(id);
		if (null != person) {
			return new ResponseEntity<Person>(person, HttpStatus.OK);
		}
		return new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/person/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Person> updatePerson(@PathVariable("id") Integer id, @RequestBody Person person) {
		Person current = service.getPerson(id);
		if (null != current) {
			Person updated = service.updatePersonManually(id, person);
			return new ResponseEntity<Person>(updated, HttpStatus.OK);
		}
		return new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/person/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> deletePerson(@PathVariable("id") Integer id) {
		if (service.deletePerson(id)) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/person", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Person> createPerson(@RequestBody Person person) {
		person.setId(null);
		Person current = service.createPerson(person);
		if (null != current) {
			return new ResponseEntity<Person>(person, HttpStatus.OK);
		}
		return new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
	}

}
