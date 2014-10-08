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

import org.air.standard.common.Publisher;
import org.air.standard.common.exception.MultipleDataException;
import org.air.standard.dao.PublisherDAO;
import org.air.standard.exceptions.ContentStandardException;
import org.air.standard.exceptions.DaoException;

public class PublisherDelegate
{
  private PublisherDAO dao = new PublisherDAO ();

  public void addPublisher (String sessionKey, Publisher publisher)
      throws ContentStandardException, DaoException {
    try {
      dao.addPublishers (sessionKey, publisher);
    } catch (SQLException exp) {
      throw new ContentStandardException (exp);
    }
  }

  public Publisher getPublisher (String publisherKey)
      throws ContentStandardException, MultipleDataException,
      DaoException {
    try {
      return dao.getPublisher (publisherKey);
    } catch (SQLException exp) {
      throw new ContentStandardException (exp);
    }
  }

  public List<Publisher> getPublishers (String publisherKey)
      throws ContentStandardException, DaoException {
    try {
      return (List<Publisher>) dao.getPublishers (publisherKey);
    } catch (SQLException exp) {
      throw new ContentStandardException (exp);
    }
  }

  public void editPublisher (Publisher oldPublisher, Publisher newPublisher) {
    dao.editPublishers (oldPublisher, newPublisher);
  }

  public void deletePublisher (Publisher publisher) {
    dao.deletePublisher (publisher);
  }
}
