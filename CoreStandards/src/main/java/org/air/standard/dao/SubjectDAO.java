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
import org.air.standard.common.Subject;
import org.air.standard.exceptions.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubjectDAO extends AbstractDAO
{

 private static final  Logger               _logger                  = LoggerFactory.getLogger (SubjectDAO.class);

  private final String GET_SUBJECT_BY_PUBLISHER = "{call GetSubjectByPublisher(?)}";
  private final String ADD_SUBJECT_BY_PUBLISHER = "{call AddSubject(?,?,?)}";

  private final String CMD_ADD_SUBJECT          = "{call AddSubject(?,?,?)}";
  // SUBJECT
  private final String GET_SUBJECT_BY_NAME      = "{call GetSubject (?)}";
  private final String CMD_EDIT_SUBJECT         = "{call editsubject (?,?,?,?)}";
  private final String CMD_DELETE_SUBJECT       = "{call deletesubject (?,?)}";

  public List<Subject> getSubjectsByPublisher (String publisherKey)
      throws SQLException, DaoException {
    List<Subject> listofsubject = null;
    try (SQLConnection connection = getSQLConnection ()) {
      try (CallableStatement callstatement = connection
          .prepareCall (GET_SUBJECT_BY_PUBLISHER)) {
        callstatement.setString (1, publisherKey);

        if (callstatement.execute ()) {
          ResultSet rs = callstatement.getResultSet ();
          /*
           * Code to check is the resultset is for Error message, If yes throws
           * DAOException and cascades to calling methods
           */
          DaoException.getInstanceIfAvailable (rs);

          // No errors from resultset, so proceed the data retreival.
          listofsubject = getSubjects (rs);
        }
      }
    } catch (SQLException e) {

      String message = StringUtils.format ("Error while getting subjects in getSubjectsByPublisher method {0}", e.getMessage ());
      printStackTraceToLogger (message, e);

      throw e;
    }
    return listofsubject;
  }

  public List<Subject> getSubjects (String subjectName) throws SQLException,
      DaoException {
    List<Subject> listofsubject = null;
    try (SQLConnection connection = getSQLConnection ()) {
      try (CallableStatement callstatement = connection
          .prepareCall (GET_SUBJECT_BY_NAME)) {
        callstatement.setString (1, subjectName);

        if (callstatement.execute ()) {
          ResultSet rs = callstatement.getResultSet ();
          /*
           * Code to check is the resultset is for Error message, If yes throws
           * DAOException and cascades to calling methods
           */
          // DaoException.getInstanceIfAvailable(rs);
          DaoException.checkForError (rs);

          // No errors from resultset, so proceed the data retreival.
          listofsubject = getSubjects (rs);
        }
      }
    } catch (SQLException e) {

      String message = StringUtils.format ("Error while getting subjects in getSubjects method {0}", e.getMessage ());
      printStackTraceToLogger (message, e);

      throw e;
    }
    return listofsubject;
  }

  private List<Subject> getSubjects (ResultSet resultset) throws SQLException {
    ArrayList<Subject> list = new ArrayList<Subject> ();
    try {
      while (resultset.next ()) {
        Subject subject = new Subject ();
        subject.setKey (resultset.getString ("SubjectKey"));
        subject.setName (resultset.getString ("SubjectLabel"));
        subject.setCode (resultset.getString ("SubjectCode"));
        list.add (subject);
      }
    } catch (SQLException e) {
      String message = StringUtils.format ("Error while getting subjects in getSubjects method {0}", e.getMessage ());
      printStackTraceToLogger (message, e);

      throw e;
    }
    return list;
  }

  public void addSubject (String sessionKey, Subject newSubject)
      throws SQLException, DaoException {

    try (SQLConnection connection = getSQLConnection ()) {
      try (CallableStatement callstatement = connection
          .prepareCall (CMD_ADD_SUBJECT)) {
        callstatement.setString (1, sessionKey);
        callstatement.setString (2, newSubject.getName ());
        callstatement.setString (3, newSubject.getCode ());

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

      String message = StringUtils.format ("Error while adding subjects in addSubject method {0}", e.getMessage ());
      printStackTraceToLogger (message, e);

      throw e;
    }

  }

  public void deleteSubject (Subject subject) {

    try (SQLConnection connection = getSQLConnection ()) {
      try (CallableStatement callstatement = connection
          .prepareCall (CMD_DELETE_SUBJECT)) {
        callstatement.setString (1, subject.getName ());
        callstatement.setString (2, subject.getCode ());

        if (callstatement.execute ()) {

        }
      }
    } catch (SQLException e) {
      String s = e.getMessage ();
    }
  }

  public void editSubject (Subject oldSubject, Subject newSubject) {
    try (SQLConnection connection = getSQLConnection ()) {
      try (CallableStatement callstatement = connection
          .prepareCall (CMD_EDIT_SUBJECT)) {
        callstatement.setString (1, oldSubject.getName ());
        callstatement.setString (2, oldSubject.getCode ());
        callstatement.setString (3, newSubject.getName ());
        callstatement.setString (4, newSubject.getCode ());

        if (callstatement.execute ()) {

        }
      }
    } catch (SQLException e) {
      String s = e.getMessage ();
    }
  }

}
