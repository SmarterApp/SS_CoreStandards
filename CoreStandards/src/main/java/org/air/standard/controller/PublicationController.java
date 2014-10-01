/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.controller;

import java.util.List;
import java.util.Map;

import org.air.shared.utils.StringUtils;
import org.air.standard.common.Publication;
import org.air.standard.common.ReturnStatus;
import org.air.standard.common.exception.MultipleDataException;
import org.air.standard.delegate.PublicationDelegate;
import org.air.standard.exceptions.ContentStandardException;
import org.air.standard.exceptions.ContentStandardSecurityException;
import org.air.standard.exceptions.DaoException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

/*
 * todo sb: using MatrixVariable requires us to use Spring 3.2 framework. import
 * org.springframework.web.bind.annotation.MatrixVariable;
 */
@Scope("prototype")
@Controller
public class PublicationController extends AbstractController {

	PublicationDelegate publicationDelegate = new PublicationDelegate();

	/*
	 * get next publication version by sessionkey
	 */
	@RequestMapping(method = RequestMethod.GET, value = "staging/publication/nextVersion")
	public @ResponseBody
	ReturnStatus<String> getNextVersionBySessionKey()
			throws ContentStandardSecurityException, ContentStandardException,
			DaoException {
		String sessionKey = getUserInformation().getSessionKey();
		ReturnStatus<String> status = new ReturnStatus<String>("success");
		status.setPayload(publicationDelegate.getNextVersion(sessionKey));
		return status;

	}

	/*
	 * get publications by sessionkey
	 */
	@RequestMapping(method = RequestMethod.GET, value = "staging/publication")
	public @ResponseBody
	ReturnStatus<List<Publication>> getPublicationsBySessionKey()
			throws ContentStandardSecurityException, ContentStandardException,
			DaoException {
		String sessionKey = getUserInformation().getSessionKey();
		ReturnStatus<List<Publication>> status = new ReturnStatus<List<Publication>>(
				"success");
		status.setPayload(publicationDelegate
				.getPublicationsBySessionKey(sessionKey));
		return status;

	}

	/*
	 * copy publications
	 */
	@RequestMapping(value = "staging/publication", method = RequestMethod.POST)
	public @ResponseBody
	ReturnStatus<Publication> copyPublication(
			@RequestBody Publication publication)
			throws ContentStandardSecurityException, ContentStandardException,
			DaoException {
		ReturnStatus<Publication> status = new ReturnStatus<Publication>(
				"success");
		String sessionKey = getUserInformation().getSessionKey();
		String sess = publicationDelegate.copyToNewPublication(sessionKey,
				publication);
		status.setPayload(publication);
		return status;
	}

	/**
	 * Edits publication version for a publication in staging tables.
	 * Publication is identified by publisher name, subject name and version
	 * number.
	 * 
	 * @param Publication
	 *            A publication of interest.
	 * @return Return status with a publication in payload.
	 * @throws ContentStandardSecurityException
	 *             If user has not been authenticated.
	 * @throws ContentStandardException
	 *             If incorrect data is supplied.
	 * @throws DaoException
	 *             If database access error occurs.
	 */
	@RequestMapping(value = "staging/publication/version", method = RequestMethod.PUT)
	public @ResponseBody
	ReturnStatus<List<Publication>> editPublicationVersion(
			@RequestBody Publication publication) throws MultipleDataException,
			ContentStandardSecurityException, ContentStandardException,
			DaoException {
		ReturnStatus<List<Publication>> status = new ReturnStatus<List<Publication>>(
				"success");
		String sessionKey = getUserInformation().getSessionKey();
		status.setPayload(publicationDelegate.editPublicationVersion(
				sessionKey, publication));
		return status;
	}

	@RequestMapping(value = "staging/publication", method = RequestMethod.PUT)
	public @ResponseBody
	ReturnStatus<Publication> editPublication(
			@RequestBody Publication publication) throws MultipleDataException,
			ContentStandardSecurityException, ContentStandardException,
			DaoException {
		ReturnStatus<Publication> status = new ReturnStatus<Publication>(
				"success");
		String sessionKey = getUserInformation().getSessionKey();
		publicationDelegate.editToNewPublication(sessionKey, publication);
		status.setPayload(publication);
		return status;
	}

	/*
	 * Edit publications
	 */
	@RequestMapping(value = "publication", method = RequestMethod.POST)
	public @ResponseBody
	ReturnStatus<List<Publication>> saveAllPublication()
			throws MultipleDataException, ContentStandardException,
			ContentStandardSecurityException, DaoException {

		String sessionKey = getUserInformation().getSessionKey();
		return publicationDelegate.saveAllPublication(sessionKey);
	}

	/**
	 * Retrieves a list of publications. Filters publications by a given subject
	 * and/or publisher if specified as query parameters. If either subject or
	 * publisher (or both) are not given, this method does not filter
	 * publication list by these criteria.
	 * 
	 * @param webReqVal
	 *            Web request.
	 * @return Return status with a list of publications in payload.
	 * @throws ContentStandardSecurityException
	 *             If user has not been authenticated.
	 * @throws ContentStandardException
	 *             If incorrect data is supplied.
	 * @throws DaoException
	 *             If database access error occurs.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "publication")
	public @ResponseBody
	ReturnStatus<List<Publication>> getPublicationsBySubjectPublisher(
			WebRequest webReqVal) throws ContentStandardSecurityException,
			ContentStandardException, DaoException {
		ReturnStatus<List<Publication>> status = new ReturnStatus<List<Publication>>(
				"success");
		Map<String, String[]> reqMap = webReqVal.getParameterMap();

		if (reqMap.containsKey("publisher")
				&& !StringUtils.isEmpty(reqMap.get("publisher")[0])
				&& reqMap.containsKey("subject")
				&& !StringUtils.isEmpty(reqMap.get("subject")[0])) {
			String subValue = reqMap.get("subject")[0];
			status.setPayload(publicationDelegate.getPublications(
					reqMap.get("publisher")[0], subValue));
		} else if (reqMap.containsKey("publisher")
				&& !StringUtils.isEmpty(reqMap.get("publisher")[0])
				&& !reqMap.containsKey("subject")) {
			status.setPayload(publicationDelegate.getPublications(
					reqMap.get("publisher")[0], null));
		} else if (!reqMap.containsKey("publisher")
				&& reqMap.containsKey("subject")
				&& !StringUtils.isEmpty(reqMap.get("subject")[0])) {
			String subValue = reqMap.get("subject")[0];
			status.setPayload(publicationDelegate.getPublications(null,
					subValue));
		} else {
			// Retrieving all publications.
			status.setPayload(publicationDelegate.getPublications(null, null));
		}

		return status;
	}

}
