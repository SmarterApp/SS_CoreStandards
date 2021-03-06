/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.common;

public class Sock {
	private String knowledgeCategory;
	private String description;
	private String socksKey;
	private String fkPublication;
    
	public String getSocksKey() {
		return socksKey;
	}

	public void setSocksKey(String socksKey) {
		this.socksKey = socksKey;
	}
	
	public String getKnowledgeCategory() {
		return knowledgeCategory;
	}

	public void setKnowledgeCategory(String knowledgeCategory) {
		this.knowledgeCategory = knowledgeCategory;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getFkPublication() {
		return fkPublication;
	}
	
	public void setFkPublication(String fkPublication) {
		this.fkPublication = fkPublication;
	}
}
