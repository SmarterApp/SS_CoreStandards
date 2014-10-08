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
import org.air.standard.common.Publication;
import org.air.standard.common.ReturnStatus;
import org.air.standard.common.exception.MultipleDataException;
import org.air.standard.exceptions.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublicationDAO extends AbstractDAO {

  private static final  Logger _logger = LoggerFactory.getLogger(PublicationDAO.class);

	private final String CMD_GET_PUBLICATION = "{call GetPublicationBySubjectByPublisher(?, ?) }";
	private final String CMD_MODIFY_PUBLICATION_STATUS = "{call ModifyPublicationStatus(?, ?) }";
	private final String CMD_COPY_PUBLICATION = "{call CopyPublication(?, ?) }";
	private final String CMD_EDIT_PUBLICATION = "{call EditPublication(?, ?) }";
	private final String CMD_GET_LOADER_PUBLICATION = "{call Loader_GetPublication(?) }";
	private final String CMD_EDIT_VERSION = "{call Loader_EditVersion(?,?,?,?) }";
	private final String CMD_IMPORT_PUBLICATION = "{call ImportPublication(?) }";
	private final String CMD_PARTIAL_IMPORT_PUBLICATION = "{call PartialImportPublication(?) }";
	private final String CMD_SAVE_PUBLICATION = "{call Loader_Parent(?) }";
	private final String CMD_GET_NEXT_PUBLICATION_VERSION = "{call loader_getnextpublicationversion(?) }";

	public String getNextPublicationVersion(String sessionKey)
			throws SQLException, DaoException {
		Double nextVersion = 0.0;
		try (SQLConnection connection = getSQLConnection()) {
			try (CallableStatement callstatement = connection
					.prepareCall(CMD_GET_NEXT_PUBLICATION_VERSION)) {
				callstatement.setString(1, sessionKey);
				if (callstatement.execute()) {
					ResultSet rs = callstatement.getResultSet();
					/*
					 * Code to check is the resultset is for Error message, If
					 * yes throws DAOException and cascades to calling methods
					 */
					DaoException.getInstanceIfAvailable(rs);

					rs.next();

					// No errors from resultset, so proceed the data retreival.
					//
					nextVersion = rs.getDouble(1);
				}
			}
		} catch (SQLException e) {

			String message = StringUtils
					.format("Error while getting nextPublicationVersion in getNextPublicationVersion method {0}",
							e.getMessage());
			printStackTraceToLogger(message, e);

			throw e;
		}
		return nextVersion.toString();
	}

	public List<Publication> getPublications(String publisherKey,
			String subjectKey) throws SQLException, DaoException {

		ArrayList<Publication> list = new ArrayList<Publication>();
		try (SQLConnection connection = getSQLConnection()) {
			try (CallableStatement callstatement = connection
					.prepareCall(CMD_GET_PUBLICATION)) {
				callstatement.setString(1, publisherKey);
				callstatement.setString(2, subjectKey);

				if (callstatement.execute()) {
					ResultSet rs = callstatement.getResultSet();
					/*
					 * Code to check is the resultset is for Error message, If
					 * yes throws DAOException and cascades to calling methods
					 */
					DaoException.getInstanceIfAvailable(rs);

					// No errors from resultset, so proceed the data retreival.
					list = (ArrayList<Publication>) getPublications(rs);
				}
			}
		} catch (SQLException e) {

			String message = StringUtils
					.format("Error while get publications in getPublications method {0}",
							e.getMessage());
			printStackTraceToLogger(message, e);

			throw e;
		}

		return list;

	}

	public Publication getAPublication(String publicationKey)
			throws MultipleDataException, SQLException, DaoException {
		try {
			List<Publication> listOfPublications = getPublications(
					publicationKey, null);
			if (listOfPublications != null && listOfPublications.size() != 0) {
				if (listOfPublications.size() == 1)
					return listOfPublications.get(0);
				else {
					throw new MultipleDataException(1,
							listOfPublications.size());
				}
			}
		} catch (SQLException e) {

			String message = StringUtils
					.format("Error while get APublication in getAPublication method {0}",
							e.getMessage());
			printStackTraceToLogger(message, e);

			throw e;
		}
		return null;
	}

	public void modifyPublicationStatus(String publicationKey, String status)
			throws SQLException {

		try (SQLConnection connection = getSQLConnection()) {
			try (CallableStatement callstatement = connection
					.prepareCall(CMD_MODIFY_PUBLICATION_STATUS)) {
				callstatement.setString(1, publicationKey);
				callstatement.setString(2, status);
				callstatement.execute();
			}
		} catch (SQLException e) {

			String message = StringUtils
					.format("Error while modifying Publication in modifyPublicationStatus method {0}",
							e.getMessage());
			printStackTraceToLogger(message, e);

			throw e;
		}
	}

	private List<Publication> getPublications(ResultSet resultset)
			throws SQLException {

		ArrayList<Publication> list = new ArrayList<Publication>();
		try {
			while (resultset.next()) {

				Publication publication = new Publication();
				publication.setKey(resultset.getString("PublicationKey"));
				publication.setVersion(resultset
						.getDouble("PublicationVersion"));
				publication.setDescription(resultset
						.getString("PublicationDescription"));
				publication.setFkPublisher(resultset.getString("publisher"));
				publication.setFkSubject(resultset.getString("subject"));
				publication.setDate(resultset.getDate("_date"));
				publication.setUrl(resultset.getString("url"));
				publication.setStatus(resultset.getString("status"));
				publication
						.setSubjectLabel(resultset.getString("subjectLabel"));
				publication.setCanonical(resultset.getBoolean("iscanonical"));
				list.add(publication);
			}

		} catch (SQLException e) {

			String message = StringUtils
					.format("Error while geting Publications in getPublications method {0}",
							e.getMessage());
			printStackTraceToLogger(message, e);

			throw e;
		}
		return list;
	}

	private List<Publication> getPublicationsBySessionKey(ResultSet resultset)
			throws SQLException {

		ArrayList<Publication> list = new ArrayList<Publication>();
		try {
			while (resultset.next()) {

				Publication publication = new Publication();
				publication.setKey(resultset.getString("PublisherName"));
				publication.setDescription(resultset
						.getString("PublicationDescription"));
				publication.setSubjectLabel(resultset.getString("Subject"));
				publication.setVersion(resultset.getInt("Version"));

				list.add(publication);
			}

		} catch (SQLException e) {

			String message = StringUtils
					.format("Error while geting Publications by session key in getPublicationsBySessionKey method {0}",
							e.getMessage());
			printStackTraceToLogger(message, e);

			throw e;
		}
		return list;
	}

	public String copyPublication(String sessionKey,
			Publication publication) throws SQLException, DaoException {
		String sesKey = null;
		try (SQLConnection connection = getSQLConnection()) {

			LoaderTablesDAO loaderTablesDAO = new LoaderTablesDAO();
			loaderTablesDAO.loaderClear(sessionKey);
			try (CallableStatement callstatement = connection
					.prepareCall(CMD_COPY_PUBLICATION)) {
				callstatement.setString(1, sessionKey);
				callstatement.setString(2, publication.getKey());

				if (callstatement.execute()) {
					ResultSet rs = callstatement.getResultSet();
					/*
					 * Code to check is the resultset is for Error message, If
					 * yes throws DAOException and cascades to calling methods
					 */
					DaoException.getInstanceIfAvailable(rs);

					// No errors from resultset, so proceed the data retreival.
					rs.next();
					sesKey = rs.getString(1);
				}
			}
		} catch (SQLException e) {

			String message = StringUtils
					.format("Error while copying to new Publications in copyToNewPublication method {0}",
							e.getMessage());
			printStackTraceToLogger(message, e);

			throw e;
		}
		return sesKey;

	}

	public String editPublication(String sessionKey,
			Publication publication) throws SQLException, DaoException {
		String sesKey = null;
		try (SQLConnection connection = getSQLConnection()) {

			LoaderTablesDAO loaderTablesDAO = new LoaderTablesDAO();
			loaderTablesDAO.loaderClear(sessionKey);
			try (CallableStatement callstatement = connection
					.prepareCall(CMD_EDIT_PUBLICATION)) {
				callstatement.setString(1, sessionKey);
				callstatement.setString(2, publication.getKey());

				if (callstatement.execute()) {
					ResultSet rs = callstatement.getResultSet();
					/*
					 * Code to check is the resultset is for Error message, If
					 * yes throws DAOException and cascades to calling methods
					 */
					DaoException.getInstanceIfAvailable(rs);

					// No errors from resultset, so proceed the data retreival.
					rs.next();
					sesKey = rs.getString(1);
				}
			}
		} catch (SQLException e) {

			String message = StringUtils
					.format("Error while copying to new Publications in editToNewPublication method {0}",
							e.getMessage());
			printStackTraceToLogger(message, e);

			throw e;
		}
		return sesKey;

	}

	public List<Publication> getPublicationsBySessionKey(String sessionKey)
			throws SQLException, DaoException {
		ArrayList<Publication> list = new ArrayList<Publication>();
		try (SQLConnection connection = getSQLConnection()) {
			try (CallableStatement callstatement = connection
					.prepareCall(CMD_GET_LOADER_PUBLICATION)) {
				callstatement.setString(1, sessionKey);

				if (callstatement.execute()) {
					ResultSet rs = callstatement.getResultSet();
					/*
					 * Code to check is the resultset is for Error message, If
					 * yes throws DAOException and cascades to calling methods
					 */
					DaoException.getInstanceIfAvailable(rs);

					// No errors from resultset, so proceed the data retreival.
					list = (ArrayList<Publication>) getPublicationsBySessionKey(rs);
				}
			}
		} catch (SQLException e) {

			String message = StringUtils
					.format("Error while Publications by session key in getPublicationsBySessionKey method {0}",
							e.getMessage());
			printStackTraceToLogger(message, e);

			throw e;
		}

		return list;
	}

	public List<Publication> editPublicationVersion(String sessionKey,
			Publication publication) throws SQLException, DaoException {
		ArrayList<Publication> list = new ArrayList<Publication>();
		try (SQLConnection connection = getSQLConnection()) {
			try (CallableStatement callstatement = connection
					.prepareCall(CMD_EDIT_VERSION)) {
				callstatement.setString(1, sessionKey);
				callstatement.setString(2, publication.getFkPublisher());
				callstatement.setString(3, publication.getFkSubject());
				callstatement.setInt(4, (int) publication.getVersion());

				if (callstatement.execute()) {
					ResultSet rs = callstatement.getResultSet();
					/*
					 * Code to check is the resultset is for Error message, If
					 * yes throws DAOException and cascades to calling methods
					 */
					DaoException.getInstanceIfAvailable(rs);
				}
				// No errors from resultset, so proceed the data retreival.
				list = (ArrayList<Publication>) getPublicationsBySessionKey(sessionKey);
			}
		} catch (SQLException e) {

			String message = StringUtils
					.format("Error while editing Publications by session key in editPublication method {0}",
							e.getMessage());
			printStackTraceToLogger(message, e);

			throw e;
		}

		return list;
	}

	public String importPublication(String sessionKey) throws SQLException,
			DaoException {
		String sesKey = null;
		try (SQLConnection connection = getSQLConnection()) {
			try (CallableStatement callstatement = connection
					.prepareCall(CMD_IMPORT_PUBLICATION)) {
				callstatement.setString(1, sessionKey);

				if (callstatement.execute()) {
					ResultSet rs = callstatement.getResultSet();
					/*
					 * Code to check is the resultset is for Error message, If
					 * yes throws DAOException and cascades to calling methods
					 */
					DaoException.getInstanceIfAvailable(rs);

					// No errors from resultset, so proceed the data retreival.
					rs.next();
					sesKey = rs.getString(1);
				}
			}
		} catch (SQLException e) {

			String message = StringUtils
					.format("Error while importing Publications by session key in importPublication method {0}",
							e.getMessage());
			printStackTraceToLogger(message, e);

			throw e;
		}
		return sesKey;

	}

	public String partialImportPublication(String sessionKey)
			throws SQLException, DaoException {
		String sesKey = null;
		try (SQLConnection connection = getSQLConnection()) {
			try (CallableStatement callstatement = connection
					.prepareCall(CMD_PARTIAL_IMPORT_PUBLICATION)) {
				callstatement.setString(1, sessionKey);
				if (callstatement.execute()) {
					ResultSet rs = callstatement.getResultSet();
					/*
					 * Code to check is the resultset is for Error message, If
					 * yes throws DAOException and cascades to calling methods
					 */
					DaoException.getInstanceIfAvailable(rs);

					// No errors from resultset, so proceed the data retreival.
					rs.next();
					sesKey = rs.getString(1);
				}
			}
		} catch (SQLException e) {

			String message = StringUtils
					.format("Error while partial importing Publications by session key in partialImportPublication method {0}",
							e.getMessage());
			printStackTraceToLogger(message, e);

			throw e;
		}
		return sesKey;
	}

	public ReturnStatus<List<Publication>> saveAllPublication(String sessionKey)
			throws SQLException, DaoException {
		ReturnStatus<List<Publication>> status = new ReturnStatus<List<Publication>>(
				"success");		
		ArrayList<Publication> list = new ArrayList<Publication>();
		try (SQLConnection connection = getSQLConnection()) {
			try (CallableStatement callstatement = connection
					.prepareCall(CMD_SAVE_PUBLICATION)) {
				callstatement.setString(1, sessionKey);
				if (callstatement.execute()) {
					ResultSet rs = callstatement.getResultSet();
					
					/*
					 * Code to check is the resultset is for Error message, If
					 * yes throws DAOException and cascades to calling methods
					 */
					DaoException.getInstanceIfAvailable(rs);
					
					while (rs.next()) {
						//severity, object, error
						if ("fatal".equalsIgnoreCase(rs.getString(1))) {
							status.appendToErrors(rs.getString(3));
							status.setStatus("failed");
						}
					}
					
				}
				// No errors from resultset, so proceed the data retreival.
				list = (ArrayList<Publication>) getPublicationsBySessionKey(sessionKey);
				status.setPayload(list);				
			}
		} catch (SQLException e) {
			_logger.error("Error while saving all Publications by session key in saveAllPublication method");

			String message = StringUtils
					.format("Error while saving all Publications by session key in saveAllPublication method {0}",
							e.getMessage());
			printStackTraceToLogger(message, e);

			throw e;
		}

		return status;
	}
}
