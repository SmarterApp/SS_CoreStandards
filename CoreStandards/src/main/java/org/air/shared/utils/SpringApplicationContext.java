/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.shared.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author mpatel
 *
 */
public class SpringApplicationContext implements ApplicationContextAware
{

  private static ApplicationContext _applicationContext = null;
  private static final Object       _SYNC_OBJECT        = new Object ();

  public void setApplicationContext (ApplicationContext context) throws BeansException {
    if (_applicationContext == null)
    {
      synchronized (_SYNC_OBJECT) {
        if (_applicationContext == null)
          _applicationContext = context;
      }
    }
  }

  public static <T> T getBean (String beanName, final Class<T> clazz)
  {
    return _applicationContext.getBean (beanName, clazz);
  }
  
  public static <T> T getBean (final Class<T> clazz)
  {
    return _applicationContext.getBean (clazz);
  }
}
