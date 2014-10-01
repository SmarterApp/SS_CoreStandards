/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
/**
 * 
 */
package org.air.standard.dao;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.air.standard.common.*;
import org.air.standard.exceptions.DaoException;

/**
 * @author temp_rreddy
 * 
 */
public class LoaderTablesDAOTest extends AbstractTest {

	LoaderTablesDAO loaderTablesDAO;
	LoaderPublication loader_Pulication = new LoaderPublication();
	LoaderCategory loader_Categories = new LoaderCategory();
	LoaderStandard loader_Standards = new LoaderStandard();
	LoaderBenchmarkGrade loader_BenchmarkGrades = new LoaderBenchmarkGrade();
	LoaderSock loader_SOCKs = new LoaderSock();
	LoaderStandardRelationship loader_StandardRelationship = new LoaderStandardRelationship();

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
		loaderTablesDAO = new LoaderTablesDAO();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAdd_Loader_Publication() throws SQLException, DaoException {
		loader_Pulication.setPublicationDescription("DESC11");
		loader_Pulication.setPublisherName("NAME11");
		loader_Pulication.setSubject("SUBJECT11");
		loader_Pulication.setVersion(2);

		loaderTablesDAO.addLoaderPublication(loader_Pulication);
		// fail("Not yet implemented");
	}

	@Test
	public void testAdd_Loader_Categories() throws SQLException, DaoException {
		loader_Categories.setCategory("CATEGORY1");
		loader_Categories.setTreeLevel(4);

		loaderTablesDAO.addLoaderCategories(loader_Categories);
		// fail("Not yet implemented");
	}

	@Test
	public void testAdd_Loader_Standards() throws SQLException, DaoException {
		loader_Standards.setDescription("DESC2");
		loader_Standards.setKey("KEY2");
		loader_Standards.setLevel(3);
		loader_Standards.setName("NAME2");
		loader_Standards.setShortName("SHORTNAME2");

		loaderTablesDAO.addLoaderStandards(loader_Standards);
		// fail("Not yet implemented");
	}

	@Test
	public void testAdd_Loader_BenchmarkGrades() throws SQLException,
			DaoException {
		loader_BenchmarkGrades.setBenchmark("BENCHMARK1");
		loader_BenchmarkGrades.setGrade("GRADE1");

		loaderTablesDAO.addLoaderBenchmarkGrades(loader_BenchmarkGrades);
		// fail("Not yet implemented");
	}

	@Test
	public void testAdd_Loader_SOCKs() throws SQLException, DaoException {
		loader_SOCKs.setDescription("DESCRIPTION1");
		loader_SOCKs.setKnowledgeCategory("KNOWLEDGE CATEGORY1");

		loaderTablesDAO.addLoaderSOCKs(loader_SOCKs);
		// fail("Not yet implemented");
	}

	@Test
	public void testAdd_Loader_StandardRelationship() throws SQLException,
			DaoException {
		loader_StandardRelationship.setKeyA("keyA1");
		loader_StandardRelationship.setKeyB("keyB1");
		loader_StandardRelationship.setRelationshipType("relshipType1");

		loaderTablesDAO
				.addLoaderStandardRelationship(loader_StandardRelationship);
		// fail("Not yet implemented");
	}

	@Test
	public void testLoader_Clear() {
		// loaderTablesDAO.loaderClear();
		fail("Not yet implemented");
	}
}
