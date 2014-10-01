/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.common;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Publication {
	private String key;
	private String fkPublisher;
	@JsonIgnore
	private Date date;
	private double version;
	@JsonIgnore
	private boolean isCanonical;
	private String description;
	private String fkSubject;
	private String subjectLabel;
	@JsonIgnore
	private String url;
	private String status;
    
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getFkPublisher() {
		return fkPublisher;
	}
	public void setFkPublisher(String fkPublisher) {
		this.fkPublisher = fkPublisher;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public double getVersion() {
		return version;
	}
	public void setVersion(double version) {
		this.version = version;
	}
	@JsonIgnore
	public boolean isCanonical() {
		return isCanonical;
	}
	public void setCanonical(boolean isCanonical) {
		this.isCanonical = isCanonical;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFkSubject() {
		return fkSubject;
	}
	public void setFkSubject(String fkSubject) {
		this.fkSubject = fkSubject;
	}
	public String getSubjectLabel() {
		return subjectLabel;
	}
	public void setSubjectLabel(String subjectLabel) {
		this.subjectLabel = subjectLabel;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Publication [key=" + key + ", fkPublisher=" + fkPublisher
				+ ", version=" + version + ", description=" + description + ", fkSubject="
				+ fkSubject + ", subjectLabel=" + subjectLabel + ", status=" + status + "]";
	}
    
    
}
