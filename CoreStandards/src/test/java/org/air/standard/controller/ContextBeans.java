/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.controller;

import org.springframework.context.ApplicationContext;

public class ContextBeans {
	 private static ApplicationContext _contextBeans = null;
	 
	 public static void set(ApplicationContext context)
	 {
		 _contextBeans = context;
	 }
	 
	 public static <T> T get(String name, Class<T> c)
	 {
		 return _contextBeans.getBean(name, c);
	 }
}
