/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.controller;

import javax.servlet.http.HttpServletResponse;

import org.air.standard.common.ReturnStatus;
import org.air.standard.common.exception.InvalidArgumentException;
import org.air.standard.common.exception.MultipleDataException;
import org.air.standard.exceptions.ContentStandardException;
import org.air.standard.exceptions.ContentStandardSecurityException;
import org.air.standard.exceptions.DaoException;
import org.air.standard.web.authentication.IUserInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public abstract class AbstractController implements BeanFactoryAware
{

  private static final Logger              _logger       = LoggerFactory.getLogger (AbstractController.class);

  private BeanFactory _contextBeans = null;

  @ExceptionHandler ({ ContentStandardException.class,
      InvalidArgumentException.class, MultipleDataException.class,
      ContentStandardSecurityException.class })
  @ResponseBody
  public ReturnStatus<Exception> handleContentStandardException (Exception exp,
      HttpServletResponse response) {
    printErrorToLog (exp);
    ReturnStatus<Exception> status = new ReturnStatus<Exception> ("failed");
    status.setPayload (exp);
    response.setStatus (HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    return status;
  }

  @ExceptionHandler ({ DaoException.class })
  @ResponseBody
  public ReturnStatus<Exception> handleDaoException (Exception exp,
      HttpServletResponse response) {
    printErrorToLog (exp);
    ReturnStatus<Exception> status = new ReturnStatus<Exception> ("failed");
    status.setPayload (null);
    status.appendToErrors (exp.getMessage ());
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return status;
  }

  @Override
  public void setBeanFactory (BeanFactory beanFactory) throws BeansException {
    // TODO Auto-generated method stub
    /*
     * to get bean in a servlet:
     * 
     * ApplicationContext beanFactory =
     * WebApplicationContextUtils.getRequiredWebApplicationContext
     * (getServletContext());
     */
    this._contextBeans = beanFactory;
  }

  protected BeanFactory getContextBeans () {
    return _contextBeans;
  }

  protected IUserInformation getUserInformation () {
    IUserInformation userInformationBean = getContextBeans ().getBean (
        "iUserInformation", IUserInformation.class);
    return userInformationBean;
  }

  private void printErrorToLog (Exception exp) {
    _logger.error (exp.getMessage ());
    _logger.error (exp.getSuppressed ().toString ());
  }

}
