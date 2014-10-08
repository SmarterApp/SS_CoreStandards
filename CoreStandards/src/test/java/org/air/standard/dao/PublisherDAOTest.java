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
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.air.standard.common.Publisher;
import org.air.standard.exceptions.DaoException;

/**
 * @author temp_rreddy
 * 
 */
public class PublisherDAOTest extends AbstractTest {

	PublisherDAO publisherDAO;

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
		publisherDAO = new PublisherDAO();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		// publisherDAO = null;
	}

	@Test
	public void testGetPublishers() throws SQLException, DaoException {
		// Positive Testing
		List<Publisher> publisher = publisherDAO.getPublishers("SBAC");
		Assert.assertTrue(
				"NO PUBLISHERS ARE AVAILABLE WITH GIVEN PUBLISHER KEY",
				publisher.size() > 0);

		// //Negative Testing
		// List<Publisher> publisherNeg = publisherDAO.getPublishers("SAC");
		// Assert.assertTrue("NO PUBLISHERS ARE AVAILABLE WITH GIVEN PUBLISHER KEY",
		// publisherNeg.size() > 0);
	}

}
