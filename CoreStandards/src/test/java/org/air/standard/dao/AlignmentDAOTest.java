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

import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.air.standard.common.Alignment;
import org.air.standard.dao.AlignmentDAO;

/**
 * @author temp_rreddy
 *
 */
public class AlignmentDAOTest extends AbstractTest{

	AlignmentDAO alignmentDao;
	Alignment alignment = null;
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
		alignmentDao = new AlignmentDAO();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		alignment = null;
	}

	@Test
	public void testGetAlignment() {		
		alignment = alignmentDao.getAlignment("TESTA", "TESTB");
		assertTrue(alignment.getStatus()!=null);
		}
	
	@Test
	public void testModifyAlignment() {		
		alignment = alignmentDao.modifyAlignment("TESTA", "TESTB","STATUS", "USERNAME");
		assertEquals(alignment.getModified(), new java.util.Date());		
		}
}
