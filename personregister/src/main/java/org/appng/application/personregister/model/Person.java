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
package org.appng.application.personregister.model;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.appng.api.Environment;
import org.appng.api.FieldProcessor;
import org.appng.api.FileUpload;
import org.appng.api.FileUpload.Unit;
import org.appng.api.FormValidator;
import org.appng.api.NotBlank;
import org.appng.api.Options;
import org.appng.api.Request;
import org.appng.api.SiteProperties;
import org.appng.api.model.Application;
import org.appng.api.model.Identifiable;
import org.appng.api.model.Nameable;
import org.appng.api.model.Site;
import org.appng.api.model.Versionable;
import org.appng.api.search.Document;
import org.appng.application.personregister.MessageConstants;
import org.appng.forms.FormUpload;
import org.appng.search.indexer.SimpleDocument;
import org.appng.xml.platform.FieldDef;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "person")
public class Person implements FormValidator, Nameable, Versionable<Date>, Identifiable<Integer> {

	private Integer id;
	private String name;
	private String lastname;
	private Date birthDay;
	private Date version;
	private Gender gender;
	private byte[] pictureData;
	private String pictureContentType;
	private FormUpload picture;
	private Double cashAmount;
	private String picturePath;
	private Coordinate location;
	private String description;

	public enum Gender {
		MALE, FEMALE, OTHER;
	}

	public Person() {

	}

	public Person(String name, String lastname) {
		this.name = name;
		this.lastname = lastname;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	@NotBlank
	public String getName() {
		return name;
	}

	@NotBlank
	public String getLastname() {
		return lastname;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	@Version
	@NotNull
	public Date getVersion() {
		return version;
	}

	@Enumerated(EnumType.STRING)
	public Gender getGender() {
		return gender;
	}

	public Double getCashAmount() {
		return cashAmount;
	}

	@Lob
	@JsonIgnore
	public byte[] getPictureData() {
		return pictureData;
	}

	@Transient
	@FileUpload(minCount = 0, maxSize = 10, unit = Unit.MB, fileTypes = "jpg,png,jpeg")
	public FormUpload getPicture() {
		return picture;
	}

	public void setPicture(FormUpload picture) {
		if (null != picture) {
			setPictureData(picture.getBytes());
			setPictureContentType(picture.getContentType());
		}
		this.picture = picture;
	}

	public String getPictureContentType() {
		return pictureContentType;
	}

	@Transient
	public String getPicturePath() {
		return picturePath;
	}

	public Coordinate getLocation() {
		if (null == location) {
			location = new Coordinate(50.116302d, 8.684477d);
		}
		return location;
	}

	/**
	 * By implementing {@link FormValidator}, we can validate the bind-object
	 */
	public void validate(Site site, Application application, Environment environment, Options options, Request request,
			FieldProcessor fieldProcessor) {
		if (getLastname().equalsIgnoreCase(getName())) {
			FieldDef field = fieldProcessor.getField("name");
			fieldProcessor.addErrorMessage(field, request.getMessage(MessageConstants.PERSON_NAME_AND_LAST_NAME_SAME));
		}
	}

	/**
	 * Generates a {@link Document} from this {@link Person}
	 */
	public Document toDocument(Site site, Application application) {
		SimpleDocument docAdded = new SimpleDocument();
		// the id must be unique per type
		docAdded.setId(getId().toString());
		// use the fully qualified class name as a type
		docAdded.setType(Person.class.getName());
		docAdded.setLanguage("en");
		docAdded.setDate(getVersion());
		docAdded.setName(getName() + " " + getLastname());
		docAdded.setDescription(getDescription());
		String path = String.format("%s%s/%s/%s/index/update/%s", site.getDomain(),
				site.getProperties().getString(SiteProperties.MANAGER_PATH), site.getName(), application.getName(),
				getId());
		docAdded.setPath(path);
		return docAdded;
	}

	@Embeddable
	public static class Coordinate extends org.appng.tools.locator.Coordinate {
		public Coordinate(double lat, double lng) {
			super(lat, lng);
		}

		public Coordinate() {
			super();
		}

		@Override
		public Double getLatitude() {
			return super.getLatitude();
		}

		@Override
		public Double getLongitude() {
			return super.getLongitude();
		}
	}
}
