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
import java.util.List;

import org.air.standard.common.Category;
import org.air.standard.common.LoaderCategory;
import org.air.standard.dao.CategoryDAO;
import org.air.standard.exceptions.ContentStandardException;
import org.air.standard.exceptions.DaoException;

public class CategoryDelegate {

	CategoryDAO categoryDao = new CategoryDAO();

	public List<LoaderCategory> getCategoryBySessionKey(String sessionKey)
			throws ContentStandardException, DaoException {
		try {
			List<LoaderCategory> categoryList = categoryDao
					.getCategoryBySessionKey(sessionKey);
			return categoryList;
		} catch (SQLException exp) {
			throw new ContentStandardException(exp);
		}
	}

	public List<LoaderCategory> editCategory(String sessionKey,
			LoaderCategory category) throws ContentStandardException,
			DaoException {
		try {
			return categoryDao.editCategory(sessionKey, category);
		} catch (SQLException exp) {
			throw new ContentStandardException(exp);
		}
	}
	public List<Category> getCategoryByPublicationKey(String publicationKey)
			throws ContentStandardException, DaoException {
		try {
			List<Category> categoryList = categoryDao
					.getCategoryByPublicationKey(publicationKey);
			return categoryList;
			
		} catch (SQLException exp) {
			throw new ContentStandardException(exp);
		}
	}
}
