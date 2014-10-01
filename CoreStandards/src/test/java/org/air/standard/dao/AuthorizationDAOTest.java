/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import org.air.standard.common.User;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author temp_rreddy
 *
 */
public class AuthorizationDAOTest extends AbstractTest{

	AuthorizationDAO authorizationDAO;
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
		authorizationDAO = new AuthorizationDAO();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		authorizationDAO = null;
	}

	@Test
	public void testValidateUser() throws SQLException{
		boolean boolValue = authorizationDAO.validateUser("USERNAME","PASSWORD");	
		assertTrue(boolValue);
		fail("Invalid User");
	}
	
	@Test
	public void testGetUser() throws SQLException{				
		User user = authorizationDAO.getUser("USERNAME", "PASSWORD");	
		assertEquals(user.getUsername(), "USERNAME");
		fail("Not yet implemented");
	}

}
