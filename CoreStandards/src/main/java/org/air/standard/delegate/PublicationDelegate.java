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

import org.air.standard.common.Publication;
import org.air.standard.common.ReturnStatus;
import org.air.standard.common.exception.MultipleDataException;
import org.air.standard.dao.PublicationDAO;
import org.air.standard.exceptions.ContentStandardException;
import org.air.standard.exceptions.DaoException;

public class PublicationDelegate {

	private PublicationDAO dao = new PublicationDAO();

	public List<Publication> getPublicationsBySessionKey(String sessionKey)
			throws ContentStandardException, DaoException {
		try {
			return dao.getPublicationsBySessionKey(sessionKey);
		} catch (SQLException exp) {
			throw new ContentStandardException(exp);
		}
	}

	public List<Publication> editPublicationVersion(String sessionKey,
			Publication publication) throws ContentStandardException,
			DaoException {
		try {
			return dao.editPublicationVersion(sessionKey, publication);
		} catch (SQLException exp) {
			throw new ContentStandardException(exp);
		}
	}

	public ReturnStatus<List<Publication>> saveAllPublication(String sessionKey)
			throws ContentStandardException, DaoException {
		try {
			return dao.saveAllPublication(sessionKey);
		} catch (SQLException exp) {
			throw new ContentStandardException(exp);
		}
	}

	public String copyToNewPublication(String sessionKey,
			Publication publication) throws ContentStandardException,
			DaoException {
		try {
			return dao.copyPublication(sessionKey, publication);
		} catch (SQLException exp) {
			throw new ContentStandardException(exp);
		}
	}

	public String editToNewPublication(String sessionKey,
			Publication publication) throws ContentStandardException,
			DaoException {
		try {
			return dao.editPublication(sessionKey, publication);
		} catch (SQLException exp) {
			throw new ContentStandardException(exp);
		}
	}

	public List<Publication> getPublications(String publisherKey,
			String subjectKey) throws ContentStandardException, DaoException {
		try {
			return dao.getPublications(publisherKey, subjectKey);
		} catch (SQLException exp) {
			throw new ContentStandardException(exp);
		}
	}

	public Publication getAPublication(String publicationKey)
			throws ContentStandardException, MultipleDataException,
			DaoException {
		try {
			return dao.getAPublication(publicationKey);
		} catch (SQLException exp) {
			throw new ContentStandardException(exp);
		}
	}
	
	public String getNextVersion (String sessionKey)
			throws ContentStandardException, DaoException {
		try {
			return dao.getNextPublicationVersion(sessionKey);
		} catch (SQLException exp) {
			throw new ContentStandardException(exp);
		}
	}

}
