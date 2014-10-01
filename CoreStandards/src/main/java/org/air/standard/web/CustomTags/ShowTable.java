/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.web.CustomTags;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.air.shared.utils.StringUtils;
import org.air.standard.common.GradeLevel;
import org.air.standard.common.Publisher;
import org.air.standard.common.Subject;
import org.air.standard.delegate.GradeDelegate;
import org.air.standard.delegate.PublicationDelegate;
import org.air.standard.delegate.PublisherDelegate;
import org.air.standard.delegate.SubjectDelegate;
import org.air.standard.exceptions.ContentStandardException;
import org.air.standard.exceptions.DaoException;

public class ShowTable extends SimpleTagSupport
{
  private String message;
  private String styleClass;

  public void setMessage (String value) {
    this.message = value;
  }

  public void setStyleClass (String value)
  {
    this.styleClass = value;
  }

  StringWriter sw = new StringWriter ();

  public void doTag () throws JspException, IOException {
    if (!StringUtils.isEmpty (message)) {
      /* Use message from attribute */
      JspWriter out = getJspContext ().getOut ();
      String str = null;
      if (message.equals ("publisher"))
        str = this.populatePublisher ();
      else if (message.equals ("subject"))
        str = this.populateSubject ();
      else if (message.equals ("grade"))
        str = this.populateGrade ();
      out.print (str);
    }
    else {
      /* use message from the body */
      /*
       * getJspBody ().invoke (sw); getJspContext ().getOut ().println
       * (sw.toString ());
       */
    }
  }

  public String populatePublisher ()
  {
    List<Publisher> publishers = new ArrayList<Publisher> ();
    PublisherDelegate pDelegate = new PublisherDelegate ();
    try {
      publishers = pDelegate.getPublishers (null);
    } catch (ContentStandardException | DaoException e) {
    }
    SubjectDelegate sDelegate = new SubjectDelegate ();

    Collections.sort (publishers, new PublisherComparator ());

    String table = "<table border='1' align='center'> "
        + "<thead>"
        + "<th>Publisher Name</th>"
        + "<th>Publisher Key</th>"
        + "<th></th>"
        + "</thead>"
        + "<tbody>";

    for (Publisher pub : publishers)
    {
      table += "<tr>";
      boolean hasPublication = false;
      try {
        List<Subject> subjects = sDelegate.getSubjectsByPublisher (pub.getKey ());
        PublicationDelegate pcDelegate = new PublicationDelegate ();
        for (Subject subject : subjects)
        {
          if (pcDelegate.getPublications (pub.getKey (), subject.getKey ()).size () > 0)
          {
            hasPublication = true;
            break;
          }
        }
      } catch (ContentStandardException | DaoException e) {
      }
      table += "<td>" + pub.getName () + "</td>"
          + "<td>" + pub.getKey () + "</td>";
      if (hasPublication)
        table += "<td><button type=\"button\" name=\"edit\" id=\"edit\"><span class=\"btnIcon icon_sprite icon_edit2\">edit</span></button></td>"; 
      else
        table += "<td>"
            + "<button type=\"button\" name=\"edit\" id=\"edit\"><span class=\"btnIcon icon_sprite icon_edit2\">edit</span></button>"
            + "<button type=\"button\" name=\"delete\"  id=\"delete\"><span class=\"btnIcon icon_sprite icon_cancel2\">delete</span></button>"
            + "</td>";
      table += "</tr>";
    }
    table += "</tbody>"
        + "</table>";
    return table;
  }

  public String populateSubject ()
  {
    String table = "";
    SubjectDelegate sDelegate = new SubjectDelegate ();
    List<Subject> subjects = new ArrayList<Subject> ();

    try {
      subjects = sDelegate.getSubjects (null);
    } catch (ContentStandardException | DaoException e) {
    }

    HashSet<Subject> hs = new HashSet<Subject> ();
    hs.addAll (subjects);
    subjects.clear ();
    subjects.addAll (hs);
    Collections.sort (subjects, new SubjectComparator ());

    PublicationDelegate pDelegate = new PublicationDelegate ();
    PublisherDelegate pubDelegate = new PublisherDelegate ();

    table += "<table border='1' align='center'><thead><th>Subject Name</th><th>Subject Code</th><th></th></thead><tbody>";

    for (Subject sub : subjects)
    {
      boolean deleteButton = true;
      try {
        for (Publisher publisher : pubDelegate.getPublishers (null))
        {
          if (pDelegate.getPublications (publisher.getKey (), sub.getKey ()).size () > 0)
          {
            deleteButton = false;
            break;
          }
        }
      } catch (ContentStandardException | DaoException e1) {
      }

      table += "<tr>";
      table += "<td>" + sub.getName () + "</td>"
          + "<td>" + sub.getCode () + "</td>"
          + "<td>"
          + "<button type=\"button\" name=\"edit\" id=\"edit\"><span class=\"btnIcon icon_sprite icon_edit2\">edit</span></button>";
      if (deleteButton)
        table += "<button type=\"button\" name=\"delete\" id=\"delete\"><span class=\"btnIcon icon_sprite icon_cancel2\">delete</span></button>";
      table += "</td></tr>";
    }
    table += "</tbody></table>";
    return table;
  }

  public String populateGrade ()
  {
    String table = "";
    GradeDelegate gDelegate = new GradeDelegate ();
    List<GradeLevel> grades = new ArrayList<GradeLevel> ();
    try {
      grades = gDelegate.getGradeLevels (null, null);
    } catch (ContentStandardException | DaoException e) {
    }

    Collections.sort (grades, new GradeComparator ());

    table += "<table border='1' align='center'><thead><th>Grade Name</th><th>Description</th><th></th></thead><tbody>";

    for (GradeLevel grade : grades)
    {
      table += "<tr>";
      String str = " ";
      if (grade.getDescription () != null)
        str = grade.getDescription ();
      table += "<td>" + grade.getName () + "</td>"
          + "<td>" + str + "</td>"
          + "<td>"
          + "<button type=\"button\" name=\"edit\" id=\"edit\"><span class=\"btnIcon icon_sprite icon_edit2\">edit</span></button>"
          + "<button type=\"button\" name=\"delete\" id=\"delete\"><span class=\"btnIcon icon_sprite icon_cancel2\">delete</span></button>"
          + "</td>";
      table += "</tr>";
    }
    table += "</tbody></table>";

    return table;
  }
}

class PublisherComparator implements Comparator<Publisher>
{
  @Override
  public int compare (Publisher o1, Publisher o2) {
    return o1.getName ().compareTo (o2.getName ());
  }
}

class GradeComparator implements Comparator<GradeLevel>
{
  @Override
  public int compare (GradeLevel o1, GradeLevel o2) {
    return o1.getName ().compareTo (o2.getName ());
  }
}

class SubjectComparator implements Comparator<Subject>
{
  @Override
  public int compare (Subject o1, Subject o2) {
    return o1.getName ().compareTo (o2.getName ());
  }
}
