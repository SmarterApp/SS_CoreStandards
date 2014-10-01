/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.web.authentication;

import org.air.standard.exceptions.ContentStandardSecurityException;
import org.air.standard.security.CoreStandardSbacUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

public class WebUserInformation implements IUserInformation{

  private static final Logger _logger = LoggerFactory.getLogger (WebUserInformation.class);
      
	public String getSessionKey() throws ContentStandardSecurityException{
	  String sessionId = null;
	  CoreStandardSbacUser user;
    try {
      user = (CoreStandardSbacUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      sessionId = user.getSessionId ();
    } catch (Exception e) {
      _logger.error ("Authentication is required to get session key..",e);
    }
	  return sessionId;
	}

}
