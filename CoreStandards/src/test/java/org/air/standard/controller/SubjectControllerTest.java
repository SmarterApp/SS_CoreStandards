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
import org.air.standard.common.Subject;
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
public class SubjectControllerTest extends AbstractTest
{
  SubjectController subjectController;
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
    super.setUp();
    subjectController = new SubjectController();
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
   * Test method for {@link org.air.standard.controller.SubjectController#getSubjectByPublisherKey(java.lang.String)}.
   * @throws DaoException 
   * @throws ContentStandardException 
   */
  @Test
  public void testGetSubjectByPublisherKey () throws ContentStandardException, DaoException {
    //String publisher_key="SBAC";
   // HttpServletRequest servletRequest = new HttpServletRequest();
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addParameter ("publisher_key", "SBAC");
    ServletWebRequest webRequest = new ServletWebRequest(request);
   
    ReturnStatus<List<Subject>> subjectList= subjectController.getSubjectByPublisherKey (webRequest);
    logger.info("size::"+ subjectList.getPayload ().size ());
    Assert.assertTrue("No Subjects ARE AVAILABLE For the GIVEN PUBLISHER KEY", subjectList.getPayload().size() > 0);
    Iterator<Subject> iterator=subjectList.getPayload().iterator ();
    while(iterator.hasNext ()){
      Subject subject=iterator.next();
      if("ELA".equals (subject.getKey ()) || "MA".equals (subject.getKey ())){
    	  
    	  logger.info("Subject key...."+ subject.getKey ());
    	  logger.info("Subject Code...."+ subject.getCode ());
    	  logger.info("Subject Name...."+ subject.getName ());
     

    }
 
  }

  /**
   * Test method for {@link org.air.standard.controller.SubjectController#getSubjectByName(java.lang.String)}.
   */


}
}
