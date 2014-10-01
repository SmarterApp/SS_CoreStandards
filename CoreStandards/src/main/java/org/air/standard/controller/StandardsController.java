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
import java.util.Map;

import org.air.shared.utils.StringUtils;
import org.air.standard.common.ReturnStatus;
import org.air.standard.common.Standard;
import org.air.standard.common.exception.InvalidArgumentException;
import org.air.standard.dao.StandardDAO;
import org.air.standard.delegate.StandardsDelegate;
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
import org.springframework.web.context.request.WebRequest;

/**
 * Handles requests for the Standards.
 */
@Scope ("prototype")
@Controller
public class StandardsController extends AbstractController
{
  private StandardDAO mStandardDao     = new StandardDAO ();
  StandardsDelegate   standardDelegate = new StandardsDelegate ();

  @RequestMapping (method = RequestMethod.GET, value = "staging/standard")
  public @ResponseBody
  ReturnStatus<List<Standard>> getStandardsBySessionKey ()
      throws InvalidArgumentException, ContentStandardSecurityException,
      ContentStandardException, DaoException {
    String sessionKey = getUserInformation ().getSessionKey ();
    ReturnStatus<List<Standard>> status = new ReturnStatus<List<Standard>> (
        "success");
    status.setPayload (standardDelegate.getStandardsBySessionKey (sessionKey));
    return status;
  }

  @RequestMapping (value = "staging/standard", method = RequestMethod.PUT)
  public @ResponseBody
  ReturnStatus<List<Standard>> editStandard (@RequestBody Standard standard)
      throws ContentStandardSecurityException, ContentStandardException,
      DaoException {
    String sessionKey = getUserInformation ().getSessionKey ();
    ReturnStatus<List<Standard>> status = new ReturnStatus<List<Standard>> (
        "success");
    status.setPayload (standardDelegate.editStandard (sessionKey, standard));
    return status;
  }

  /*
   * merge with getPublicationsBySubjectPublisher
   * api/publication/{publicationKey}/standard?grade={gradeKey}
   */

  @RequestMapping (method = RequestMethod.GET, value = "publication/{publicationKey}/standard")
  public @ResponseBody
  ReturnStatus<List<Standard>> getStandards (
      @PathVariable String publicationKey, WebRequest webReqVal)
      throws ContentStandardException, DaoException, InvalidArgumentException {

    ReturnStatus<List<Standard>> status = new ReturnStatus<List<Standard>> (
        "success");

    Map<String, String[]> reqMap = webReqVal.getParameterMap ();

    if (reqMap.isEmpty () && !"".equals (publicationKey)) {

      status.setPayload (standardDelegate.getStandards (publicationKey));
    } else if (!"".equals (publicationKey) && reqMap.containsKey ("grade")
        && !StringUtils.isEmpty (reqMap.get ("grade")[0])) {

      status.setPayload (standardDelegate.getStandards (publicationKey,
          reqMap.get ("grade")[0], true));
    }
    return status;
  }
}
