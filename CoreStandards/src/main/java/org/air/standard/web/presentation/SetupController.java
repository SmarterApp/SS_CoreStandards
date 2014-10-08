/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.web.presentation;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.air.standard.common.GradeLevel;
import org.air.standard.common.Publisher;
import org.air.standard.common.Subject;
import org.air.standard.controller.AbstractController;
import org.air.standard.delegate.GradeDelegate;
import org.air.standard.delegate.PublicationDelegate;
import org.air.standard.delegate.PublisherDelegate;
import org.air.standard.delegate.SubjectDelegate;
import org.air.standard.exceptions.ContentStandardException;
import org.air.standard.exceptions.DaoException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Scope ("prototype")
@Controller
public class SetupController extends AbstractController
{
  // todo: @Secured(role="ROLE_USER")
  @RequestMapping (value = "setup")
  public String renderSetup (HttpServletRequest request, HttpServletResponse response, @RequestParam (value = "setupBtnClicked", required = false) String setupButtonClickedType) {
    String path = "pages/setup.jsp";
    if (setupButtonClickedType != null)
    {
      path += "?setupBtnClicked=" + setupButtonClickedType;
      if (setupButtonClickedType.equals ("publisher"))
      {
        PublisherDelegate publisherDelegate = new PublisherDelegate ();
        try {
          List<Publisher> pubs = publisherDelegate.getPublishers (null);
          SubjectDelegate sDao = new SubjectDelegate ();
          PublicationDelegate pDao = new PublicationDelegate ();
          HashMap<Publisher, Boolean> publishers = new HashMap<Publisher, Boolean> ();
          for (Publisher pub : pubs)
          {
            boolean deleteButton = true;
            List<Subject> subs = sDao.getSubjectsByPublisher (pub.getKey ());
            for (Subject sub : subs)
            {
              if (pDao.getPublications (pub.getKey (), sub.getKey ()).size () > 0)
              {
                deleteButton = false;
                break;
              }
            }
            publishers.put (pub, deleteButton);
          }
          request.setAttribute ("publishers", publishers);
        } catch (ContentStandardException | DaoException e) {
        }
      }
      else if (setupButtonClickedType.equals ("grade"))
      {
        GradeDelegate gradeDelegate = new GradeDelegate ();
        try {
          List<GradeLevel> grades = gradeDelegate.getGradeLevels (null, null);
          request.setAttribute ("grades", grades);
        } catch (ContentStandardException | DaoException e) {
        }
      }
      else if (setupButtonClickedType.equals ("subject"))
      {
        SubjectDelegate subjectDelegate = new SubjectDelegate ();
        try {
          List<Subject> subjects = subjectDelegate.getSubjects (null);
          request.setAttribute ("subjects", subjects);
        } catch (ContentStandardException | DaoException e) {
        }
      }
    }
    /*
     * request.setAttribute ("myDisplayList", Arrays.<String> asList (new
     * String[] { "1", "2", "3", "4", "5" }));
     */
    return path;
  }
}
