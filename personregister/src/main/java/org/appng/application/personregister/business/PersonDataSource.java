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

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.appng.api.DataContainer;
import org.appng.api.DataProvider;
import org.appng.api.Environment;
import org.appng.api.FieldProcessor;
import org.appng.api.Options;
import org.appng.api.Request;
import org.appng.api.SiteProperties;
import org.appng.api.model.Application;
import org.appng.api.model.NameProvider;
import org.appng.api.model.Site;
import org.appng.api.support.SelectionFactory;
import org.appng.api.support.SelectionFactory.Selection;
import org.appng.application.personregister.MessageConstants;
import org.appng.application.personregister.model.Person;
import org.appng.application.personregister.model.Person.Gender;
import org.appng.application.personregister.service.PersonService;
import org.appng.xml.platform.SelectionGroup;
import org.appng.xml.platform.SelectionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class PersonDataSource implements DataProvider {

	private PersonService service;
	private SelectionFactory selectionFactory;

	@Autowired
	private PersonFilter filter;
	
	@Autowired
	public PersonDataSource(PersonService service, SelectionFactory selectionFactory) {
		this.service = service;
		this.selectionFactory = selectionFactory;
	}

	public DataContainer getData(Site site, Application application, Environment environment, Options options,
			Request request, FieldProcessor fieldProcessor) {
		DataContainer dataContainer = new DataContainer(fieldProcessor);
		Integer personId = options.getInteger("person", "id");
		NameProvider<Gender> nameProvider = new NameProvider<Gender>() {
			public String getName(Gender instance) {
				return request.getMessage(Gender.class.getSimpleName() + "." + instance.name());
			}
		};
		if (null == personId) {
			String fName = StringUtils.trimToNull(filter.getFName());
			Selection firstNameFilter = selectionFactory.getTextSelection("FName", "name", fName);
			String lName = StringUtils.trimToNull(filter.getLName());
			Selection lastNameFilter = selectionFactory.getTextSelection("LName", "lastname", lName);

			Gender fGender = filter.getFGender();
			Selection genderSelection = getGenders("FGender", nameProvider, fGender);
			genderSelection.setType(SelectionType.RADIO);

			String datePattern = request.getMessage(MessageConstants.DATE_PATTERN);
			FastDateFormat df = FastDateFormat.getInstance(datePattern);

			Date fBornAfter = filter.getFBornAfter();
			String format = null == fBornAfter ? null : df.format(fBornAfter);
			Selection birthDayFilter = selectionFactory.getTextSelection("FBornAfter", "bornAfter",
					format);
			birthDayFilter.setFormat(datePattern);
			birthDayFilter.setType(SelectionType.DATE);

			SelectionGroup filter = new SelectionGroup();
			filter.getSelections().add(lastNameFilter);
			filter.getSelections().add(firstNameFilter);
			filter.getSelections().add(birthDayFilter);
			filter.getSelections().add(genderSelection);
			dataContainer.getSelectionGroups().add(filter);

			Page<Person> persons = service.getPersons(fieldProcessor.getPageable(), this.filter);
			dataContainer.setPage(persons);
		} else if (Integer.valueOf(-1).equals(personId)) {
			Selection genderSelection = getGenders("gender", nameProvider, Gender.OTHER);
			dataContainer.getSelections().add(genderSelection);
			dataContainer.setItem(new Person());
		} else {
			Person person = service.getPerson(personId);
			if (null != person.getPictureData()) {
				String managerPath = site.getProperties().getString(SiteProperties.SERVICE_PATH);
				person.setPicturePath(String.format("%s/%s/%s/webservice/pictureService?id=%s", managerPath,
						site.getName(), application.getName(), personId));
			}

			Selection genderSelection = getGenders("gender", nameProvider, person.getGender());
			dataContainer.getSelections().add(genderSelection);

			dataContainer.setItem(person);
		}
		return dataContainer;
	}

	private Selection getGenders(String id, NameProvider<Gender> nameProvider, Gender value) {
		Selection genderSelection = selectionFactory.fromEnum(id, "gender", Gender.values(), value, nameProvider);
		return genderSelection;
	}
}
