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

import org.air.standard.common.Standard;
import org.air.standard.common.exception.InvalidArgumentException;
import org.air.standard.dao.StandardDAO;
import org.air.standard.exceptions.ContentStandardException;
import org.air.standard.exceptions.DaoException;

public class StandardsDelegate {
	StandardDAO standardDao = new StandardDAO();

	public List<Standard> getStandards(String publicationKey)
			throws ContentStandardException, InvalidArgumentException,
			DaoException {
		try {
			List<Standard> standardList = standardDao
					.getStandards(publicationKey);
			return standardList;
		} catch (SQLException exp) {
			throw new ContentStandardException(exp);
		}
	}

	public List<Standard> getStandards(String publicationKey, String gradeKey,
			boolean gradeAgnostic) throws ContentStandardException,
			DaoException {
		try {
			List<Standard> standardList = standardDao.getStandards(
					publicationKey, gradeKey, gradeAgnostic);
			return standardList;
		} catch (SQLException exp) {
			throw new ContentStandardException(exp);
		}
	}

	public List<Standard> getStandardsBySessionKey(String sessionKey)
			throws InvalidArgumentException, ContentStandardException,
			DaoException {
		try {
			List<Standard> standardList = standardDao
					.getStandardsBySessionKey(sessionKey);
			return standardList;
		} catch (SQLException exp) {
			throw new ContentStandardException(exp);
		}

	}

	public List<Standard> editStandard(String sessionKey, Standard standard)
			throws ContentStandardException, DaoException {
		try {
			List<Standard> standardList = standardDao.editStandard(sessionKey,
					standard);
			return standardList;
		} catch (SQLException exp) {
			throw new ContentStandardException(exp);
		}
	}
}
