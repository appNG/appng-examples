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
package org.appng.application.personregister.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.appng.application.personregister.model.Person;
import org.springframework.beans.factory.annotation.Autowired;

public class PersonRepositoryImpl implements PersonRepositoryCustom {

	private EntityManager entityManager;

	@Autowired
	public PersonRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	public List<Person> findByNameOrLastNameLike(String name) {
		Query query = entityManager.createQuery("from Person p where p.name like ?1 or p.lastname like ?1");
		return query.setParameter(1, "%" + name + "%").getResultList();
	}

}
