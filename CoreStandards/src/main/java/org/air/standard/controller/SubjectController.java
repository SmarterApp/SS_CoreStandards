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
import org.air.standard.common.Subject;
import org.air.standard.delegate.SubjectDelegate;
import org.air.standard.exceptions.ContentStandardException;
import org.air.standard.exceptions.ContentStandardSecurityException;
import org.air.standard.exceptions.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

/**
 * Handles requests for the subjects.
 */
@Scope ("prototype")
@Controller
public class SubjectController extends AbstractController
{

  SubjectDelegate subjectDelegate = new SubjectDelegate ();
 private static final  Logger          _logger         = LoggerFactory
                                      .getLogger (SubjectController.class);

  /*
   * gets list of subjects by a publisher Key api/subject?publisher=SBAC merge
   * with the previous api and retrieve search parameters from a maps
   */
  @RequestMapping (method = RequestMethod.GET, value = "subject")
  public @ResponseBody
  ReturnStatus<List<Subject>> getSubjectByPublisherKey (WebRequest webReqVal)
      throws ContentStandardException, DaoException {
    ReturnStatus<List<Subject>> status = new ReturnStatus<List<Subject>> (
        "success");
    Map<String, String[]> reqMap = webReqVal.getParameterMap ();

    if (reqMap.containsKey ("publisher")
        && !StringUtils.isEmpty (reqMap.get ("publisher")[0])) {
      status.setPayload (subjectDelegate.getSubjectsByPublisher (reqMap
          .get ("publisher")[0]));
    } else {
      // Retrieving all subjects.
      status.setPayload (subjectDelegate.getSubjects (null));
    }
    return status;
  }

  /*
   * gets a subject by a subjectKey
   */
  @RequestMapping (method = RequestMethod.GET, value = "subject/{subjectKey}")
  public @ResponseBody
  ReturnStatus<List<Subject>> getSubjectByName (@PathVariable String subjectKey)
      throws ContentStandardException, DaoException {
    ReturnStatus<List<Subject>> status = new ReturnStatus<List<Subject>> (
        "success");
    status.setPayload (subjectDelegate.getSubjects (subjectKey));
    return status;
  }

  /*
   * Add subject.
   */
  @RequestMapping (method = RequestMethod.POST, value = "subject")
  public @ResponseBody
  ReturnStatus<Subject> addSubject (@RequestBody Subject newSubject)
      throws ContentStandardSecurityException, ContentStandardException,
      DaoException {
    String sessionKey = getUserInformation ().getSessionKey ();
    ReturnStatus<Subject> status = new ReturnStatus<Subject> ("success");
    subjectDelegate.addSubject (sessionKey, newSubject);
    status.setPayload (newSubject);
    return status;

  }

  @RequestMapping (method = RequestMethod.DELETE, value = "subject")
  public @ResponseBody
  ReturnStatus<Subject> deleteSubject (@RequestBody Subject subject)
      throws ContentStandardSecurityException, ContentStandardException,
      DaoException {
    ReturnStatus<Subject> status = new ReturnStatus<Subject> ("success");
    subjectDelegate.deleteSubject (subject);
    status.setPayload (subject);
    status.setStatus ("success");
    return status;
  }

  @RequestMapping (method = RequestMethod.PUT, value = "subject")
  public @ResponseBody
  ReturnStatus<Subject[]> editSubject (@RequestBody Subject[] subject)
      throws ContentStandardSecurityException, ContentStandardException,
      DaoException {
    ReturnStatus<Subject[]> status = new ReturnStatus<Subject[]> ("success");
    subjectDelegate.editSubject (subject[0], subject[1]);
    status.setPayload (subject);
    status.setStatus ("success");
    return status;
  }

}
