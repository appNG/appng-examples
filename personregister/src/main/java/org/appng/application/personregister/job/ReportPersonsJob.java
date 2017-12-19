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
package org.appng.application.personregister.job;

import java.util.List;
import java.util.Map;

import org.appng.api.ScheduledJob;
import org.appng.api.model.Application;
import org.appng.api.model.Site;
import org.appng.application.personregister.model.Person;
import org.appng.application.personregister.service.PersonService;
import org.appng.mail.Mail;
import org.appng.mail.Mail.RecipientType;
import org.appng.mail.MailTransport;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ReportPersonsJob implements ScheduledJob {
	private String description;
	private Map<String, Object> jobDataMap;
	private PersonService service;
	private MailTransport mailTransport;

	@Autowired
	public ReportPersonsJob(PersonService service, MailTransport mailTransport) {
		this.service = service;
		this.mailTransport = mailTransport;
	}

	public void execute(Site site, Application application) throws Exception {
		List<Person> personsWithOutImage = service.personsWithOutImage();
		int size = personsWithOutImage.size();
		log.debug("found {} persons without a picture", size);
		Mail mail = mailTransport.createMail();
		String sender = (String) jobDataMap.get("sender");
		String subject = (String) jobDataMap.get("subject");
		String receiver = (String) jobDataMap.get("receiver");
		String content = new StringBuilder().append("There are ").append(size).append(" persons without a picture.")
				.toString();

		mail.setFrom(sender);
		mail.setSubject(subject);
		mail.addReceiver(receiver, RecipientType.TO);
		mail.setTextContent(content);
		mailTransport.send(mail);

	}

}
