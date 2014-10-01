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

import org.air.standard.common.ReturnStatus;
import org.air.standard.dao.LoaderTablesDAO;
import org.air.standard.exceptions.ContentStandardSecurityException;
import org.air.standard.exceptions.DaoException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Scope ("prototype")
@Controller
public class SessionController extends AbstractController
{
  // todo: @Secured(role="ROLE_USER")
  @RequestMapping (method = RequestMethod.DELETE, value = "staging")
  public @ResponseBody
  ReturnStatus<String> clearLoaderTablesBySessionKey () throws SQLException,
      ContentStandardSecurityException, DaoException {
    String sessionKey = getUserInformation ().getSessionKey ();
    ReturnStatus<String> status = new ReturnStatus<String> ("success");
    LoaderTablesDAO dao = new LoaderTablesDAO ();
    dao.loaderClear (sessionKey);
    // return new ReturnStatus("success");
    return status;
  }

}
