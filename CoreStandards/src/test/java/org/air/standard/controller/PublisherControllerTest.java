/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.controller;


import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.air.standard.common.Publisher;
import org.air.standard.common.ReturnStatus;
import org.air.standard.common.exception.MultipleDataException;
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
public class PublisherControllerTest extends AbstractTest
{
//  PublisherDelegate publisherDelegate;
  PublisherController publisherController;
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
 //    publisherDelegate = new PublisherDelegate();
     publisherController=new PublisherController ();
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
   * Test method for {@link org.air.standard.controller.PublisherController#getAllPublishers()}.
   * @throws DaoException 
   * @throws SQLException 
   * @throws ContentStandardException 
   * @throws ContentStandardSecurityException 
   */
  @Test
  public void testGetAllPublishers () throws SQLException, DaoException, ContentStandardException, ContentStandardSecurityException {
  
  
    ReturnStatus<List<Publisher>> publishersList= publisherController.getAllPublishers ();
    logger.info("size::"+ publishersList.getPayload ().size ());
    Assert.assertNotNull ("NO PUBLISHERS ARE AVAILABLE", publishersList.getPayload ());
    Iterator<Publisher> iterator=publishersList.getPayload ().iterator ();
    while(iterator.hasNext ()){
      Publisher publisher=(Publisher) iterator.next();
      if("SBAC".equals (publisher.getKey ())){
    	  
    	  logger.info("Publisher key...."+ publisher.getKey ());
    	  logger.info("Publisher value...."+ publisher.getName ());
         

        }
      
    }
  
  }

  /**
   * Test method for {@link org.air.standard.controller.PublisherController#getPublisher(java.lang.String)}.
   * @throws DaoException 
   * @throws ContentStandardException 
   * @throws MultipleDataException 
   */
  @Test
  public void testGetPublisher () throws MultipleDataException, ContentStandardException, DaoException {
    String publisher_key="SBAC";
    ReturnStatus<Publisher> list= publisherController.getPublisher(publisher_key);
    logger.info("size::"+ list.getPayload ());
       Assert.assertNotNull ("NO PUBLISHERS ARE AVAILABLE WITH GIVEN PUBLISHER KEY", list.getPayload ());
    Publisher publisher=list.getPayload ();
    if("SBAC".equals (publisher.getKey ())){
      
    	 logger.info("Publisher key...."+ publisher.getKey ());
    	 logger.info("Publisher value.."+ publisher.getName ());
      //System.out.println("Publisher key."+ publisher.getKey ());
     // System.out.println("Publisher value.."+ publisher.getName ());
    }
       
  
   
  }

}
