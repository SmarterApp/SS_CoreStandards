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

import org.air.standard.common.Subject;
import org.air.standard.exceptions.DaoException;

/**
 * @author temp_rreddy
 * 
 */
public class SubjectDAOTest extends AbstractTest {

	SubjectDAO subjectDAO;

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
		subjectDAO = new SubjectDAO();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		subjectDAO = null;
	}

	@Test
	public void test() throws SQLException, DaoException {
		// Positive Testing
		List<Subject> subjectList = subjectDAO.getSubjects("SBAC");
		Assert.assertTrue("NO SUBJECT ARE AVAILABLE WITH GIVEN PUBLISHER KEY",
				subjectList.size() > 0);

		// Negative Testing
		// List<Subject> subjectListNeg = subjectDAO.getSubjects("EAC");
		// Assert.assertTrue("NO SUBJECT ARE AVAILABLE WITH GIVEN PUBLISHER KEY",
		// subjectListNeg.size() > 0);

	}

}
