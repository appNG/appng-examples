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

import org.appng.testsupport.TestBase;
import org.junit.Ignore;
import org.springframework.test.context.ContextConfiguration;

@Ignore("can not be abtract, so ignore")
@ContextConfiguration(locations = { TestBase.TESTCONTEXT_JPA,
		"classpath:/beans-test.xml" }, initializers = PersonTestBase.class)
public class PersonTestBase extends TestBase {

	public PersonTestBase() {
		super();
		setEntityPackage("org.appng.application.personregister.model");
		setRepositoryBase("org.appng.application.personregister.repository");
	}
}
