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

import java.io.IOException;

import org.appng.api.ProcessingException;
import org.appng.api.support.CallableAction;
import org.appng.application.personregister.model.Person;
import org.appng.testsupport.validation.WritingXmlValidator;
import org.junit.Test;

public class PersonActionTest extends PersonTestBase {

	static {
		WritingXmlValidator.writeXml = false;
	}

	@Test
	public void testEditPerson() throws ProcessingException, IOException {
		Person form = new Person("Luke the Duke", "Skywalker");
		CallableAction callableAction = getAction("personEvent", "editPerson").withParam(FORM_ACTION, "editPerson")
				.withParam("id", "3").getCallableAction(form);
		callableAction.perform();
		WritingXmlValidator.validateXml(callableAction.getAction(), "xml/PersonActionTest-testEditPerson.xml");
	}

	@Test
	public void testCreatePerson() throws ProcessingException, IOException {
		CallableAction callableAction = getAction("personEvent", "createPerson").withParam(FORM_ACTION, "createPerson")
				.getCallableAction(new Person("Darth", "Vader"));
		callableAction.perform();
		WritingXmlValidator.validateXml(callableAction.getAction(), "xml/PersonActionTest-testCreatePerson.xml");
	}
}
