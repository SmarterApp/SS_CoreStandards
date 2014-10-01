/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.common.exception;

import org.air.shared.utils.StringUtils;

public class InvalidArgumentException extends Exception{
	
	private static final String MESSAGE = "Argument {0} has invalid value. Expected value should be {1}";
	public InvalidArgumentException (String argument, String expectedValue)
	{
		super(StringUtils.format(MESSAGE, argument, expectedValue));
	}
}
