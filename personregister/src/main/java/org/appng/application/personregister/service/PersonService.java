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

import java.util.List;

import org.appng.api.FieldProcessor;
import org.appng.api.Request;
import org.appng.application.personregister.business.PersonFilter;
import org.appng.application.personregister.model.Person;
import org.appng.application.personregister.repository.PersonRepository;
import org.appng.persistence.repository.SearchQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PersonService {

	private PersonRepository personRepository;

	@Autowired
	public PersonService(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	@Transactional(readOnly = true)
	public Page<Person> getPersons(Pageable p, PersonFilter filter) {
		SearchQuery<Person> query = personRepository.createSearchQuery();
		query.startsWith("name", filter.getFName()).startsWith("lastname", filter.getLName());
		query.equals("gender", filter.getFGender()).greaterThan("birthDay", filter.getFBornAfter());

		return personRepository.search(query, p);
	}

	public List<Person> getPersonsByLastNameLikeOrNameLike(String name) {
		return personRepository.findByNameOrLastNameLike(name);
	}

	public Person getPerson(Integer id) {
		return personRepository.findOne(id);
	}

	public Person updatePerson(Person p, Request request, FieldProcessor fp) {
		Person current = getPerson(p.getId());
		request.setPropertyValues(p, current, fp.getMetaData());
		return current;
	}

	public Person createPerson(Person p) {
		return personRepository.save(p);
	}

	public Person updatePersonManually(Integer id, Person person) {
		Person current = getPerson(id);
		current.setName(person.getName());
		current.setLastname(person.getLastname());
		return current;
	}

	public boolean deletePerson(Integer id) {
		Person person = getPerson(id);
		if (null == person) {
			return false;
		}
		personRepository.delete(person);
		return true;
	}

	public List<Person> getAllPersons() {
		return personRepository.findAll();
	}

	public List<Person> personsWithOutImage() {
		return personRepository.findByPictureDataIsNull();
	}

}
