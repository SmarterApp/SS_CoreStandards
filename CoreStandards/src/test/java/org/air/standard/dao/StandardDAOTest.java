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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.air.standard.common.Standard;
import org.air.standard.common.exception.InvalidArgumentException;
import org.air.standard.exceptions.DaoException;

/**
 * @author temp_rreddy
 * 
 */
public class StandardDAOTest extends AbstractTest {

	StandardDAO standardDAO;

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
		standardDAO = new StandardDAO();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetStandards() throws InvalidArgumentException,
			SQLException, DaoException {
		// Positive Testing
		List<Standard> standardList = standardDAO.getStandards("SBAC-ELA-v1");
		Assert.assertTrue(
				"NO STANDARD ARE AVAILABLE WITH GIVEN PUBLICATION KEY",
				standardList.size() > 0);

		// //Negative Testing
		// List<Standard> standardNegList = standardDAO.getStandards("SBAC");
		// Assert.assertTrue("NO STANDARD ARE AVAILABLE WITH GIVEN PUBLICATION KEY",
		// standardNegList.size() > 0);
	}

	@Test
	public void testGetStandard() throws SQLException, DaoException {
		// Positive Testing
		List<Standard> standardList = standardDAO.getStandards("SBAC-ELA-v1",
				"11", true);
		Assert.assertTrue(
				"NO STANDARD ARE AVAILABLE WITH GIVEN PUBLICATION KEY AND GRADE KEY",
				standardList.size() > 0);

		// //Negative Testing
		// List<Standard> standardNegList = standardDAO.getStandards("SBAC",
		// "11");
		// Assert.assertTrue("NO STANDARD ARE AVAILABLE WITH GIVEN PUBLICATION KEY AND GRADE KEY",
		// standardNegList.size() > 0);
	}

}
