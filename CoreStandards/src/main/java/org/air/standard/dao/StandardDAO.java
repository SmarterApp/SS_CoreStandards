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
import java.util.ArrayList;
import java.util.List;

import org.air.shared.db.AbstractDAO;
import org.air.shared.db.SQLConnection;
import org.air.shared.utils.StringUtils;
import org.air.standard.common.Standard;
import org.air.standard.common.exception.InvalidArgumentException;
import org.air.standard.exceptions.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardDAO extends AbstractDAO {
	
  private static final  Logger _logger = LoggerFactory.getLogger(StandardDAO.class);

	private final String CMD_GET_STANDARDS_BY_SESSION_KEY = "{call Loader_GetStandards(?)}";
	private final String CMD_MODIFY_STANDARD = "{call Loader_EditStandard(?, ?, ?, ?, ?, ?)}";
	private final String CMD_GET_STANDARDS_BY_PUBLICATION_KEY_AND_GRADE_KEY = "{call getstandard(?, ?, ?)}";
	
	/* 
	 * return List of Standards by Publication key as input
	 * It throws Invalid Argument and SQL and DAO Exceptions
	 * */
	public List<Standard> getStandards(String publicationKey)
			throws InvalidArgumentException, SQLException, DaoException {
		try {
			if (publicationKey == null)
				throw new InvalidArgumentException("publicationKey",
						"is not null");
			return getStandards(publicationKey, null, true);
		} catch (SQLException e) {
			String message = StringUtils.format("Error while geting standard in getStandards method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
					
			throw e;
		}
	}

	/* 
	 * return List of Standards by Publication key, grade key and grade agnostic as inputs
	 * It throws SQL and DAO Exceptions
	 * */
	public List<Standard> getStandards(String publicationKey, String gradeKey,
			boolean gradeAgnostic) throws SQLException, DaoException {

		ArrayList<Standard> standardlist = new ArrayList<Standard>();

		try (SQLConnection connection = getSQLConnection()) {
			try(CallableStatement callstatement = connection
					.prepareCall(CMD_GET_STANDARDS_BY_PUBLICATION_KEY_AND_GRADE_KEY)){
			callstatement.setString(1, publicationKey);
			callstatement.setString(2, gradeKey); //Grade Key
			callstatement.setInt(3, gradeAgnostic ? 1 : 0);
			if (callstatement.execute()) {
				ResultSet rs = callstatement.getResultSet();
				/*
				 * Code to check is the resultset is for Error message, If yes
				 * throws DAOException and cascades to calling methods
				 */
				DaoException.getInstanceIfAvailable(rs);

				// No errors from resultset, so proceed the data retreival.
				standardlist = (ArrayList<Standard>) getStandardsResultSet(rs);
			}
			}
		} catch (SQLException e) {
			
			String message = StringUtils.format("Error while geting standard in getStandards method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
					
			throw e;
		}
		return standardlist;
	}

	/* 
	 *  return List of Standards by Result set as input
	 *  It throws SQL Exceptions
	 * */
	private List<Standard> getStandardsResultSet(ResultSet resultset)
			throws SQLException {

		ArrayList<Standard> standardList = new ArrayList<Standard>();
		try {
			while (resultset.next()) {
				Standard standard = new Standard();
				standard.setKey(resultset.getString("_key"));
				standard.setName(resultset.getString("name"));
				standard.setFkParent(resultset.getString("_fk_parent"));
				standard.setFkPublication(resultset.getString("_fk_publication"));
				standard.setDescription(resultset.getString("description"));				
				standard.setTreeLevel(resultset.getInt("treelevel"));
				standard.setFkGradeLevel(resultset.getString("_fk_gradelevel"));
				//standard.setFkPublication(resultset.getString("pubkey"));				
				standard.setShortName(resultset.getString("shortname"));			

				standardList.add(standard);
			}
		} catch (SQLException e) {
			
			String message = StringUtils.format("Error while geting standard result set in getStandards method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
							
			throw e;
		}
		return standardList;
	}
	/* 
	 *  return List of Standards by Result set as input
	 *  It throws SQL Exceptions
	 * */
	private List<Standard> getStandards(ResultSet resultset)
			throws SQLException {

		ArrayList<Standard> standardList = new ArrayList<Standard>();
		try {
			while (resultset.next()) {
				Standard standard = new Standard();
				standard.setTreeLevel(resultset.getInt("standardlevel"));
				standard.setKey(resultset.getString("standardkey"));
				standard.setName(resultset.getString("standardname"));
				standard.setDescription(resultset.getString("description"));
				standard.setShortName(resultset.getString("shortname"));

				standardList.add(standard);
			}
		} catch (SQLException e) {
			
			String message = StringUtils.format("Error while geting standard result set in getStandards method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
						
			throw e;
		}
		return standardList;
	}

	/* Edit the Standard
	 * return List of Standards by session key, grade key and standard key as inputs
	 * It throws SQL and DAO Exceptions
	 * */
	public List<Standard> editStandard(String sessionKey, Standard standard)
			throws SQLException, DaoException {

		try (SQLConnection connection = getSQLConnection()) {
			try (CallableStatement callstatement = connection
					.prepareCall(CMD_MODIFY_STANDARD)){
			callstatement.setString(1, sessionKey);
			callstatement.setInt(2, standard.getTreeLevel());
			callstatement.setString(3, standard.getKey());
			callstatement.setString(4, standard.getName());
			callstatement.setString(5, standard.getDescription());
			callstatement.setString(6, standard.getShortName());
			if (callstatement.execute()) {
				ResultSet rs = callstatement.getResultSet();
				/*
				 * Code to check is the resultset is for Error message, If yes
				 * throws DAOException and cascades to calling methods
				 */
				DaoException.getInstanceIfAvailable(rs);
			}
			}
		} catch (SQLException e) {
			
			String message = StringUtils.format("Error while editing standard in editStandard method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
					
			throw e;
		}

		return getStandardsBySessionKey(sessionKey);
	}

	/* Get Standards By Session Key
	 * return List of Standards by session key as inputs
	 * It throws SQL and DAO Exceptions
	 * */
	
	public List<Standard> getStandardsBySessionKey(String sessionkey)
			throws SQLException, DaoException {

		ArrayList<Standard> standardlist = new ArrayList<Standard>();

		try (SQLConnection connection = getSQLConnection()) {
			try (CallableStatement callstatement = connection
					.prepareCall(CMD_GET_STANDARDS_BY_SESSION_KEY)){
			callstatement.setString(1, sessionkey);

			if (callstatement.execute()) {
				ResultSet rs = callstatement.getResultSet();
				/*
				 * Code to check is the resultset is for Error message, If yes
				 * throws DAOException and cascades to calling methods
				 */
				DaoException.getInstanceIfAvailable(rs);

				// No errors from resultset, so proceed the data retreival.
				standardlist = (ArrayList<Standard>) getStandards(rs);
			}
			}
		} catch (SQLException e) {
			
			String message = StringUtils.format("Error while getting standard by session key in getStandardsBySessionKey method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
						
			throw e;
		}
		return standardlist;
	}

}
