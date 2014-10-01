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

import org.air.standard.dao.LoaderTablesDAO;
import org.air.standard.exceptions.ContentStandardException;
import org.air.standard.exceptions.DaoException;

public class LoaderTablesDelegate {

	LoaderTablesDAO loaderTablesDAO = new LoaderTablesDAO();

	public void loaderClear(String sessionKey)
			throws ContentStandardException, DaoException {
		try {
			loaderTablesDAO.loaderClear(sessionKey);
		} catch (SQLException exp) {
			throw new ContentStandardException(exp);
		}
	}
}
