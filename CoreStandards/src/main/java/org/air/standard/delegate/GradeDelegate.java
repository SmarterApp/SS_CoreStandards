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

import org.air.standard.common.GradeLevel;
import org.air.standard.common.LoaderBenchmarkGrade;
import org.air.standard.dao.GradeDAO;
import org.air.standard.exceptions.ContentStandardException;
import org.air.standard.exceptions.DaoException;

public class GradeDelegate
{
  GradeDAO gradeDAO = new GradeDAO ();

  public List<GradeLevel> getGradeLevels (String gradekey,
      String publicationKey) throws ContentStandardException,
      DaoException {
    try {
      return gradeDAO.getGradeLevels (gradekey, publicationKey);
    } catch (SQLException exp) {
      throw new ContentStandardException (exp);
    }
  }

  public List<GradeLevel> addGradeLevels (String gradekey, String gradename)
      throws ContentStandardException, DaoException {
    try {
      return gradeDAO.addGradeLevels (gradekey, gradename);
    } catch (SQLException exp) {
      throw new ContentStandardException (exp);
    }
  }

  public GradeLevel modifyGrade (String pubKey, String newGrade)
      throws ContentStandardException {
    try {
      return gradeDAO.modifyGrade (pubKey, newGrade);
    } catch (SQLException exp) {
      throw new ContentStandardException (exp);
    }
  }

  public List<LoaderBenchmarkGrade> getBenchmarkGradesBySessionKey (
      String sessionKey) throws ContentStandardException, DaoException {
    try {
      return gradeDAO.getBenchmarkGradesBySessionKey (sessionKey);
    } catch (SQLException exp) {
      throw new ContentStandardException (exp);
    }
  }

  public void addGrade (String sessionKey, GradeLevel newGrade)
      throws ContentStandardException, DaoException {
    try {
      gradeDAO.addGradeLevels (sessionKey, newGrade);
    } catch (SQLException exp) {
      throw new ContentStandardException (exp);
    }

  }

  public List<LoaderBenchmarkGrade> editGrade (String sessionKey,
      LoaderBenchmarkGrade grade) throws ContentStandardException,
      DaoException {
    try {
      return gradeDAO.editGrade (sessionKey, grade);
    } catch (SQLException exp) {
      throw new ContentStandardException (exp);
    }
  }

  public List<LoaderBenchmarkGrade> deleteGrade (String sessionKey,
      LoaderBenchmarkGrade grade) throws ContentStandardException,
      DaoException {
    try {
      return gradeDAO.deleteGrade (sessionKey, grade);
    } catch (SQLException exp) {
      throw new ContentStandardException (exp);
    }
  }

  public void editGrade (GradeLevel oldGrade, GradeLevel newGrade) {
    gradeDAO.editGrade (oldGrade, newGrade);
  }

  public void deleteGrade (GradeLevel gradeLevel) {
    gradeDAO.deleteGrade (gradeLevel);
  }

}
