/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.test.authentication;

import java.sql.SQLException;

import org.air.standard.dao.AuthorizationDAO;
import org.air.standard.exceptions.ContentStandardSecurityException;
import org.air.standard.web.authentication.IUserInformation;

public class TestUserInformation implements IUserInformation{
  private String sessionKey;

	public TestUserInformation(String userName) throws SQLException
	{
		init(userName);
	}
	
	@Override
	public String getSessionKey() throws ContentStandardSecurityException {
		// TODO Auto-generated method stub
		return sessionKey;
	}

	
	private void init(String userName) throws SQLException
	{
		// lets create a new sessionKey.
		AuthorizationDAO dao = new AuthorizationDAO();

		// generate a unique identifier.
		// todo: not sure if we need to make a DB call for this.
		String sessionKey = dao.generateNewSessionKey();
  
		// now insert a session into the table for this user.
		// todo: instead of CSR_Administrator we need to insert the
		// appropriate OpenAM roles - one record for each role.
		dao.addSessionForUser(userName, sessionKey, "Admin");
		 this.sessionKey=sessionKey;
	}

}
