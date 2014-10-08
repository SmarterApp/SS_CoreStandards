/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.dao;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.air.shared.db.AbstractDAO;
import org.air.shared.db.SQLConnection;
import org.air.shared.utils.StringUtils;
import org.air.standard.common.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* 
 *  VERIFY THE USER WITH GIVEN INPUT PARAMETERS
 * */
public class AuthorizationDAO extends AbstractDAO {
	
  private static final  Logger _logger = LoggerFactory.getLogger(AuthorizationDAO.class);


	/*
	 * VALIDATE USER WITH GIVEN PARAMETERS USERNAME AND PWD
	 */
	public boolean validateUser(String userName, String password)
			throws SQLException {
		boolean isvaliduser = false;
		ResultSet rs = null;
		try (SQLConnection connection = getSQLConnection()) {
		 try (CallableStatement	callstatement = connection
					.prepareCall("{call GetSubjectByPublisher(?) }")){
			callstatement.setString(1, userName);
			callstatement.setString(2, password);
			if (callstatement.execute()) {
				rs = callstatement.getResultSet();
				while (rs.next()) {
				}
			}
		 }
		} catch (SQLException e) {
			
			String message = StringUtils.format("Error while validating user in validateUser method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
					
			throw e;
		}
		return isvaliduser;
	}

	/*
	 * GET USER OBJECT WITH GIVEN INPUT PARAMETERS USERNAME AND PWD
	 */
	public User getUser(String userName, String password) throws SQLException {
		User user = null;
		
		ResultSet relset = null;
		try (SQLConnection connection = getSQLConnection()) {
		  try (CallableStatement callstatement = connection
					.prepareCall("{call GetSubjectByPublisher(?) }")){
			callstatement.setString(1, userName);
			callstatement.setString(2, password);
			if (callstatement.execute()) {
				relset = callstatement.getResultSet();
				while (relset.next()) {
				}
			}
		  }
		} catch (SQLException e) {
			
			String message = StringUtils.format("Error while getting User Object in getUser method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
					
			throw e;
		}
		return user;
	}

	/*
	 * Add a authenticated user to the session table.
	 */
	public void addSessionForUser(String userName, String sessionKey,
			String openAmRoles) throws SQLException {
			ResultSet relset = null;
		try (SQLConnection connection = getSQLConnection()) {
		  try (CallableStatement	callstatement = connection
					.prepareCall("{call AddSession(?, ?, ?) }")){
			callstatement.setString(1, sessionKey);
			callstatement.setString(2, userName);
			callstatement.setString(3, openAmRoles);

			if (callstatement.execute()) {
				relset = callstatement.getResultSet();
				while (relset.next()) {
				}
			}
		  }
		} catch (SQLException e) {
			
			String message = StringUtils.format("Error while adding User to session in addSessionForUser method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
					
			throw e;
		}
	}

	/*
	 * Get a new session key.
	 */
	public String generateNewSessionKey() throws SQLException {

/*
 * We checked in code and it seems that the only thing we are doing in generateNewSessionKey is to get a new UUID.
 * The problem is that SQL Server does not seem to allow non-deterministic user defined functions. 
 * so we are instead generating a new UUID in code right here. We are going to comment all existing database function call here.
 */
		String sessionKey = null;
		try (SQLConnection connection = getSQLConnection()) {
			String sql = "select GenerateSessionKey()";
			Statement s = connection.createStatement();
			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				sessionKey = (String) rs.getString(1);
				break;
			}
		} catch (SQLException e) {
			
			String message = StringUtils.format("Error while generating sessionkey in generateNewSessionKey method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
						
			throw e;
		}
		return sessionKey;
	 // return UUID.randomUUID ().toString ();
	}

	/*
	 * Logout a user. The stored procedure should invoke Loader_Clear to clear
	 * all staging tables. It should also end date the session.
	 */
	public void logout(String sessionKey) throws SQLException {
		
		ResultSet relset = null;
		try (SQLConnection connection = getSQLConnection()) {
		  try (CallableStatement callstatement = connection.prepareCall("{call Logout(?) }")){
			callstatement.setString(1, sessionKey);

			if (callstatement.execute()) {
				relset = callstatement.getResultSet();
				while (relset.next()) {
					// not expecting anything.
				}
			}
		  }
		} catch (SQLException e) {
			
			String message = StringUtils.format("Error while logging out user in logout method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
					
			throw e;
		}
	}

}
