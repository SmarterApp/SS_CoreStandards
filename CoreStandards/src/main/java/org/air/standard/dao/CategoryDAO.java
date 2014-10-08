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
import org.air.standard.common.Category;
import org.air.standard.common.LoaderCategory;
import org.air.standard.exceptions.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CategoryDAO extends AbstractDAO {
	
  private static final  Logger _logger = LoggerFactory.getLogger(CategoryDAO.class);

	private final String CMD_GET_CATEGORIES = "{call Loader_GetCategories(?) }";
	private final String CMD_MODIFY_CATEGORY = "{call Loader_EditCategory(?, ?, ?, ?) }";
	private final String CMD_GET_STANDARD_CATEGORY = "{call GetStandardCategory(?) }";

	public List<LoaderCategory> getCategoryBySessionKey(String sessionKey)
			throws SQLException, DaoException {

		ArrayList<LoaderCategory> list = new ArrayList<LoaderCategory>();
		try (SQLConnection connection = getSQLConnection()) {
			try (CallableStatement callstatement = connection
					.prepareCall(CMD_GET_CATEGORIES)){
			callstatement.setString(1, sessionKey);

			if (callstatement.execute()) {
				ResultSet rs = callstatement.getResultSet();
				/*
				 * Code to check is the resultset is for Error message, If yes
				 * throws DAOException and cascades to calling methods
				 */
				DaoException.getInstanceIfAvailable(rs);

				// No errors from resultset, so proceed the data retrieval.
				list = (ArrayList<LoaderCategory>) getLoaderCategories(rs);
			}
			}
		} catch (SQLException e) {
			
			String message = StringUtils.format("Error while getting categories by session key in getCategoryBySessionKey method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
							
			throw e;
		}

		return list;
	}

	private List<LoaderCategory> getLoaderCategories(ResultSet resultset)
			throws SQLException {

		ArrayList<LoaderCategory> list = new ArrayList<LoaderCategory>();
		try {
			while (resultset.next()) {

				LoaderCategory category = new LoaderCategory();

				category.setCategory(resultset.getString("Category"));
				category.setTreeLevel(Integer.parseInt(resultset
						.getString("treeLevel")));

				list.add(category);
			}

		} catch (SQLException e) {
			
			String message = StringUtils.format("Error while getting result set in getLoaderCategories method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
							
			throw e;
		}
		return list;
	}

	private List<Category> getCategories(ResultSet resultset)
			throws SQLException {

		ArrayList<Category> list = new ArrayList<Category>();
		try {
			while (resultset.next()) {

				Category category = new Category();

				category.setName(resultset.getString("name"));
				category.setTreeLevel(Integer.parseInt(resultset
						.getString("treeLevel")));
				category.setFkPublication(resultset.getString("_fk_publication"));

				list.add(category);
			}

		} catch (SQLException e) {
			
			String message = StringUtils.format("Error while getting result set in getCategories method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
							
			throw e;
		}
		return list;
	}

	public List<LoaderCategory> editCategory(String sessionKey,
			LoaderCategory category) throws SQLException, DaoException {

		ArrayList<LoaderCategory> listCategory = new ArrayList<LoaderCategory>();

		try (SQLConnection connection = getSQLConnection()) {
			try (CallableStatement callstatement = connection
					.prepareCall(CMD_MODIFY_CATEGORY)){
			callstatement.setString(1, sessionKey);
			callstatement.setString(2, category.getCategory());
			callstatement.setInt(3, category.getTreeLevel());
			callstatement.setString(4, category.getNewCategory());

			if (callstatement.execute()) {
				ResultSet rs = callstatement.getResultSet();
				/*
				 * Code to check is the resultset is for Error message, If yes
				 * throws DAOException and cascades to calling methods
				 */
				DaoException.getInstanceIfAvailable(rs);

				// No errors from resultset, so proceed the data retrieval.
				listCategory = (ArrayList<LoaderCategory>) getLoaderCategories(rs);
			}
			}
		} catch (SQLException e) {
			String message = StringUtils.format("Error while editing category in editCategory method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
					
			throw e;
		}

		return listCategory;
	}
	
	public List<Category> getCategoryByPublicationKey(String publicationKey)
			throws SQLException, DaoException {
		
		ArrayList<Category> list = new ArrayList<Category>();
		try (SQLConnection connection = getSQLConnection()) {
			try (CallableStatement callstatement = connection
					.prepareCall(CMD_GET_STANDARD_CATEGORY)){
			callstatement.setString(1, publicationKey);

			if (callstatement.execute()) {
				ResultSet rs = callstatement.getResultSet();
				/*
				 * Code to check is the result set is for Error message, If yes
				 * throws DAOException and cascades to calling methods
				 */
				DaoException.getInstanceIfAvailable(rs);

				// No errors from resultset, so proceed the data retreival.
				list = (ArrayList<Category>) getCategories(rs);
			}
			}
		} catch (SQLException e) {
			
			String message = StringUtils.format("Error while getting Category by publicationKey in getCategoryByPublicationKey method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
							
			throw e;
		}
		return list;
	}
}
