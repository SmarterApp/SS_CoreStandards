/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.exceptions;

public class ContentStandardException extends Exception {

	/**
	 * add a version id
	 */
	private static final long serialVersionUID = 1L;

	public ContentStandardException(String exceptionMsg) {
		super(exceptionMsg);
	}

	public ContentStandardException(Exception exp) {
		super(exp);
	}

}
