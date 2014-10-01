/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.shared.resources;

public class ResourceInitializationException extends Exception{
	public ResourceInitializationException(String message){
		super(message);
	}
	
	public ResourceInitializationException(Exception exp)
	{
		super (exp);
	}
	
	public ResourceInitializationException(String message, Exception exp)
	{
		super(message, exp);
	}
}
