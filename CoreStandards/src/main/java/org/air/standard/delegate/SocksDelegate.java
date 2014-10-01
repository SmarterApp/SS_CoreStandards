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

import org.air.standard.common.LoaderSock;
import org.air.standard.common.Sock;
import org.air.standard.dao.SocksDAO;
import org.air.standard.exceptions.ContentStandardException;
import org.air.standard.exceptions.DaoException;
import org.springframework.web.bind.annotation.PathVariable;

public class SocksDelegate {

	SocksDAO socksDao = new SocksDAO();

	public List<LoaderSock> getSocksBySessionKey(
			@PathVariable String sessionKey) throws ContentStandardException,
			DaoException {
		try {
			return socksDao.getSocksBySessionKey(sessionKey);
		} catch (SQLException exp) {
			throw new ContentStandardException(exp);
		}
	}

	public List<LoaderSock> editSock(String sessionKey, LoaderSock sock)
			throws ContentStandardException, DaoException {
		try {
			return socksDao.editSock(sessionKey, sock);
		} catch (SQLException exp) {
			throw new ContentStandardException(exp);
		}
	}
	
	public List<Sock> getSocksByPublicationKey(
			@PathVariable String publicationKey) throws ContentStandardException,
			DaoException {
		try {
			return socksDao.getSocksByPublicationKey(publicationKey);
		} catch (SQLException exp) {
			throw new ContentStandardException(exp);
		}
	}

	

}
