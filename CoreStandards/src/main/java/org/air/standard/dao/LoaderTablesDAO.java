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

import org.air.shared.db.AbstractDAO;
import org.air.shared.db.SQLConnection;
import org.air.shared.utils.StringUtils;
import org.air.standard.common.LoaderBenchmarkGrade;
import org.air.standard.common.LoaderCategory;
import org.air.standard.common.LoaderPublication;
import org.air.standard.common.LoaderSock;
import org.air.standard.common.LoaderStandard;
import org.air.standard.common.LoaderStandardRelationship;
import org.air.standard.exceptions.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import org.air.standard.util.ConnectionManager;

public class LoaderTablesDAO extends AbstractDAO {
	
	
  private static final  Logger _logger = LoggerFactory.getLogger(LoaderTablesDAO.class);

	private final String CMD_ADD_LOADER_PUBLICATION = "{call Loader_AddPublication(?, ?, ?, ?, ?) }";
	private final String CMD_ADD_LOADER_CATEGORIES = "{call Loader_AddCategories(?, ?, ?) }";
	private final String CMD_ADD_LOADER_STANDARDS = "{call Loader_AddStandards(?, ?, ?,?,?,?) }";
	private final String CMD_ADD_LOADER_BENCHMARKGRADES = "{call Loader_AddBenchmarkGrades(?, ?, ?) }";
	private final String CMD_ADD_LOADER_SOCKS = "{call Loader_AddSOCKs(?, ?, ?) }";
	private final String CMD_ADD_LOADER_STANDARDRELATIONSHIP = "{call Loader_AddStandardRelationship(?, ?, ?, ?) }";

	// modify
	private final String CMD_LOADER_CLEAR = "{call Loader_Clear(?) }";

	public void addLoaderPublication(LoaderPublication pub)
			throws SQLException, DaoException {

		try (SQLConnection connection = getSQLConnection()) {

			try (CallableStatement callstatement = connection
					.prepareCall(CMD_ADD_LOADER_PUBLICATION)){
			callstatement.setString(1, pub.getSessionKey());
			callstatement.setString(2, pub.getPublisherName());
			callstatement.setString(3, pub.getPublicationDescription());
			callstatement.setString(4, pub.getSubject());
			callstatement.setInt(5, (int) pub.getVersion());
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
			
			String message = StringUtils.format("Error while adding publication in addLoaderPublication method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
							
			throw e;
		}
	}

	public void addLoaderCategories(LoaderCategory cat) throws SQLException,
			DaoException {

		try (SQLConnection connection = getSQLConnection()) {

			try (CallableStatement callstatement = connection
					.prepareCall(CMD_ADD_LOADER_CATEGORIES)){
			callstatement.setString(1, cat.getSessionKey());
			callstatement.setString(2, cat.getCategory());
			callstatement.setInt(3, cat.getTreeLevel());

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
			
			String message = StringUtils.format("Error while adding category in addLoaderCategories method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
							
			throw e;
		}
	}

	public void addLoaderStandards(LoaderStandard std) throws SQLException,
			DaoException {

		try (SQLConnection connection = getSQLConnection()) {

		try	(CallableStatement callstatement = connection
					.prepareCall(CMD_ADD_LOADER_STANDARDS)){
			callstatement.setString(1, std.getSessionKey());
			callstatement.setInt(2, std.getLevel());
			callstatement.setString(3, std.getKey());
			callstatement.setString(4, std.getName());
			callstatement.setString(5, std.getDescription());
			callstatement.setString(6, std.getShortName());

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
			
			String message = StringUtils.format("Error while adding standards in addLoaderStandards method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
							
			throw e;
		}
	}

	public void addLoaderBenchmarkGrades(LoaderBenchmarkGrade benchmark)
			throws SQLException, DaoException {
		try (SQLConnection connection = getSQLConnection()) {

			try (CallableStatement callstatement = connection
					.prepareCall(CMD_ADD_LOADER_BENCHMARKGRADES)){
			callstatement.setString(1, benchmark.getSessionKey());
			callstatement.setString(2, benchmark.getBenchmark());
			callstatement.setString(3, benchmark.getGrade());

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
			
			String message = StringUtils.format("Error while adding benchmark grade in addLoaderBenchmarkGrades method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
							
			throw e;
		}

	}

	public void addLoaderSOCKs(LoaderSock socks) throws SQLException,
			DaoException {
		try (SQLConnection connection = getSQLConnection()) {

			try (CallableStatement callstatement = connection
					.prepareCall(CMD_ADD_LOADER_SOCKS)){
			callstatement.setString(1, socks.getSessionKey());
			callstatement.setString(2, socks.getKnowledgeCategory());
			callstatement.setString(3, socks.getDescription());

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
			
			String message = StringUtils.format("Error while adding socks grade in addLoaderSOCKs method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
				
			throw e;
		}

	}

	public void addLoaderStandardRelationship(
			LoaderStandardRelationship relationship) throws SQLException,
			DaoException {
		try (SQLConnection connection = getSQLConnection()) {

		try	(CallableStatement callstatement = connection
					.prepareCall(CMD_ADD_LOADER_STANDARDRELATIONSHIP)){
			callstatement.setString(1, relationship.getSessionKey());
			callstatement.setString(2, relationship.getKeyA());
			callstatement.setString(3, relationship.getKeyB());
			callstatement.setString(4, relationship.getRelationshipType());

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
			
			String message = StringUtils.format("Error while adding loader standard relation in addLoaderStandardRelationship method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
							
			throw e;
		}

	}

	public void loaderClear(String sessionKey) throws SQLException,
			DaoException {
		try (SQLConnection connection = getSQLConnection()) {
			try(CallableStatement callstatement = connection
					.prepareCall(CMD_LOADER_CLEAR)){
			callstatement.setString(1, sessionKey);
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
			
			String message = StringUtils.format("Error while clearing loader tables in loaderClear method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
              
					
			throw e;
		}
	}
}
