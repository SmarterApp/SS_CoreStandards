/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.dao;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.air.shared.db.AbstractDAO;
import org.air.shared.db.SQLConnection;
import org.air.shared.utils.StringUtils;
import org.air.standard.common.GradeLevel;
import org.air.standard.common.LoaderBenchmarkGrade;
import org.air.standard.exceptions.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GradeDAO extends AbstractDAO
{

 private static final  Logger               _logger                      = LoggerFactory.getLogger (GradeDAO.class);

  // GRADE
  private final String GET_GRADE_BY_PUBLICATION     = "{call GetGradeLevel(?, ?)}";
  private final String ADD_GRADE_BY_PUBLISHER       = "{call AddGrade(?, ?, ?)}";
  private final String CMD_MODIFY_GRADE             = "{call ModifyGrade(?, ?)}";
  private final String CMD_GET_GRADE_BY_SESSION_KEY = "{call Loader_GetBenchMarkGrades(?)}";
  private final String CMD_EDIT_GRADE               = "{call Loader_EditBenchmarkGrade(?,?,?)}";
  private final String CMD_edit_GRADE               = "{call editgrade(?,?,?)}";
  private final String CMD_DELETE_GRADE             = "{call Loader_DeleteBenchmarkGrade(?, ?, ?)}";
  private final String CMD_delete_GRADE             = "{call deletegrade(?,?)}";

  public List<GradeLevel> getGradeLevels (String gradekey,
      String publicationKey) throws SQLException, DaoException {
    List<GradeLevel> listofgradelevels = null;
    try (SQLConnection connection = getSQLConnection ()) {
      try (CallableStatement callstatement = connection
          .prepareCall (GET_GRADE_BY_PUBLICATION)) {
        callstatement.setString (1, gradekey);
        callstatement.setString (2, publicationKey);

        if (callstatement.execute ()) {
          ResultSet rs = callstatement.getResultSet ();
          /*
           * Code to check is the resultset is for Error message, If yes throws
           * DAOException and cascades to calling methods
           */
          DaoException.getInstanceIfAvailable (rs);

          // No errors from resultset, so proceed the data retreival.
          listofgradelevels = getGradeLevels (rs);
        }
      }
    } catch (SQLException e) {

      String message = StringUtils.format ("Error while geting Benchmark Grade levels in getGradeLevels method {0}", e.getMessage ());
      printStackTraceToLogger (message, e);

      throw e;
    }
    return listofgradelevels;
  }

  private List<GradeLevel> getGradeLevels (ResultSet resultset)
      throws SQLException {
    ArrayList<GradeLevel> list = new ArrayList<GradeLevel> ();
    try {
      while (resultset.next ()) {
        GradeLevel grades = new GradeLevel ();
        grades.setKey (resultset.getString ("_key"));
        grades.setName (resultset.getString ("name"));
        grades.setDescription (resultset.getString ("description"));

        list.add (grades);
      }
    } catch (SQLException e) {

      String message = StringUtils.format ("Error while geting Benchmark Grade result set in getGradeLevels method {0}", e.getMessage ());
      printStackTraceToLogger (message, e);

      throw e;
    }
    return list;
  }

  private List<LoaderBenchmarkGrade> getGrades (ResultSet resultset)
      throws SQLException {
    ArrayList<LoaderBenchmarkGrade> list = new ArrayList<LoaderBenchmarkGrade> ();
    try {
      while (resultset.next ()) {
        LoaderBenchmarkGrade grades = new LoaderBenchmarkGrade ();
        grades.setBenchmark (resultset.getString ("Benchmark"));
        grades.setGrade (resultset.getString ("Grade"));

        list.add (grades);
      }
    } catch (SQLException e) {

      String message = StringUtils.format ("Error while geting Benchmark Grade in getGrades method {0}", e.getMessage ());
      printStackTraceToLogger (message, e);

      throw e;
    }
    return list;
  }

  public List<GradeLevel> addGradeLevels (String gradekey, String gradename)
      throws SQLException, DaoException {
    List<GradeLevel> listofgradelevels = null;
    try (SQLConnection connection = getSQLConnection ()) {
      try (CallableStatement callstatement = connection
          .prepareCall (ADD_GRADE_BY_PUBLISHER)) {
        callstatement.setString (1, gradekey);
        callstatement.setString (2, gradename);
        if (callstatement.execute ()) {
          ResultSet rs = callstatement.getResultSet ();
          /*
           * Code to check is the resultset is for Error message, If yes throws
           * DAOException and cascades to calling methods
           */
          DaoException.getInstanceIfAvailable (rs);
        }
        // No errors from resultset, so proceed the data retreival.
        listofgradelevels = getGradeLevels (gradekey, gradename);
      }
    } catch (SQLException e) {

      String message = StringUtils.format ("Error while adding Benchmark Grade in addGradeLevels method {0}", e.getMessage ());
      printStackTraceToLogger (message, e);

      throw e;
    }
    return listofgradelevels;
  }

  public GradeLevel modifyGrade (String pubKey, String newGrade)
      throws SQLException {

    GradeLevel grade = new GradeLevel ();
    try (SQLConnection connection = getSQLConnection ()) {
      try (CallableStatement callstatement = connection
          .prepareCall (CMD_MODIFY_GRADE)) {
        callstatement.setString (1, pubKey);
        callstatement.setString (2, newGrade);

        if (callstatement.execute ()) {
          grade.setKey (pubKey);
          grade.setName (newGrade);
        }
      }
    } catch (SQLException e) {

      String message = StringUtils.format ("Error while modify Benchmark Grade in modifyGrade method {0}", e.getMessage ());
      printStackTraceToLogger (message, e);

      throw e;
    }
    return grade;
  }

  public List<LoaderBenchmarkGrade> getBenchmarkGradesBySessionKey (
      String sessionKey) throws SQLException, DaoException {
    List<LoaderBenchmarkGrade> listofgradelevels = null;
    try (SQLConnection connection = getSQLConnection ()) {
      try (CallableStatement callstatement = connection
          .prepareCall (CMD_GET_GRADE_BY_SESSION_KEY)) {
        callstatement.setString (1, sessionKey);

        if (callstatement.execute ()) {
          ResultSet rs = callstatement.getResultSet ();
          /*
           * Code to check is the resultset is for Error message, If yes throws
           * DAOException and cascades to calling methods
           */
          DaoException.getInstanceIfAvailable (rs);

          // No errors from resultset, so proceed the data retreival.
          listofgradelevels = getGrades (rs);
        }
      }
    } catch (SQLException e) {

      String message = StringUtils.format ("Error while getting Benchmark Grade by sessionkey in getBenchmarkGradesBySessionKey method {0}", e.getMessage ());
      printStackTraceToLogger (message, e);

      throw e;
    }
    return listofgradelevels;
  }

  public void addGradeLevels (String sessionKey, GradeLevel newGrade)
      throws SQLException, DaoException {

    try (SQLConnection connection = getSQLConnection ()) {
      try (CallableStatement callstatement = connection
          .prepareCall (ADD_GRADE_BY_PUBLISHER)) {
        callstatement.setString (1, sessionKey);
        callstatement.setString (2, newGrade.getName ());
        callstatement.setString (3, newGrade.getDescription ());
        if (callstatement.execute ()) {
          ResultSet rs = callstatement.getResultSet ();
          /*
           * Code to check is the resultset is for Error message, If yes throws
           * DAOException and cascades to calling methods
           */
          DaoException.getInstanceIfAvailable (rs);
        }
      }
    } catch (SQLException e) {

      String message = StringUtils.format ("Error while adding Benchmark Grade in addGradeLevels method {0}", e.getMessage ());
      printStackTraceToLogger (message, e);

      throw e;
    }

  }

  public List<LoaderBenchmarkGrade> editGrade (String sessionKey,
      LoaderBenchmarkGrade grade) throws SQLException, DaoException {

    List<LoaderBenchmarkGrade> listofgradelevels = null;
    try (SQLConnection connection = getSQLConnection ()) {
      try (CallableStatement callstatement = connection
          .prepareCall (CMD_EDIT_GRADE)) {
        callstatement.setString (1, sessionKey);
        callstatement.setString (2, grade.getBenchmark ());
        callstatement.setString (3, grade.getNewGrade ());
        if (callstatement.execute ()) {
          ResultSet rs = callstatement.getResultSet ();
          /*
           * Code to check is the resultset is for Error message, If yes throws
           * DAOException and cascades to calling methods
           */
          DaoException.getInstanceIfAvailable (rs);

          // No errors from resultset, so proceed the data retreival.
          listofgradelevels = getGrades (rs);
        }
      }
    } catch (SQLException e) {

      String message = StringUtils.format ("Error while editing Benchmark Grade in editGrade method {0}", e.getMessage ());
      printStackTraceToLogger (message, e);

      throw e;
    }
    return listofgradelevels;

  }

  public List<LoaderBenchmarkGrade> deleteGrade (String sessionKey,
      LoaderBenchmarkGrade grade) throws SQLException, DaoException {

    List<LoaderBenchmarkGrade> listofgradelevels = null;
    try (SQLConnection connection = getSQLConnection ()) {
      try (CallableStatement callstatement = connection
          .prepareCall (CMD_DELETE_GRADE)) {
        callstatement.setString (1, sessionKey);
        callstatement.setString (2, grade.getBenchmark ());
        callstatement.setString (3, grade.getGrade ());
        if (callstatement.execute ()) {
          ResultSet rs = callstatement.getResultSet ();
          /*
           * Code to check is the resultset is for Error message, If yes throws
           * DAOException and cascades to calling methods
           */
          DaoException.getInstanceIfAvailable (rs);
          // No errors from resultset, so proceed the data retreival.
        }
        listofgradelevels = getBenchmarkGradesBySessionKey (sessionKey);
      }
    } catch (SQLException e) {

      String message = StringUtils.format ("Error while deleting Benchmark Grade in deleteGrade method {0}", e.getMessage ());
      printStackTraceToLogger (message, e);

      throw e;
    }
    return listofgradelevels;

  }

  public void editGrade (GradeLevel oldGrade, GradeLevel newGrade) {
    try (SQLConnection connection = getSQLConnection ()) {
      try (CallableStatement callstatement = connection
          .prepareCall (CMD_edit_GRADE)) {
        callstatement.setString (1, oldGrade.getName ());
        callstatement.setString (2, oldGrade.getDescription ());
        callstatement.setString (3, newGrade.getName ());
        if (callstatement.execute ()) {
        }
      }
    } catch (SQLException e) {
    }
  }

  public void deleteGrade (GradeLevel gradeLevel) {
    try (SQLConnection connection = getSQLConnection ()) {
      try (CallableStatement callstatement = connection
          .prepareCall (CMD_delete_GRADE)) {
        callstatement.setString (1, gradeLevel.getName ());
        callstatement.setString (2, gradeLevel.getDescription ());
        if (callstatement.execute ()) {
        }
      }
    } catch (SQLException e) {
      String s = e.getMessage ();
    }
  }

  // Public String separateComma(String originalString){
  // String
  //
  // }

}
