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
import org.air.standard.common.LoaderSock;
import org.air.standard.common.Sock;
import org.air.standard.exceptions.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocksDAO extends AbstractDAO {
	
	

  private static final  Logger _logger = LoggerFactory.getLogger(SocksDAO.class);

	private final String CMD_GET_LOADER_SOCKS = "{call Loader_GetSOCKs(?) }";
	private final String CMD_MODIFY_SOCKS = "{call Loader_EditSOCK(?, ?, ?, ?, ?) }";
	private final String CMD_GET_SOCKS = "{call GetSocks(?) }";

	public List<LoaderSock> getSocksBySessionKey(String sessionKey)
			throws SQLException, DaoException {

		ArrayList<LoaderSock> socksList = new ArrayList<LoaderSock>();
		try (SQLConnection connection = getSQLConnection()) {
			try (CallableStatement callstatement = connection
					.prepareCall(CMD_GET_LOADER_SOCKS)){
			callstatement.setString(1, sessionKey);

			if (callstatement.execute()) {
				ResultSet rs = callstatement.getResultSet();
				/*
				 * Code to check is the resultset is for Error message, If yes
				 * throws DAOException and cascades to calling methods
				 */
				DaoException.getInstanceIfAvailable(rs);

				// No errors from resultset, so proceed the data retreival.
				socksList = (ArrayList<LoaderSock>) getLoaderSocks(rs);
			}
			}
		} catch (SQLException e) {
			
			String message = StringUtils.format("Error while geting socks by session key in getSocksBySessionKey method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
						
			throw e;
		}

		return socksList;
	}

	public List<LoaderSock> editSock(String sessionKey, LoaderSock sock)
			throws SQLException, DaoException {

		try (SQLConnection connection = getSQLConnection()) {
			try (CallableStatement callstatement = connection
					.prepareCall(CMD_MODIFY_SOCKS)){

			callstatement.setString(1, sessionKey);
			callstatement.setString(2, sock.getKnowledgeCategory());
			callstatement.setString(3, sock.getDescription());
			callstatement.setString(4, sock.getNewKnowledgeCategory());
			callstatement.setString(5, sock.getNewDescription());
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
			
			String message = StringUtils.format("Error while editing socks by session key in editSock method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
							
			throw e;
		}

		return getSocksBySessionKey(sessionKey);
	}

	private List<LoaderSock> getLoaderSocks(ResultSet resultset) throws SQLException {

		ArrayList<LoaderSock> socksList = new ArrayList<LoaderSock>();
		try {
			while (resultset.next()) {

				LoaderSock socks = new LoaderSock();

				socks.setDescription(resultset.getString("Description"));
				socks.setKnowledgeCategory(resultset
						.getString("KnowledgeCategory"));

				socksList.add(socks);
			}

		} catch (SQLException e) {
			
			String message = StringUtils.format("Error while geting socks result set in getSocks method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
			
			throw e;
		}
		return socksList;
	}
	
	private List<Sock> getSocks(ResultSet resultset) throws SQLException {

		ArrayList<Sock> socksList = new ArrayList<Sock>();
		try {
			while (resultset.next()) {

				Sock socks = new Sock();

				socks.setDescription(resultset.getString("description"));
				socks.setKnowledgeCategory(resultset.getString("name"));
				socks.setSocksKey(resultset.getString("_key"));
				socks.setFkPublication(resultset.getString("_fk_publication"));

				socksList.add(socks);
			}

		} catch (SQLException e) {
			
			String message = StringUtils.format("Error while geting socks result set in getSocks method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
			
			throw e;
		}
		return socksList;
	}
	
	public List<Sock> getSocksByPublicationKey(String publicationKey)
			throws SQLException, DaoException {
		
		ArrayList<Sock> list = new ArrayList<Sock>();
		try (SQLConnection connection = getSQLConnection()) {
			try (CallableStatement callstatement = connection
					.prepareCall(CMD_GET_SOCKS)){
			callstatement.setString(1, publicationKey);

			if (callstatement.execute()) {
				ResultSet rs = callstatement.getResultSet();
				/*
				 * Code to check is the result set is for Error message, If yes
				 * throws DAOException and cascades to calling methods
				 */
				DaoException.getInstanceIfAvailable(rs);

				// No errors from resultset, so proceed the data retrieval.
				list = (ArrayList<Sock>) getSocks(rs);
			}
			}
		} catch (SQLException e) {
			
			String message = StringUtils.format("Error while getting Socks by publicationKey in getSocksByPublicationKey method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
						
			throw e;
		}
		return list;
	}
}
