/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.dao;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.air.standard.common.Publication;
import org.air.standard.common.exception.MultipleDataException;
import org.air.standard.exceptions.DaoException;

/**
 * @author temp_rreddy
 * 
 */
public class PublicationDAOTest extends AbstractTest {

	PublicationDAO publicationDAO;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
		publicationDAO = new PublicationDAO();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetPublications() throws SQLException, DaoException {
		// Positive Testing
		// List<Publication> publicationList =
		// publicationDAO.getPublications("SBAC", "ELA");
		// Assert.assertTrue("NO PUBLICATIONS ARE AVAILABLE WITH GIVEN PUBLICATION KEY AND SUBJECT KEY",
		// publicationList.size() > 0);

		// Negative Testing
		List<Publication> publicationNegList = publicationDAO.getPublications(
				"SAC", "LA");
		Assert.assertTrue(
				"NO PUBLICATIONS ARE AVAILABLE WITH GIVEN PUBLICATION KEY AND SUBJECT KEY",
				publicationNegList.size() > 0);
	}

	@Test
	public void testGetPublication() throws SQLException, DaoException {
		// Positive Testing
		Publication publicationList = null;
		try {
			publicationList = publicationDAO.getAPublication("SBAC-ELA-v1");
		} catch (MultipleDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertTrue(
				"NO PUBLICATIONS ARE AVAILABLE WITH GIVEN PUBLICATION KEY",
				publicationList.getKey() != null);

		// //Negative Testing
		// List<Publication> publicationNegList =
		// publicationDAO.getPublications("SBAC-E-v1");
		// Assert.assertTrue("NO PUBLICATIONS ARE AVAILABLE WITH GIVEN PUBLICATION KEY",
		// publicationNegList.size() > 0);
	}

	@Test
	public void testGetAPublication() throws MultipleDataException,
			SQLException, DaoException {
		// Positive Testing
		Publication publication = publicationDAO.getAPublication("SBAC-ELA-v1");
		Assert.assertTrue(
				"NO PUBLICATIONS ARE AVAILABLE WITH GIVEN PUBLICATION KEY",
				publication.getKey() != null);

		// //Negative Testing
		// Publication publicationNeg =
		// publicationDAO.getAPublication("SBAC-EA-v1");
		// Assert.assertTrue("NO PUBLICATIONS ARE AVAILABLE WITH GIVEN PUBLICATION KEY",
		// publicationNeg.get_key() != null);
	}

	@Test
	public void testModifyPublicationStatus() throws SQLException {
		// Positive Testing
		publicationDAO.modifyPublicationStatus("SBAC-ELA-v1", "true");

	}

}
