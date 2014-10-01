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

import org.air.standard.common.Category;
import org.air.standard.common.ReturnStatus;
import org.air.standard.dao.AbstractTest;
import org.air.standard.exceptions.ContentStandardException;
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
public class CategoryControllerTest extends AbstractTest
{
     
  CategoryController categoryController;
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
    categoryController = new CategoryController();
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
   * Test method for {@link org.air.standard.controller.CategoryController#getCategoryByPublicationKey(java.lang.String)}.
   * @throws DaoException 
   * @throws ContentStandardException 
   */
  @Test
  public void testGetCategoryByPublicationKey () throws ContentStandardException, DaoException {
    String publication_key="SBAC-ELA-v1";
    ReturnStatus<List<Category>> categoryList = categoryController.getCategoryByPublicationKey (publication_key);
    logger.info("size::"+ categoryList.getPayload ().size ());
    Assert.assertTrue ("NO CATEGORIES ARE AVAILABLE WITH GIVEN PUBLISHER KEY", categoryList.getPayload ().size () > 0);
    Iterator<Category> iterator=categoryList.getPayload().iterator ();
    while(iterator.hasNext ()){
    	Category category=iterator.next();
      if("SBAC Claim".equals (category.getName()) || "SBAC Assessment Target".equals (category.getName()) || "Common Core Standard".equals (category.getName())){
      
      logger.info("Category Name...."+ category.getName());
      logger.info("Category key...."+ category.getFkPublication());
      logger.info("Category TreeLevel...."+ category.getTreeLevel());
    }
 
  }

    
  }

}
