package org.appng.application.example.camunda.business;

import org.apache.commons.io.FilenameUtils;
import org.appng.api.AttachmentWebservice;
import org.appng.api.BusinessException;
import org.appng.api.Environment;
import org.appng.api.Request;
import org.appng.api.model.Application;
import org.appng.api.model.Site;
import org.appng.application.example.camunda.domain.Document;
import org.appng.application.example.camunda.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import com.google.common.net.MediaType;

@Lazy
@Service
@RequestScope
public class Download implements AttachmentWebservice {

	@Autowired
	private DocumentService service;
	private String fileName;
	private String contentType;

	public byte[] processRequest(Site site, Application application, Environment environment, Request request)
			throws BusinessException {
		Integer id = Integer.valueOf(request.getParameter("id"));
		Document document = service.getDocumentById(id);
		this.fileName = document.getName();
		String extension = FilenameUtils.getExtension(fileName);
		if ("txt".equals(extension)) {
			this.contentType = MediaType.PLAIN_TEXT_UTF_8.toString();
		}
		return document.getData();
	}

	public String getContentType() {
		return contentType;
	}

	public String getFileName() {
		return fileName;
	}

	public boolean isAttachment() {
		return true;
	}

}
