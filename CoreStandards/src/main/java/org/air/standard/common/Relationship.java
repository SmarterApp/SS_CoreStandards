/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.common;

public class Relationship {
	 public String _fk_Standard_A;
     public String _fk_Standard_B;
     private String _fk_RelationshipType;
     private String Description;
	public String get_fk_Standard_A() {
		return _fk_Standard_A;
	}
	public void set_fk_Standard_A(String _fk_Standard_A) {
		this._fk_Standard_A = _fk_Standard_A;
	}
	public String get_fk_Standard_B() {
		return _fk_Standard_B;
	}
	public void set_fk_Standard_B(String _fk_Standard_B) {
		this._fk_Standard_B = _fk_Standard_B;
	}
	public String get_fk_RelationshipType() {
		return _fk_RelationshipType;
	}
	public void set_fk_RelationshipType(String _fk_RelationshipType) {
		this._fk_RelationshipType = _fk_RelationshipType;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
}
