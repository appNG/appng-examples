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

import java.io.File;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.appng.application.personregister.business.PictureService;
import org.appng.application.personregister.model.Person;
import org.appng.application.personregister.service.PersonService;
import org.appng.testsupport.TestBase;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;

// We do not want to use persistence here, so only use TestBase.TESTCONTEXT 
@ContextConfiguration(locations = {
		TestBase.TESTCONTEXT }, inheritLocations = false, initializers = PictureServiceTest.class)
public class PictureServiceTest extends TestBase {

	@Override
	protected Properties getProperties() {
		return new Properties();
	}

	@Test
	public void test() throws Exception {
		PersonService serviceMock = Mockito.mock(PersonService.class);
		PictureService pictureService = new PictureService(serviceMock);
		Person p = new Person();
		File original = new File("src/test/resources/appng.png");
		p.setPictureData(FileUtils.readFileToByteArray(original));
		Mockito.when(serviceMock.getPerson(1)).thenReturn(p);
		addParameter("id", "1");
		initParameters();
		byte[] processRequest = pictureService.processRequest(site, application, environment, request);
		Assert.assertEquals(10985, processRequest.length);
	}

}
