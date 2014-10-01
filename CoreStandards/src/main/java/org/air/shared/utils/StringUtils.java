/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.shared.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	/*
	 * This is a C# style String.Format method.
	 */
	public static String format(String formatPattern, Object... values) {
		if (formatPattern == null)
			return null;
		if (values == null || values.length == 0)
			return formatPattern;

		for (int counter1 = 0; counter1 < values.length; ++counter1) {
			Object value = values[counter1];
			if (value != null) {
				Pattern p = Pattern.compile("\\{" + counter1 + "\\}");
				Matcher m = p.matcher(formatPattern);
				formatPattern = m.replaceAll(value.toString());
			}
		}
		return formatPattern;
	}

	public static boolean isEmpty(String text) {
		if (text == null || "".equals(text))
			return true;		
		return false;
	}
}
