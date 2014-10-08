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

import org.air.standard.common.GradeLevel;
import org.air.standard.common.LoaderBenchmarkGrade;
import org.air.standard.common.ReturnStatus;
import org.air.standard.delegate.GradeDelegate;
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

/**
 * Handles requests for the Grades.
 */
@Scope ("prototype")
@Controller
public class GradeController extends AbstractController
{
  private GradeDelegate gradeDelegate = new GradeDelegate ();

  @RequestMapping (method = RequestMethod.GET, value = "staging/grade")
  public @ResponseBody
  ReturnStatus<List<LoaderBenchmarkGrade>> getBenchmarkGradesBySessionKey ()
      throws ContentStandardSecurityException, ContentStandardException,
      DaoException {
    String sessionKey = getUserInformation ().getSessionKey ();
    ReturnStatus<List<LoaderBenchmarkGrade>> status = new ReturnStatus<List<LoaderBenchmarkGrade>> (
        "success");
    status.setPayload (gradeDelegate
        .getBenchmarkGradesBySessionKey (sessionKey));
    return status;
  }

  @RequestMapping (method = RequestMethod.GET, value = "grade")
  public @ResponseBody
  ReturnStatus<List<GradeLevel>> getAllBenchmarkGrades ()
      throws ContentStandardSecurityException, ContentStandardException,
      DaoException {
    ReturnStatus<List<GradeLevel>> status = new ReturnStatus<List<GradeLevel>> (
        "success");
    status.setPayload (gradeDelegate.getGradeLevels (null, null));
    return status;
  }

  /*
   * gets grade levels by gradeKey and publicationKey. URL:
   * http://localhost:8080
   * /contentstandards/REST/Grades/Grade/Get/Publication/{publicationKey}
   */
  @RequestMapping (method = RequestMethod.GET, value = "publication/{publicationKey}/grade")
  public @ResponseBody
  ReturnStatus<List<GradeLevel>> getGradeLevels (
      @PathVariable String publicationKey)
      throws ContentStandardSecurityException, ContentStandardException,
      DaoException {
    ReturnStatus<List<GradeLevel>> status = new ReturnStatus<List<GradeLevel>> (
        "success");
    // get rid of all
    if ("all".equalsIgnoreCase (publicationKey))
      publicationKey = null;
    status.setPayload (gradeDelegate.getGradeLevels (null, publicationKey));
    return status;
  }

  @RequestMapping (method = RequestMethod.POST, value = "grade")
  public @ResponseBody
  ReturnStatus<GradeLevel> addGrade (@RequestBody GradeLevel gradeLevel)
      throws ContentStandardSecurityException, ContentStandardException,
      DaoException {
    ReturnStatus<GradeLevel> status = new ReturnStatus<GradeLevel> ("success");
    String sessionKey = getUserInformation ().getSessionKey ();
    gradeDelegate.addGrade (sessionKey, gradeLevel);
    status.setPayload (gradeLevel);
    return status;
  }

  // TODO Shajib: which class to use GradeLevel or LoaderBenchMarkGrade
  @RequestMapping (method = RequestMethod.PUT, value = "grade")
  public @ResponseBody
  ReturnStatus<GradeLevel[]> editGrade (@RequestBody GradeLevel[] gradeLevel)
      throws ContentStandardSecurityException, ContentStandardException,
      DaoException {
    ReturnStatus<GradeLevel[]> status = new ReturnStatus<GradeLevel[]> ("success");
    String sessionKey = getUserInformation ().getSessionKey ();
    gradeDelegate.editGrade (gradeLevel[0], gradeLevel[1]);
    status.setStatus ("success");
    status.setPayload (gradeLevel);
    return status;
  }

  @RequestMapping (value = "staging/grade", method = RequestMethod.PUT)
  public @ResponseBody
  ReturnStatus<List<LoaderBenchmarkGrade>> editGrade (
      @RequestBody LoaderBenchmarkGrade grade)
      throws ContentStandardSecurityException, ContentStandardException,
      DaoException {
    String sessionKey = getUserInformation ().getSessionKey ();
    ReturnStatus<List<LoaderBenchmarkGrade>> status = new ReturnStatus<List<LoaderBenchmarkGrade>> (
        "success");
    status.setPayload (gradeDelegate.editGrade (sessionKey, grade));
    return status;
  }

  // TODO Shajib: which class to use? GradeLevel or LoaderBenchMarkGrade
  @RequestMapping (method = RequestMethod.DELETE, value = "grade")
  public @ResponseBody
  ReturnStatus<GradeLevel> deleteGrade (@RequestBody GradeLevel gradeLevel)
      throws ContentStandardSecurityException, ContentStandardException,
      DaoException {
    ReturnStatus<GradeLevel> status = new ReturnStatus<GradeLevel> ("success");
    gradeDelegate.deleteGrade (gradeLevel);
    status.setStatus ("success");
    status.setPayload (gradeLevel);
    return status;
  }

  @RequestMapping (value = "staging/grade", method = RequestMethod.DELETE)
  public @ResponseBody
  ReturnStatus<List<LoaderBenchmarkGrade>> deleteGrade (
      @RequestBody LoaderBenchmarkGrade grade)
      throws ContentStandardSecurityException, ContentStandardException,
      DaoException {
    String sessionKey = getUserInformation ().getSessionKey ();
    ReturnStatus<List<LoaderBenchmarkGrade>> status = new ReturnStatus<List<LoaderBenchmarkGrade>> (
        "success");
    status.setPayload (gradeDelegate.deleteGrade (sessionKey, grade));
    return status;
  }

}
