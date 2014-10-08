/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.delegate;

import java.sql.SQLException;
import java.util.List;

import org.air.standard.common.Subject;
import org.air.standard.dao.SubjectDAO;
import org.air.standard.exceptions.ContentStandardException;
import org.air.standard.exceptions.DaoException;

public class SubjectDelegate
{

  private SubjectDAO mSubjectDao = new SubjectDAO ();

  public void addSubject (String sessionKey, Subject newSubject)
      throws ContentStandardException, DaoException {
    // TODO Auto-generated method stub
    try {
      mSubjectDao.addSubject (sessionKey, newSubject);
    } catch (SQLException exp) {
      throw new ContentStandardException (exp);
    }
  }

  public List<Subject> getSubjects (String subjectKey)
      throws ContentStandardException, DaoException {
    try {
      return mSubjectDao.getSubjects (subjectKey);
    } catch (SQLException exp) {
      throw new ContentStandardException (exp);
    }
  }

  public List<Subject> getSubjectsByPublisher (String publisherKey)
      throws ContentStandardException, DaoException {
    try {
      return mSubjectDao.getSubjectsByPublisher (publisherKey);
    } catch (SQLException exp) {
      throw new ContentStandardException (exp);
    }
  }

  public void deleteSubject (Subject subject) {
    mSubjectDao.deleteSubject (subject);
  }

  public void editSubject (Subject oldSubject, Subject newSubject) {
    mSubjectDao.editSubject (oldSubject, newSubject);
  }

}
