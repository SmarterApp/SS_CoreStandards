/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.delegate;

import java.sql.SQLException;

import org.air.standard.common.User;
import org.air.standard.dao.AuthorizationDAO;

public class AuthorizationDelegate {
	
	
	
	 public static boolean isValidUser(String username, String password) throws SQLException{
         boolean isValid = false;         
         isValid = (new AuthorizationDAO()).validateUser(username, password);
         return isValid;
      }
	 
	 public static User getUser(String username, String password) throws SQLException{
         User user;         
         user = (new AuthorizationDAO()).getUser(username, password);
         return user;
     }
}
