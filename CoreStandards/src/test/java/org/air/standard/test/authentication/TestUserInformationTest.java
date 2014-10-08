/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.test.authentication;

import static org.junit.Assert.*;

import org.air.standard.dao.AbstractTest;
import org.air.standard.exceptions.ContentStandardSecurityException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author temp_rbandi
 *
 */
public class TestUserInformationTest extends AbstractTest
{
  TestUserInformation testUserInformation;

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
     testUserInformation=new TestUserInformation("a");
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown () throws Exception {
  }

  /**
   * Test method for {@link org.air.standard.test.authentication.TestUserInformation#getSessionKey()}.
   * @throws ContentStandardSecurityException 
   */
  @Test
  public void testGetSessionKey () throws ContentStandardSecurityException {
    String sessionKey=testUserInformation.getSessionKey ();
    System.out.println ("the value of sessionkey is::"+sessionKey);
    
    
  }

}
