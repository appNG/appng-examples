package org.appng.application.example.camunda.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.appng.api.FileUpload;
import org.appng.api.model.Nameable;
import org.appng.api.model.Versionable;
import org.appng.forms.FormUpload;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = { "data" })
@Entity
public class Document implements Nameable, Versionable<Date> {

	private Integer id;
	private String name;
	private String description;
	private byte[] data;
	private State state;
	private Date version;
	private String initiator;
	private String groupName;
	private FormUpload upload;

	public enum State {
		NEW, PENDING, ACCEPTED, DECLINED;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	@Enumerated(EnumType.STRING)
	public State getState() {
		return state;
	}

	@Lob
	public byte[] getData() {
		return data;
	}

	@NotNull(groups = Upload.class)
	@Transient
	@FileUpload(minCount = 1, fileTypes = "pdf,png,xlsx,doc", groups = Upload.class)
	public FormUpload getUpload() {
		return upload;
	}

	@Version
	public Date getVersion() {
		return version;
	}
	
	public interface Upload {

	}

}
