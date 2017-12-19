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
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.appng.api.AttachmentWebservice;
import org.appng.api.BusinessException;
import org.appng.api.Environment;
import org.appng.api.PermissionProcessor;
import org.appng.api.Request;
import org.appng.api.model.Application;
import org.appng.api.model.FeatureProvider;
import org.appng.api.model.Site;
import org.appng.application.personregister.model.Person;
import org.appng.application.personregister.service.PersonService;
import org.appng.tools.image.ImageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

@Lazy
@RequestScope
@Service
public class PictureService implements AttachmentWebservice {

	private PersonService service;
	private String contentType;
	private HttpStatus status = HttpStatus.OK;

	@Autowired
	public PictureService(PersonService service) {
		this.service = service;
	}

	public byte[] processRequest(Site site, Application application, Environment environment, Request request)
			throws BusinessException {
		PermissionProcessor permissionProcessor = request.getPermissionProcessor();
		if (!permissionProcessor.hasPermission("picture.download")) {
			status = HttpStatus.NOT_FOUND;
			return null;
		}
		try {
			Integer personId = request.convert(request.getParameter("id"), Integer.class);
			Person person = service.getPerson(personId);
			FeatureProvider featureProvider = application.getFeatureProvider();
			File imageCache = featureProvider.getImageCache();
			File sourceFile = new File(imageCache, "image-" + personId);
			FileUtils.writeByteArrayToFile(sourceFile, person.getPictureData());
			String resizedName = sourceFile.getName() + "resized";
			ImageProcessor imageProcessor = featureProvider.getImageProcessor(sourceFile, resizedName);
			Integer imageSize = application.getProperties().getInteger("imageSize", 120);

			File result = imageProcessor.resize(imageSize, imageSize).quality(0.8).getImage();

			this.contentType = person.getPictureContentType();
			return FileUtils.readFileToByteArray(result);
		} catch (IOException e) {
			throw new BusinessException(e);
		}
	}

	public String getContentType() {
		return contentType;
	}

	public String getFileName() {
		return null;
	}

	public boolean isAttachment() {
		return true;
	}

	@Override
	public int getStatus() {
		return status.value();
	}

}
