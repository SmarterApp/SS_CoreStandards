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

import org.air.standard.common.Category;
import org.air.standard.common.LoaderCategory;
import org.air.standard.common.ReturnStatus;
import org.air.standard.delegate.CategoryDelegate;
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
public class CategoryController extends AbstractController
{

  CategoryDelegate categoryDelegate = new CategoryDelegate ();

  // @Secured(role="ROLE_USER")
  @RequestMapping (method = RequestMethod.GET, value = "staging/category")
  public @ResponseBody
  ReturnStatus<List<LoaderCategory>> getCategoriesBySessionKey ()
      throws ContentStandardSecurityException, ContentStandardException,
      DaoException {
    ReturnStatus<List<LoaderCategory>> status = new ReturnStatus<List<LoaderCategory>> (
        "success");

    String sessionKey = getUserInformation ().getSessionKey ();
    status.setPayload (categoryDelegate.getCategoryBySessionKey (sessionKey));
    return status;
  }

  @RequestMapping (value = "staging/category", method = RequestMethod.PUT)
  public @ResponseBody
  ReturnStatus<List<LoaderCategory>> editCategory (
      @RequestBody LoaderCategory category)
      throws ContentStandardSecurityException, ContentStandardException,
      DaoException {
    ReturnStatus<List<LoaderCategory>> status = new ReturnStatus<List<LoaderCategory>> (
        "success");
    String sessionKey = getUserInformation ().getSessionKey ();
    status.setPayload (categoryDelegate.editCategory (sessionKey, category));
    return status;
  }

  /*
   * gets list of standard categories by publication Key
   */
  @RequestMapping (method = RequestMethod.GET, value = "publication/{publicationKey}/category")
  public @ResponseBody
  ReturnStatus<List<Category>> getCategoryByPublicationKey (
      @PathVariable String publicationKey) throws ContentStandardException,
      DaoException {
    ReturnStatus<List<Category>> status = new ReturnStatus<List<Category>> (
        "success");
    status.setPayload (categoryDelegate
        .getCategoryByPublicationKey (publicationKey));
    return status;
  }

}
