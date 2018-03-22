package org.appng.application.example.camunda.business;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.appng.api.AttachmentWebservice;
import org.appng.api.BusinessException;
import org.appng.api.Environment;
import org.appng.api.Path;
import org.appng.api.Request;
import org.appng.api.Scope;
import org.appng.api.model.Application;
import org.appng.api.model.Site;
import org.appng.api.support.environment.EnvironmentKeys;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

@Service
public class ModelViewer implements AttachmentWebservice {

	private ThreadLocal<String> filename = new ThreadLocal<>();

	private Configuration cfg;

	public ModelViewer() {
		cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
		cfg.setDefaultEncoding("UTF-8");
		cfg.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "/");
	}

	public byte[] processRequest(Site site, Application application, Environment environment, Request request)
			throws BusinessException {
		if (!request.getPermissionProcessor().hasPermission("process.download")) {
			throw new BusinessException("not allowed!");
		}
		Path path = environment.getAttribute(Scope.REQUEST, EnvironmentKeys.PATH_INFO);
		String name = path.getElementAt(6);
		filename.set(name);

		Map<String, Object> input = new HashMap<String, Object>();
		input.put("site", site.getName());
		input.put("resource", name);

		try {
			StringWriter out = new StringWriter();
			cfg.getTemplate("viewer.ftl").process(input, out);
			return out.getBuffer().toString().getBytes(StandardCharsets.UTF_8);
		} catch (IOException | TemplateException e) {
			throw new BusinessException(e);
		}
	}

	public String getContentType() {
		return MediaType.TEXT_HTML_VALUE;
	}

	public String getFileName() {
		return filename.get().replace(".bpmn", ".html");
	}

	public boolean isAttachment() {
		return false;
	}
}
