/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.controller;


import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.air.standard.common.ReturnStatus;
import org.air.standard.common.Sock;
import org.air.standard.dao.AbstractTest;
import org.air.standard.exceptions.ContentStandardException;
import org.air.standard.exceptions.ContentStandardSecurityException;
import org.air.standard.exceptions.DaoException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author temp_rbandi
 *
 */
public class SocksControllerTest extends AbstractTest
{
  SocksController socksController;
 private static   Logger logger;

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass () throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass () throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp () throws Exception {
    super.setUp ();
    socksController = new SocksController();
    logger = Logger.getLogger("");
    logger.setLevel(java.util.logging.Level.INFO);
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown () throws Exception {
  }

  /**
   * Test method for {@link org.air.standard.controller.SocksController#getSocksByPublicationKey(java.lang.String)}.
   * @throws DaoException 
   * @throws ContentStandardException 
   * @throws ContentStandardSecurityException 
   */
  @Test
  public void testGetSocksByPublicationKey () throws ContentStandardSecurityException, ContentStandardException, DaoException {
    String publication_key="SBAC-ELA-v1";
    
    ReturnStatus<List<Sock>> socksList = socksController.getSocksByPublicationKey (publication_key);
    logger.info("size::"+ socksList.getPayload ().size ());
    Assert.assertTrue("NO SOCKS ARE AVAILABLE WITH GIVEN PUBLICATION KEY", socksList.getPayload ().size() > 0);
    Iterator<Sock> iterator=socksList.getPayload().iterator ();
    
    while(iterator.hasNext ()){
    	Sock sOCKs=iterator.next();
      if("Usage/grammar".equals (sOCKs.getKnowledgeCategory())){
    	  
    	  logger.info("SOCKs Publication...."+ sOCKs.getFkPublication());
    	  logger.info("SOCKs key...."+ sOCKs.getSocksKey());
         logger.info("SOCKs Description...."+ sOCKs.getDescription());  
         logger.info("SOCKs KnowledgeCategory...."+ sOCKs.getKnowledgeCategory()); 
      
      
    }
 
  }
    
  }

}
