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

public class Alignment {

	private String _Key_Publication_A;
	private String _Key_Publication_B;
	private String Status;
	private String ModifiedBy;
	private Date Modified;

	public String get_Key_Publication_A() {
		return _Key_Publication_A;
	}

	public void set_Key_Publication_A(String _Key_Publication_A) {
		this._Key_Publication_A = _Key_Publication_A;
	}

	public String get_Key_Publication_B() {
		return _Key_Publication_B;
	}

	public void set_Key_Publication_B(String _Key_Publication_B) {
		this._Key_Publication_B = _Key_Publication_B;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getModifiedBy() {
		return ModifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		ModifiedBy = modifiedBy;
	}

	public Date getModified() {
		return Modified;
	}

	public void setModified(Date modified) {
		Modified = modified;
	}
}
