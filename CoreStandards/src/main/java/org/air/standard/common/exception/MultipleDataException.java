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

public class MultipleDataException extends Exception {

	private int mExpectedSize = -1;
	private int mReceivedSize = -1;

	private static final String MESSAGE = "Multiple records were retrieved. Expected size was {0}. Size received {1}.";

	public MultipleDataException(int expectedSize, int receivedSize) {
		super(getMessage(expectedSize, receivedSize));
	}

	public MultipleDataException(int expectedSize, int receivedSize,
			Exception exp) {
		super(getMessage(expectedSize, receivedSize), exp);
	}

	private static String getMessage(int expectedSize, int receivedSize) {
		return StringUtils.format(MESSAGE, expectedSize, receivedSize);
	}
}
