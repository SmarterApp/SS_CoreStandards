/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.web.presentation;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.air.standard.common.ReturnStatus;
import org.air.standard.common.exception.InvalidArgumentException;
import org.air.standard.dao.LoaderTablesDAO;
import org.air.standard.dao.PublicationDAO;
import org.air.standard.exceptions.ContentStandardException;
import org.slf4j.LoggerFactory;
import org.air.standard.openoffice.OpenOfficeImporter;
import org.air.standard.web.authentication.WebUserInformation;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Servlet implementation class FileUploadHandler
 */
public class FileUploadHandler extends HttpServlet {
	
	Logger _logger = LoggerFactory.getLogger(FileUploadHandler.class);

	private String mType = "full";
	private static final long serialVersionUID = 1L;

	// OpenOfficeInitializerCommon openOfficeInitializerCommon = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FileUploadHandler() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		_logger.debug("FileUploadhandler fired!");
			
		String initParameterFileType = getServletConfig().getInitParameter(
				"fileType");

		String ooExecPath = null;
		String ooSpreadsheetPath = null;
		File uploadedFile = null;

		try {
			InitialContext context = new InitialContext();
			
			
			ooExecPath = request.getSession ().getServletContext ().getInitParameter ("ooExecPath");
			ooSpreadsheetPath = request.getSession ().getServletContext ().getInitParameter ("ooSpreadsheetPath");
//			 ooExecPath = container.getInitParameter("ooExecPath");
//			 ooSpreadsheetPath = container.getInitParameter("ooSpreadsheetPath");
//			ooExecPath = (String) context.lookup("java:comp/env/ooExecPath");
//			ooSpreadsheetPath = (String) context
//					.lookup("java:comp/env/ooSpreadsheetPath");

			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			// LoggerFactory.LogInfo("TEST File upload MANAGER", getClass());
			if (isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);

				try {
					// Parse the request
					_logger.debug("Before parseRequest");
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					BufferedInputStream buf = null;
					ServletOutputStream stream = null;
					while (iterator.hasNext()) {
						FileItem item = (FileItem) iterator.next();
						if (!item.isFormField()) {
							String fileName = item.getName();
							if (fileName.isEmpty())
								throw new InvalidArgumentException(
										"File to Upload ", "not empty");
							_logger.debug("Parsed file file "
									+ fileName);
							File uploadDirectory = new File(ooSpreadsheetPath);
							if (!uploadDirectory.exists()) {
								boolean status = uploadDirectory.mkdirs();
							}
							if (!uploadDirectory.exists())
								throw new ContentStandardException(
										"Problems accessing "
												+ ooSpreadsheetPath
												+ "server directory");
							// just so that we do not have a name clash
							// we will prefix the current time stamp to the file
							// name.
							Date date = new Date();

							String filePath = ooSpreadsheetPath + "/"
									+ date.getTime() + fileName;
							uploadedFile = new File(filePath);
							/*if (!uploadedFile.canWrite()
									|| !uploadedFile.canRead())
								throw new ContentStandardException(
										"Problem with read or write access to files in "
												+ ooSpreadsheetPath
												+ " server directory");
												*/
							/*
							 * System.out.println(uploadedFile.getAbsolutePath())
							 * ;
							 */
							item.write(uploadedFile);
							_logger.debug("Wrote temp file "
									+ uploadedFile.getAbsolutePath());
									
						}
					}
				} catch (SecurityException se) {
					// can be thrown by canWrite, canRead, exists, mkdirs calls
					// if server is not configured with correct access rights
					_logger.error(
							"Problem with access rights to "
									+ ooSpreadsheetPath
									+ " service directory. " + se.getMessage());
					
					displayError(se, response);
					return;
				} catch (ContentStandardException cse) {
					_logger.error(cse.getMessage());
					displayError(cse, response);
					return;
				} catch (InvalidArgumentException iae) {
					// EF: do not use displayError because it sets
					// SC_INTERNAL_SERVLET_ERROR and this is not the case!
					_logger.error(
							"FileUploadException: " + iae.getMessage());
					ReturnStatus<Exception> status = new ReturnStatus<Exception>(
							"failed");
					status.setPayload(null);
					status.appendToErrors(iae.getMessage());

					ObjectMapper mapper = new ObjectMapper();
					String statusJson = mapper.writeValueAsString(status);
					// serialize the status object.
					response.setContentType("application/json");
					response.getOutputStream().print(statusJson);
					return;

				} catch (FileUploadException e) {
					e.printStackTrace();
					_logger.error(
							"FileUpload Exception: " + e.getMessage());
					displayError(e, response);
					return;
				} catch (Exception e) {
					_logger.error(
							"Problem parsing request/creating temp file : "
									+ e.getMessage());
					e.printStackTrace();
					displayError(e, response);
					return;
				}

			}
			/* System.out.println("CONFIG FILE:::" + ooExecPath); */
		} catch (NamingException ex) {
			/* System.out.println("exception in jndi lookup"); */
			_logger.error(
					"NamingException in jndi lookup" + ex.getMessage());
			displayError(ex, response);
			return;
		}

		ObjectMapper mapper = new ObjectMapper();
		String statusJson = null;
		try {
			ReturnStatus<String> status = new ReturnStatus<String>("success");
			//TODO Shiva how to get the bean factory here so that we can remove (new WebUserInformation()) and call the bean instead?
			String sessionKey = (new WebUserInformation()).getSessionKey();
			// clear out existing staging tables for this session key.
			LoaderTablesDAO dao = new LoaderTablesDAO();
			dao.loaderClear(sessionKey);

			// tell DB what kind of upload we are doing - full or partial.
			PublicationDAO publicationDAO = new PublicationDAO();
			if (initParameterFileType.equalsIgnoreCase("full")) {
				publicationDAO.importPublication(sessionKey);

			} else if (initParameterFileType.equalsIgnoreCase("partial")) {
				publicationDAO.partialImportPublication(sessionKey);
			}

			_logger.debug("About to call importFromFile for "
					+ uploadedFile.getAbsolutePath());
			OpenOfficeImporter openOfficeImporter = new OpenOfficeImporter();

			openOfficeImporter.importFromFile(ooExecPath,
					uploadedFile.getAbsolutePath(), sessionKey, status);
			_logger.debug(
					"Finished importFromFile  for"
							+ uploadedFile.getAbsolutePath() + " status is "
							+ status.getStatus());
			statusJson = mapper.writeValueAsString(status);
			// serialize the status object.
			response.setContentType("application/json");
			response.getOutputStream().print(statusJson);
			_logger.debug("Sent json response ");

		} catch (Exception exp) {
			_logger.error(
					"Exception after temp file created " + exp.getMessage());
			exp.printStackTrace();
			displayError(exp, response);
		}
	}

	private void displayError(Exception exp, HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException

	{
		ObjectMapper mapper = new ObjectMapper();
		ReturnStatus<Exception> status = new ReturnStatus<Exception>("failed");
		if(exp.getMessage() == null) {
			status.setPayload (new Exception ("Error"));
		}
		else
		    status.setPayload(exp);
		String statusJson = mapper.writeValueAsString(status);

		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		response.setContentType("application/json");
		response.getOutputStream().print(statusJson);
	}
}
