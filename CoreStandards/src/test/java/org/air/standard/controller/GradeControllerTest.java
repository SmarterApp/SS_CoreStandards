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

import org.air.standard.common.GradeLevel;
import org.air.standard.common.ReturnStatus;
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
public class GradeControllerTest extends AbstractTest
{
  
  GradeController gradeController;
 private static   Logger logger ;

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
    gradeController = new GradeController();
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
   * Test method for {@link org.air.standard.controller.GradeController#getGradeLevels(java.lang.String)}.
   * @throws DaoException 
   * @throws ContentStandardException 
   * @throws ContentStandardSecurityException 
   */
  @Test
  public void testGetGradeLevels () throws ContentStandardSecurityException, ContentStandardException, DaoException {
    String publication_key="SBAC-ELA-v1";
    ReturnStatus<List<GradeLevel>> gradeLevelList =  gradeController.getGradeLevels (publication_key);
    logger.info("size::"+ gradeLevelList.getPayload ().size ());
    Assert.assertTrue ("NO GRADES ARE AVAILABLE WITH GIVEN PUBLICATION KEY", gradeLevelList.getPayload ().size() > 0);
    Iterator<GradeLevel> iterator=gradeLevelList.getPayload().iterator ();
    while(iterator.hasNext ()){
    	GradeLevel gradeLevel=iterator.next();
      if("3".equals (gradeLevel.getKey())){
    	  
    	  logger.info("GradeLevel key...."+ gradeLevel.getKey());
          logger.info("GradeLevel Description...."+ gradeLevel.getDescription());
          logger.info("GradeLevel Name...."+ gradeLevel.getName ());
      
     

    }
 
  }
   
  }

}
