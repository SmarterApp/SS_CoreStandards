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
import org.air.standard.common.AddDeleteRelationshipStatus;
import org.air.standard.common.Relationship;
import org.air.standard.exceptions.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RelationshipDAO extends AbstractDAO {
	
  private static final  Logger _logger = LoggerFactory.getLogger(RelationshipDAO.class);

	private final String CMD_GET_RELATIONSHIPS = "{call GetStandardRelationships(?, ?) }";
	private final String CMD_ADD_RELATIONSHIP = "{call AddStandardRelationship(?, ?, ?) }";
	private final String CMD_REMOVE_RELATIONSHIP = "{call RemoveStandardRelationship(?, ?, ?) }";

	public List<Relationship> getRelationships(String publicationAKey,
			String publicationBKey) throws SQLException, DaoException {
		ArrayList<Relationship> relationshipList = new ArrayList<Relationship>();

		try (SQLConnection connection = getSQLConnection()) {
			try (CallableStatement callstatement = connection
					.prepareCall(CMD_GET_RELATIONSHIPS)){
			callstatement.setString(1, publicationAKey);
			callstatement.setString(2, publicationBKey);

			if (callstatement.execute()) {
				ResultSet rs = callstatement.getResultSet();
				/*
				 * Code to check is the resultset is for Error message, If yes
				 * throws DAOException and cascades to calling methods
				 */
				DaoException.getInstanceIfAvailable(rs);

				// No errors from resultset, so proceed the data retreival.
				relationshipList = (ArrayList<Relationship>) getRelationships(rs);
			}
			}
		} catch (SQLException e) {
			
			String message = StringUtils.format("Error while get publishers in getPublishers method {0}", e.getMessage());
      printStackTraceToLogger(message, e);  
					
			throw e;
		}
		return relationshipList;
	}

	public boolean addRelationship(String standardA, String standardB,
			String relationshipType) throws SQLException, DaoException {
		boolean resultvalue = false;
		try (SQLConnection connection = getSQLConnection()) {
			try (CallableStatement callstatement = connection
					.prepareCall(CMD_ADD_RELATIONSHIP)){
			callstatement.setString(1, standardA);
			callstatement.setString(2, standardB);
			callstatement.setString(3, relationshipType);

			if (callstatement.execute()) {
				ResultSet rs = callstatement.getResultSet();
				/*
				 * Code to check is the resultset is for Error message, If yes
				 * throws DAOException and cascades to calling methods
				 */
				DaoException.getInstanceIfAvailable(rs);
				// No errors from resultset, so proceed the data retreival.
				resultvalue = true;
			}
			}
		} catch (SQLException e) {
			
			String message = StringUtils.format("Error while adding relationship in addRelationship method {0}", e.getMessage());
      printStackTraceToLogger(message, e);  
					
			return false;
		}
		return resultvalue;
	}

	public boolean removeRelationship(String standardA, String standardB,
			String relationshipType) throws SQLException, DaoException {
		// todo: removeRelationshipResult needs to be used here instead.
		boolean resultvalue = false;
		try (SQLConnection connection = getSQLConnection()) {
			try (CallableStatement callstatement = connection
					.prepareCall(CMD_REMOVE_RELATIONSHIP)){
			callstatement.setString(1, standardA);
			callstatement.setString(2, standardB);
			callstatement.setString(3, relationshipType);

			if (callstatement.execute()) {
				ResultSet rs = callstatement.getResultSet();
				/*
				 * Code to check is the resultset is for Error message, If yes
				 * throws DAOException and cascades to calling methods
				 */
				DaoException.getInstanceIfAvailable(rs);

				// No errors from resultset, so proceed the data retreival.
				resultvalue = true;
			}
			}
		} catch (SQLException e) {
			
			String message = StringUtils.format("Error while remove relationship in removeRelationship method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
					
			resultvalue = false;
		}
		return resultvalue;
	}

	public AddDeleteRelationshipStatus addRelationshipResult(String standardA,
			String standardB, String relationshipType) throws SQLException,
			DaoException {
		AddDeleteRelationshipStatus addDeleteRelationshipStatus = null;

		try (SQLConnection connection = getSQLConnection()) {
			try (CallableStatement callstatement = connection
					.prepareCall(CMD_ADD_RELATIONSHIP)){
			callstatement.setString(1, standardA);
			callstatement.setString(2, standardB);
			callstatement.setString(3, relationshipType);

			if (callstatement.execute()) {
				ResultSet rs = callstatement.getResultSet();
				/*
				 * Code to check is the resultset is for Error message, If yes
				 * throws DAOException and cascades to calling methods
				 */
				DaoException.getInstanceIfAvailable(rs);

				// No errors from resultset, so proceed the data retreival.
				addDeleteRelationshipStatus = getAddDeleteRelationshipStatus(rs);
			}
			}
		} catch (SQLException e) {
			
			String message = StringUtils.format("Error while add relationship in addRelationshipResult method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
			
		}
		return addDeleteRelationshipStatus;
	}

	public AddDeleteRelationshipStatus removeRelationshipResult(
			String standardA, String standardB, String relationshipType)
			throws SQLException, DaoException {
		AddDeleteRelationshipStatus addDeleteRelationshipStatus = null;

		try (SQLConnection connection = getSQLConnection()) {
			try (CallableStatement callstatement = connection
					.prepareCall(CMD_REMOVE_RELATIONSHIP)){
			callstatement.setString(1, standardA);
			callstatement.setString(2, standardB);
			callstatement.setString(3, relationshipType);

			if (callstatement.execute()) {
				ResultSet rs = callstatement.getResultSet();
				/*
				 * Code to check is the resultset is for Error message, If yes
				 * throws DAOException and cascades to calling methods
				 */
				DaoException.getInstanceIfAvailable(rs);

				// No errors from resultset, so proceed the data retreival.
				addDeleteRelationshipStatus = getAddDeleteRelationshipStatus(rs);
			}
			}
		} catch (SQLException e) {
			
			String message = StringUtils.format("Error while remove relationship result in removeRelationshipResult method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
				
		}
		return addDeleteRelationshipStatus;
	}

	private List<Relationship> getRelationships(ResultSet resultset)
			throws SQLException {

		ArrayList<Relationship> list = new ArrayList<Relationship>();
		try {
			while (resultset.next()) {
				Relationship relationship = new Relationship();
				relationship.set_fk_Standard_A(resultset
						.getString("_fk_Standard_A"));
				relationship.set_fk_Standard_B(resultset
						.getString("_fk_Standard_B"));
				relationship.set_fk_RelationshipType(resultset
						.getString("_fk_RelationshipType"));

				list.add(relationship);
			}
		} catch (SQLException e) {
			String message = StringUtils.format("Error in getRelationships method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
					
			e.printStackTrace();
		}
		return list;
	}

	private AddDeleteRelationshipStatus getAddDeleteRelationshipStatus(
			ResultSet resultset) throws SQLException {
		AddDeleteRelationshipStatus addDeleteRelationshipStatus = null;
		try {
			while (resultset.next()) {
				addDeleteRelationshipStatus = new AddDeleteRelationshipStatus();
				addDeleteRelationshipStatus.setStatus(resultset
						.getString("status"));
				addDeleteRelationshipStatus.setReason(resultset
						.getString("reason"));
				return addDeleteRelationshipStatus;
			}
		} catch (SQLException e) {
			
			String message = StringUtils.format("Error in getAddDeleteRelationshipStatus method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
					
			e.printStackTrace();
		}
		return addDeleteRelationshipStatus;
	}
}
