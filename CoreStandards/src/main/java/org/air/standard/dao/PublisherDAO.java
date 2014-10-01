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
import org.air.standard.common.Publisher;
import org.air.standard.common.exception.MultipleDataException;
import org.air.standard.exceptions.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublisherDAO extends AbstractDAO
{

 private static final  Logger                      _logger              = LoggerFactory.getLogger (PublisherDAO.class);
  private static final String CMD_GET_PUBLISHER    = "{call GetPublisher(?) }";
  private static final String CMD_ADD_PUBLISHER    = "{call AddPublisher(?, ?, ?, ?) }";
  private static final String CMD_EDIT_PUBLISHER   = "{call editpublisher(?, ?, ?) }";
  private static final String CMD_DELETE_PUBLISHER = "{call deletepublisher(?, ?) }";

  public List<Publisher> getPublishers (String publisherKey)
      throws SQLException, DaoException {

    ArrayList<Publisher> publisherList = new ArrayList<Publisher> ();
    try (SQLConnection connection = getSQLConnection ()) {
      try (CallableStatement callstatement = connection
          .prepareCall (CMD_GET_PUBLISHER)) {
        callstatement.setString (1, publisherKey);

        if (callstatement.execute ()) {
          ResultSet rs = callstatement.getResultSet ();
          /*
           * Code to check is the resultset is for Error message, If yes throws
           * DAOException and cascades to calling methods
           */
          // DaoException.getInstanceIfAvailable(rs);

          DaoException.checkForError (rs);

          // No errors from resultset, so proceed the data retreival.
          publisherList = (ArrayList<Publisher>) getPublishers (rs);
        }
      }
    } catch (SQLException e) {
      String message = StringUtils.format ("Error while get publishers in getPublishers method {0}", e.getMessage ());
      printStackTraceToLogger (message, e);
      throw e;
    }
    return publisherList;
  }

  public Publisher getPublisher (String publisherKey)
      throws MultipleDataException, SQLException, DaoException {
    try {
      List<Publisher> publisherList = getPublishers (publisherKey);
      if (publisherList != null && publisherList.size () != 0) {
        if (publisherList.size () > 1)
          throw new MultipleDataException (1, publisherList.size ());

        return publisherList.get (0);
      }
    } catch (SQLException e) {
      String message = StringUtils.format ("Error while get publishers in getPublishers method {0}", e.getMessage ());
      printStackTraceToLogger (message, e);
      throw e;
    }
    return null;
  }

  public void addPublishers (String sessionKey, Publisher publisher)
      throws SQLException, DaoException {

    try (SQLConnection connection = getSQLConnection ()) {
      try (CallableStatement callstatement = connection
          .prepareCall (CMD_ADD_PUBLISHER)) {
        callstatement.setString (1, sessionKey);
        callstatement.setString (2, publisher.getName ());
        callstatement.setString (3, publisher.getKey ());
        callstatement.setString (4, publisher.getUrl ());
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
      String message = StringUtils.format ("Error while get publishers in getPublishers method {0}", e.getMessage ());
      printStackTraceToLogger (message, e);
      throw e;
    }
  }

  private List<Publisher> getPublishers (ResultSet resultset)
      throws SQLException {

    ArrayList<Publisher> list = new ArrayList<Publisher> ();
    try {
      while (resultset.next ()) {

        Publisher publisher = new Publisher ();

        publisher.setName (resultset.getString ("Name"));
        publisher.setUrl (resultset.getString ("URL"));
        publisher.setKey (resultset.getString ("_Key"));

        list.add (publisher);
      }

    } catch (SQLException e) {
      String message = StringUtils.format ("Error while get publishers in getPublishers method {0}", e.getMessage ());
      printStackTraceToLogger (message, e);
      throw e;
    }
    return list;
  }

  public void editPublishers (Publisher oldPublisher, Publisher newPublisher) {
    try (SQLConnection connection = getSQLConnection ()) {
      try (CallableStatement callstatement = connection
          .prepareCall (CMD_EDIT_PUBLISHER)) {
        callstatement.setString (1, oldPublisher.getName ());
        callstatement.setString (2, oldPublisher.getKey ());
        callstatement.setString (3, newPublisher.getName ());
        if (callstatement.execute ()) {
        }
      }
    } catch (SQLException e) {
    }
  }

  public void deletePublisher (Publisher publisher) {
    try (SQLConnection connection = getSQLConnection ()) {
      try (CallableStatement callstatement = connection
          .prepareCall (CMD_DELETE_PUBLISHER)) {
        callstatement.setString (1, publisher.getName ());
        callstatement.setString (2, publisher.getKey ());
        if (callstatement.execute ()) {
        }
      }
    } catch (SQLException e) {
    }
  }
}
