/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.common;

public class LoaderCategory {
	
	private String category;
	private int treeLevel;
	private String publicationKey;
	private String sessionKey;
	private String newCategory;	
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getTreeLevel() {
		return treeLevel;
	}

	public void setTreeLevel(int treeLevel) {
		this.treeLevel = treeLevel;
	}

	public String getPublicationKey() {
		return publicationKey;
	}

	public void setPublicationKey(String publicationKey) {
		this.publicationKey = publicationKey;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getNewCategory() {
		return newCategory;
	}

	public void setNewCategory(String newCategory) {
		this.newCategory = newCategory;
	}
	
}
