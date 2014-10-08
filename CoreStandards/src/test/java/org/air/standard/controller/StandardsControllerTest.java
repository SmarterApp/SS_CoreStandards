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
import org.air.standard.common.Standard;
import org.air.standard.common.exception.InvalidArgumentException;
import org.air.standard.dao.AbstractTest;
import org.air.standard.exceptions.ContentStandardException;
import org.air.standard.exceptions.DaoException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author temp_rbandi
 *
 */
public class StandardsControllerTest extends AbstractTest
{
  StandardsController standardsController;  
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
   
    standardsController= new StandardsController();
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
   * Test method for {@link org.air.standard.controller.StandardsController#getStandardsByPublication(java.lang.String)}.
   * @throws DaoException 
   * @throws ContentStandardException 
   * @throws InvalidArgumentException 
   */
 /* @Test
  public void testGetStandardsByPublication () throws InvalidArgumentException, ContentStandardException, DaoException {
    //String publication_key="SBAC-ELA-v1";
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addParameter ("publication_key", "SBAC-ELA-v1");
    ServletWebRequest webRequest = new ServletWebRequest(request);
    
    ReturnStatus<List<Standard>> standardList= standardsController.getStandardsByPublication(webRequest);
    logger.info("size::"+ standardList.getPayload ().size ());
    Assert.assertTrue("NO STANDARDS ARE AVAILABLE WITH GIVEN PUBLICATION KEY", standardList.getPayload ().size() > 0);
    Iterator<Standard> iterator=standardList.getPayload().iterator ();
    while(iterator.hasNext ()){
    	Standard standard=iterator.next();
     if("SBAC-ELA-v1:1-IT".equals(standard.getKey()) || "SBAC-ELA-v1:1-IT|10-1".equals (standard.getKey())){
    	 
    	 logger.info("Standard key...."+ standard.getKey());
      
     
        logger.info("Standard key...."+ standard.getKey());
        logger.info("Standard name...."+ standard.getName());
        logger.info("Standard parent...."+ standard.getFkParent());  
        logger.info("Standard publication...."+ standard.getFkPublication());
        logger.info("Standard Desc...."+ standard.getDescription());
        logger.info("Standard treelevel...."+ standard.getTreeLevel());
        logger.info("Standard gradelevel...."+ standard.getFkGradeLevel());
        logger.info("Standard shortname...."+ standard.getShortName());  
    }
 
  }
  }
*/
  /**
   * Test method for {@link org.air.standard.controller.StandardsController#getStandards(java.lang.String, java.lang.String)}.
   * @throws DaoException 
   * @throws ContentStandardException 
   * @throws InvalidArgumentException 
   */
  @Test
  public void testGetStandards () throws InvalidArgumentException, ContentStandardException, DaoException 
  {
  String publicationKey="SBAC-ELA-v1";
 //String grade_key="1";
    MockHttpServletRequest request = new MockHttpServletRequest();
   // request.addParameter ("publication_key", "SBAC-ELA-v1");
    request.addParameter ("grade","1");
        ServletWebRequest webRequest = new ServletWebRequest(request);
   
    
    ReturnStatus<List<Standard>> standardList= standardsController.getStandards (publicationKey,webRequest);
    logger.info("size::"+ standardList.getPayload ().size ());
    Assert.assertTrue ("NO STANDARDS ARE AVAILABLE WITH GIVEN PUBLICATION KEY and Grade", standardList.getPayload ().size() > 0);
    Iterator<Standard> iterator=standardList.getPayload().iterator ();
    while(iterator.hasNext ()){
    	Standard standard=iterator.next();
     if("SBAC-ELA-v1:1-IT".equals(standard.getKey()) || "SBAC-ELA-v1:1-IT|10-1".equals (standard.getKey())){
     
     
    	 logger.info("Standard key...."+ standard.getKey());
         logger.info("Standard name...."+ standard.getName());
         logger.info("Standard parent...."+ standard.getFkParent());  
         logger.info("Standard publication...."+ standard.getFkPublication());
         logger.info("Standard Desc...."+ standard.getDescription());
         logger.info("Standard treelevel...."+ standard.getTreeLevel());
         logger.info("Standard gradelevel...."+ standard.getFkGradeLevel());
         logger.info("Standard shortname...."+ standard.getShortName());  
    }
 
  }
   
  }

}
