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

import org.air.standard.common.Relationship;
import org.air.standard.common.AddDeleteRelationshipStatus;
import org.air.standard.exceptions.DaoException;

/**
 * @author temp_rreddy
 * 
 */
public class RelationshipDAOTest extends AbstractTest {
	RelationshipDAO relationshipDAO;

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
		relationshipDAO = new RelationshipDAO();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetRelationships() throws SQLException, DaoException {
		// Positive Testing
		List<Relationship> relationshipList = relationshipDAO.getRelationships(
				"SBAC-ELA-v1", "SBAC-ELA-v1");
		Assert.assertTrue(
				"NO RELATIONSHIP ARE AVAILABLE WITH GIVEN PUBLICATION KEYS",
				relationshipList.size() > 0);
		// Negative Testing
		// List<Relationship> relationshipNegList =
		// relationshipDAO.getRelationships("SBACELA-v1", "SBAC-EL-v1");
		// Assert.assertTrue("NO RELATIONSHIP ARE AVAILABLE WITH GIVEN PUBLICATION KEYS",
		// relationshipNegList.size() > 0);
	}

	@Test
	public void testAddRelationship() throws SQLException, DaoException {
		// Positive Testing
		boolean boolValue = relationshipDAO.addRelationship(
				"SBAC-ELA-v1:1-IT|10-10", "SBAC-MA-v2:3|NF", "prereq");
		Assert.assertTrue(
				"NOT ADDED RELATIONSHIP WITH GIVEN  STANDARD A,STANDARD B, PUBLICATION KEYS VALUES",
				boolValue);
		// Negative Testing
		// boolean boolNegValue =
		// relationshipDAO.addRelationship("SBAC-ELA:1-IT|10-10",
		// "SBAC-v2:3|NF", "prereq");
		// Assert.assertTrue("NOT ADDED RELATIONSHIP WITH GIVEN  STANDARD A,STANDARD B, PUBLICATION KEYS VALUES",
		// boolNegValue);

	}

	@Test
	public void testRemoveRelationship() throws SQLException, DaoException {
		// Positive Testing
		boolean boolValue = relationshipDAO.removeRelationship(
				"SBAC-ELA-v1:1-IT|10-10", "SBAC-MA-v2:3|NF", "prereq");
		Assert.assertTrue("NOT REMOVED RELATIONSHIPS", boolValue);

	}

	@Test
	public void testAddRelationshipResult() throws SQLException, DaoException {
		// Positive Testing
		AddDeleteRelationshipStatus addDeleteRelationshipStatus = relationshipDAO
				.addRelationshipResult("SBAC-ELA-v1:1-IT|10-11",
						"SBAC-ELA-v1:1-IT|10-3", "prereq");
		assertEquals(addDeleteRelationshipStatus.getStatus(), "failed");

	}

	@Test
	public void testRemoveRelationshipResult() throws SQLException,
			DaoException {
		// Positive Testing
		AddDeleteRelationshipStatus addDeleteRelationshipStatus = relationshipDAO
				.removeRelationshipResult("SBAC-ELA-v1:1-IT|10-11",
						"SBAC-ELA-v1:1-IT|10-3", "prereq");
		Assert.assertTrue("NOT REMOVED RELATIONSHIPS WITH GIVEN VALUES",
				addDeleteRelationshipStatus.getStatus().equals("true"));
	}

}
