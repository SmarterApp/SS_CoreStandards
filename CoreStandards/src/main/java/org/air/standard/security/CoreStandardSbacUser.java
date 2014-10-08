/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.security;

import java.util.Map;

import org.opentestsystem.shared.security.domain.SbacRole;
import org.opentestsystem.shared.security.domain.SbacUser;

import com.google.common.collect.Multimap;

/**
 * @author mpatel
 *
 */
public class CoreStandardSbacUser extends SbacUser
{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * @param inUserRoles
   * @param userAttributes
   */
  public CoreStandardSbacUser (Multimap<String, SbacRole> inUserRoles, Map<String, String> userAttributes) {
    super (inUserRoles, userAttributes);
  }

  private String sessionId;

  public String getSessionId () {
    return sessionId;
  }

  public void setSessionId (String sessionId) {
    this.sessionId = sessionId;
  }
  
  

}
