/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.common;

public class Standard {

	private String key;
	private String name;
	private String fkParent;
	private String fkPublication;
	private String description;
	private int treeLevel;
	private String fkGradeLevel;
	private String shortName;	
     
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFkParent() {
		return fkParent;
	}
	public void setFkParent(String fkParent) {
		this.fkParent = fkParent;
	}
	public String getFkPublication() {
		return fkPublication;
	}
	public void setFkPublication(String fkPublication) {
		this.fkPublication = fkPublication;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getTreeLevel() {
		return treeLevel;
	}
	public void setTreeLevel(int treeLevel) {
		this.treeLevel = treeLevel;
	}
	public String getFkGradeLevel() {
		return fkGradeLevel;
	}
	public void setFkGradeLevel(String fkGradeLevel) {
		this.fkGradeLevel = fkGradeLevel;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
     
}
