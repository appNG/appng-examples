package org.appng.application.personregister;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.appng.api.ApplicationController;
import org.appng.api.Environment;
import org.appng.api.SiteProperties;
import org.appng.api.model.Application;
import org.appng.api.model.Resource;
import org.appng.api.model.ResourceType;
import org.appng.api.model.Site;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Used to move JSPs to the site's document directory
 */
@Component
@Slf4j
public class Startup implements ApplicationController {

	public boolean start(Site site, Application application, Environment environment) {
		Set<Resource> resources = application.getResources().getResources(ResourceType.RESOURCE);
		String rootDir = site.getProperties().getString(SiteProperties.SITE_ROOT_DIR);
		String wwwDir = site.getProperties().getString(SiteProperties.WWW_DIR);
		List<String> documentDirs = site.getProperties().getList(SiteProperties.DOCUMENT_DIR, ";");
		String docDir = documentDirs.get(0);
		String contentTarget = String.format("%s%s", wwwDir, docDir);
		File targetDir = new File(rootDir, contentTarget);
		
		for (Resource resource : resources) {
			try {
				File sourceFile = resource.getCachedFile().getAbsoluteFile();
				String name = resource.getName();
				File targetFile = new File(targetDir, name);
				FileUtils.deleteQuietly(targetFile);
				FileUtils.copyFile(sourceFile, targetFile);
				log.info("wrote {}", targetFile.getAbsolutePath());
			} catch (IOException e) {
				log.error("error copying file", e);
				return false;
			}
		}
		return true;
	}

	public boolean shutdown(Site site, Application application, Environment environment) {
		return true;
	}

	public boolean removeSite(Site site, Application application, Environment environment) {
		return true;
	}

	public boolean addSite(Site site, Application application, Environment environment) {
		return true;
	}

}
