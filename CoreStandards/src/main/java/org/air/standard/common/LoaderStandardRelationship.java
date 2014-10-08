/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.common;

public class LoaderStandardRelationship {
	private String KeyA;
	private String KeyB;
	private String RelationshipType;
	private String sessionKey;
	
	public String getKeyA() {
		return KeyA;
	}

	public void setKeyA(String keyA) {
		KeyA = keyA;
	}

	public String getKeyB() {
		return KeyB;
	}

	public void setKeyB(String keyB) {
		KeyB = keyB;
	}

	public String getRelationshipType() {
		return RelationshipType;
	}

	public void setRelationshipType(String relationshipType) {
		RelationshipType = relationshipType;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	
}
