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

import org.air.standard.common.Publisher;
import org.air.standard.common.ReturnStatus;
import org.air.standard.common.exception.MultipleDataException;
import org.air.standard.delegate.PublisherDelegate;
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

/**
 * Handles requests for the Publications.
 */
@Scope ("prototype")
@Controller
public class PublisherController extends AbstractController
{

 private static final  Logger            _logger           = LoggerFactory
                                          .getLogger (PublisherController.class);

  PublisherDelegate publisherDelegate = new PublisherDelegate ();

  @RequestMapping (method = RequestMethod.GET, value = "publisher")
  public @ResponseBody
  ReturnStatus<List<Publisher>> getAllPublishers ()
      throws ContentStandardSecurityException, ContentStandardException,
      DaoException {
    _logger.info ("start get all Publishers");
    String publisherkey = null;
    List<Publisher> listOfPublishers = publisherDelegate
        .getPublishers (publisherkey);
    ReturnStatus<List<Publisher>> status = new ReturnStatus<List<Publisher>> (
        "success");
    status.setPayload (listOfPublishers);
    _logger.info ("end get all Publishers");

    return status;

  }

  @RequestMapping (method = RequestMethod.GET, value = "publisher/{publisherKey}")
  public @ResponseBody
  ReturnStatus<Publisher> getPublisher (@PathVariable String publisherKey)
      throws MultipleDataException, ContentStandardException, DaoException {
    ReturnStatus<Publisher> status = new ReturnStatus<Publisher> ("success");
    if (publisherKey.equalsIgnoreCase ("")
        || publisherKey.equalsIgnoreCase (null)) {
      throw new ContentStandardException ("Invalid Publisher Key");
    }

    status.setPayload (publisherDelegate.getPublisher (publisherKey));
    return status;
  }

  @RequestMapping (method = RequestMethod.POST, value = "publisher")
  public @ResponseBody
  ReturnStatus<Publisher> addPublisher (@RequestBody Publisher publisher)
      throws ContentStandardSecurityException, ContentStandardException,
      DaoException {
    ReturnStatus<Publisher> status = new ReturnStatus<Publisher> ("success");
    String sessionKey = getUserInformation ().getSessionKey ();
    publisherDelegate.addPublisher (sessionKey, publisher);
    status.setPayload (publisher);
    return status;
  }

  @RequestMapping (method = RequestMethod.PUT, value = "publisher")
  public @ResponseBody
  ReturnStatus<Publisher> editPublisher (@RequestBody Publisher[] publisher)
      throws ContentStandardSecurityException, ContentStandardException,
      DaoException {
    ReturnStatus<Publisher> status = new ReturnStatus<Publisher> ("success");
    publisherDelegate.editPublisher (publisher[0], publisher[1]);
    status.setStatus ("success");
    return status;
  }

  @RequestMapping (method = RequestMethod.DELETE, value = "publisher")
  public @ResponseBody
  ReturnStatus<Publisher> deletePublisher (@RequestBody Publisher publisher)
      throws ContentStandardSecurityException, ContentStandardException,
      DaoException {
    ReturnStatus<Publisher> status = new ReturnStatus<Publisher> ("success");
    publisherDelegate.deletePublisher (publisher);
    status.setPayload (publisher);
    status.setStatus ("success");
    return status;
  }

  /*
   * Warning!!! This is a sample method to show how to get session key
   * information.
   */
  @RequestMapping (method = RequestMethod.GET, value = "Publisher/Post/Secure")
  public @ResponseBody
  Publisher testPublisher () throws ContentStandardSecurityException,
      ContentStandardSecurityException, ContentStandardException, DaoException {

    String sessionKey = getUserInformation ().getSessionKey ();
    Publisher publisher = new Publisher ();
    publisher.setKey ("test");
    publisher.setName ("test");
    publisher.setUrl ("test");
    return publisher;
  }

}
