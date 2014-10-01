/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.openoffice;

public class StandardsHolder {
	private String level;
	private String key;
	private String name;
	private String description;
	private String shortName;

	public StandardsHolder(String level, String key, String name,
			String description, String shortName) {
		this.level = level;
		this.key = key;
		this.name = name;
		this.description = description;
		this.shortName = shortName;
	}

	public String getLevel() {
		return level;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getShortName() {
		return shortName;
	}
}
