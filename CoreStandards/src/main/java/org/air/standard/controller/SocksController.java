/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.controller;

import java.util.List;

import org.air.standard.common.LoaderSock;
import org.air.standard.common.ReturnStatus;
import org.air.standard.common.Sock;
import org.air.standard.delegate.SocksDelegate;
import org.air.standard.exceptions.ContentStandardException;
import org.air.standard.exceptions.ContentStandardSecurityException;
import org.air.standard.exceptions.DaoException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Scope ("prototype")
@Controller
public class SocksController extends AbstractController
{

  SocksDelegate socksDelegate = new SocksDelegate ();

  @RequestMapping (method = RequestMethod.GET, value = "staging/sock")
  public @ResponseBody
  ReturnStatus<List<LoaderSock>> getSocksBySessionKey ()
      throws ContentStandardSecurityException, ContentStandardException,
      DaoException {
    ReturnStatus<List<LoaderSock>> status = new ReturnStatus<List<LoaderSock>> (
        "success");
    String sessionKey = getUserInformation ().getSessionKey ();
    status.setPayload (socksDelegate.getSocksBySessionKey (sessionKey));
    return status;
  }

  @RequestMapping (value = "staging/sock", method = RequestMethod.PUT)
  public @ResponseBody
  ReturnStatus<List<LoaderSock>> editSock (@RequestBody LoaderSock sock)
      throws ContentStandardSecurityException, ContentStandardException,
      DaoException {
    String sessionKey = getUserInformation ().getSessionKey ();
    ReturnStatus<List<LoaderSock>> status = new ReturnStatus<List<LoaderSock>> (
        "success");
    status.setPayload (socksDelegate.editSock (sessionKey, sock));
    return status;
  }

  @RequestMapping (method = RequestMethod.GET, value = "publication/{publicationKey}/sock")
  public @ResponseBody
  ReturnStatus<List<Sock>> getSocksByPublicationKey (
      @PathVariable String publicationKey)
      throws ContentStandardSecurityException, ContentStandardException,
      DaoException {
    ReturnStatus<List<Sock>> status = new ReturnStatus<List<Sock>> ("success");
    status.setPayload (socksDelegate.getSocksByPublicationKey (publicationKey));
    return status;
  }

}
