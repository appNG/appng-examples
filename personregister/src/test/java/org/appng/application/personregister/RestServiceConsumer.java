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
package org.appng.application.personregister;

import java.net.URI;

import org.appng.application.personregister.model.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RestServiceConsumer {

	public static void main(String[] args) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		MappingJackson2HttpMessageConverter conv = new MappingJackson2HttpMessageConverter();
		restTemplate.getMessageConverters().add(conv);

		URI url = new URI("http://localhost:8080/service/manager/personregistry/rest/person/1");
		ResponseEntity<Person> response = restTemplate.getForEntity(url, Person.class);
		System.err.println(response);
		Person pers = response.getBody();
		System.err.println(pers);

	}

}
