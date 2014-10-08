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

import org.air.standard.common.Publication;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author temp_rbandi
 *
 */
public class PublicationControllerTest extends AbstractTest
{
  PublicationController publicationController;
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
    
    publicationController = new PublicationController();
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
   * Test method for {@link org.air.standard.controller.PublicationController#getPublicationsBySubjectPublisher(java.lang.String, java.lang.String)}.
   * @throws DaoException 
   * @throws ContentStandardException 
   * @throws ContentStandardSecurityException 
   */
  
  @Test
  public void testGetPublicationsBySubjectPublisher () throws ContentStandardSecurityException, ContentStandardException, DaoException {
   String publisher_key="SBAC";
    //String subject_key="ELA";
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addParameter ("publisher", "SBAC");
     request.addParameter ("subject","ELA");
         ServletWebRequest webRequest = new ServletWebRequest(request);
    
    ReturnStatus<List<Publication>> publicationList = publicationController.getPublicationsBySubjectPublisher (webRequest);
    logger.info("size::"+ publicationList.getPayload ().size ());
    Assert.assertTrue("NO Publications ARE AVAILABLE for the given Publisher and Subject", publicationList.getPayload ().size() > 0);
    Iterator<Publication> iterator=publicationList.getPayload().iterator ();
    while(iterator.hasNext ()){
    	Publication publication=iterator.next();
        if("SBAC-ELA-v1".equals (publication.getKey())){
        	
        	logger.info("publication FKPublisher...."+ publication.getFkPublisher());
        	logger.info("publication key ...."+ publication.getKey());
        	logger.info("publication version...."+ publication.getVersion());
        	logger.info("publication Description...."+ publication.getDescription());
        	logger.info("publication Subject...."+ publication.getFkSubject());
        	logger.info("publication SubjectLabel...."+ publication.getSubjectLabel());
        	logger.info("publication Status...."+ publication.getStatus());
               

        
      }
   
    }
    
  }

}
