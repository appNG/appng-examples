package org.appng.application.example.camunda.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.appng.api.AttachmentWebservice;
import org.appng.api.BusinessException;
import org.appng.api.DataContainer;
import org.appng.api.DataProvider;
import org.appng.api.Environment;
import org.appng.api.FieldProcessor;
import org.appng.api.Options;
import org.appng.api.Path;
import org.appng.api.Request;
import org.appng.api.Scope;
import org.appng.api.model.Application;
import org.appng.api.model.Site;
import org.appng.api.support.environment.EnvironmentKeys;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Processes implements DataProvider, AttachmentWebservice {

	private ProcessEngine processEngine;
	private ThreadLocal<String> filename = new ThreadLocal<>();

	@Autowired
	public Processes(ProcessEngine processEngine) {
		this.processEngine = processEngine;
	}

	public DataContainer getData(Site site, Application application, Environment environment, Options options,
			Request request, FieldProcessor fp) {
		DataContainer dataContainer = new DataContainer(fp);
		RepositoryService repositoryService = processEngine.getRepositoryService();
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
		dataContainer.setPage(list, fp.getPageable());
		return dataContainer;
	}

	public byte[] processRequest(Site site, Application application, Environment environment, Request request)
			throws BusinessException {
		if (!request.getPermissionProcessor().hasPermission("process.download")) {
			throw new BusinessException("not allowed!");
		}
		try {
			Path path = environment.getAttribute(Scope.REQUEST, EnvironmentKeys.PATH_INFO);
			String name = path.getElementAt(6);
			InputStream resourceAsStream = site.getSiteClassLoader().getResourceAsStream(name);
			filename.set(name);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			StreamUtils.copy(resourceAsStream, out);
			return out.toByteArray();
		} catch (IOException e) {
			throw new BusinessException(e);
		}
	}

	public String getContentType() {
		return MediaType.APPLICATION_XML_VALUE;
	}

	public String getFileName() {
		String name = filename.get();
		filename.remove();
		return name;
	}

	public boolean isAttachment() {
		return false;
	}
}
