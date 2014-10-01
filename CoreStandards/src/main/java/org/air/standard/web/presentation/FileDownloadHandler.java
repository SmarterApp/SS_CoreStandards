/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.web.presentation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.air.standard.common.ReturnStatus;
import org.air.standard.dao.PublicationDAO;
import org.air.standard.exceptions.ContentStandardException;
import org.air.standard.exceptions.ContentStandardSecurityException;
import org.air.standard.exceptions.DaoException;
import org.slf4j.LoggerFactory;
import org.air.standard.openoffice.OpenOfficeExporter;
import org.air.standard.web.authentication.WebUserInformation;
import org.slf4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Servlet implementation class FileDownloadHandler
 */
public class FileDownloadHandler extends HttpServlet {
	
	
	Logger _logger = LoggerFactory.getLogger(FileDownloadHandler.class);

	private static final long serialVersionUID = 1L;

	// OpenOfficeInitializerCommon openOfficeInitializerCommon = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FileDownloadHandler() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	static final int BUF_SIZE = 4096;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PublicationDAO publicationDAO = new PublicationDAO();
		OpenOfficeExporter openOfficeExporter = new OpenOfficeExporter();
		String sessionKey = null;
		try {
			//TODO Shiva how to get the bean factory here so that we can remove (new WebUserInformation()) and call the bean instead?
			sessionKey = (new WebUserInformation()).getSessionKey();
		} catch (ContentStandardSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String ooExecPath = null;
		String ooSpreadsheetPath = null;
		FileInputStream fis = null;

		try {
		InitialContext context = new InitialContext();
      ooExecPath = request.getSession ().getServletContext ().getInitParameter ("ooExecPath");
      ooSpreadsheetPath = request.getSession ().getServletContext ().getInitParameter ("ooSpreadsheetPath");
	   //ooExecPath = container.getInitParameter("ooExecPath");
	  // ooSpreadsheetPath = container.getInitParameter("ooSpreadsheetPath");
			//ooExecPath = (String) context.lookup("java:comp/env/ooExecPath");
			//ooSpreadsheetPath = (String) context
				//	.lookup("java:comp/env/ooSpreadsheetPath");
			File downloadDirectory = new File(ooSpreadsheetPath);
			if (!downloadDirectory.exists()) {
				boolean status = downloadDirectory.mkdirs();
			}
			if (!downloadDirectory.exists())
				throw new ContentStandardException(
						"Problems accessing "
								+ ooSpreadsheetPath
								+ "server directory");
			/*if (!downloadDirectory.canRead() || !downloadDirectory.canWrite())
				
				throw new ContentStandardException ("Problem with read or write access to files in "
						+ ooSpreadsheetPath
						+ " server directory");
            */
		} catch (NamingException ex) {
			_logger.error("Error Naming Exception in doPost method");
				
			ex.printStackTrace();
			displayError (ex, response);
			return;
		} catch (SecurityException se) {
			// can be thrown by canWrite, canRead, exists, mkdirs calls
			// if server is not configured with correct access rights
			_logger.error(
					"Problem with access rights to "
							+ ooSpreadsheetPath
							+ " service directory. " + se.getMessage());
			
			displayError(se, response);
			return;
		}catch (ContentStandardException cse) {
			_logger.error(cse.getMessage());
					
			displayError(cse, response);
			return;
		}
		
		String docName = null;
		ReturnStatus<String> status = new ReturnStatus<String>("success");
		
		_logger.debug("About to call exportToFile");
				
		try {
			docName = openOfficeExporter.exportToFile(ooExecPath,
					ooSpreadsheetPath, sessionKey, status);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			_logger.error("Error SQL Exception in doPost method:" + e.getMessage());
					
			displayError (new Exception ("Database access problem" + e.getMessage()), response);
			e.printStackTrace();
			return;
		}
		_logger.debug("Finished exportToFile, output docName: "
				+ docName);
		
		if (docName != null) {
			String filePath = ooSpreadsheetPath + "/" + docName;
			File file = new File(filePath);
			ServletOutputStream outputStream = null;
			try {

				fis = new FileInputStream(file);
				ServletContext context = getServletConfig().getServletContext();
				String mimeType = context.getMimeType(filePath);
				_logger.debug("File " + docName + " has mime type "
						+ mimeType);

				if (mimeType == null) {
					mimeType = "application/octet-stream";
				}
				response.setContentType(mimeType);
				response.setHeader("Content-Disposition",
						"attachment;filename=" + docName + "");
				outputStream = response.getOutputStream();
				int numRead = 0;
				byte[] buf = new byte[BUF_SIZE];
				while ((numRead = fis.read(buf)) != -1) {
					outputStream.write(buf, 0, numRead);
				}

				_logger.debug("Sent reply with file " + docName
						+ "as attachment");
			} catch (FileNotFoundException e) {
				_logger.error("Failed to open file: " + filePath);
                displayError(new Exception("Error"), response);
                
			} catch (IOException e) {
				_logger.error("Failed to outstream data from file: " + filePath);
                displayError(new Exception("Error"), response);
                
			} finally {
				if (outputStream != null)
					outputStream.close();
				if (fis != null)
					fis.close();
				if (!file.delete()) {
					_logger.error("Failed to delete file: " + filePath);
				} else {
					_logger.debug("Sucessfully deleted file: " + filePath);
				}

			}
		} else {

             if (!status.getErrors().isEmpty())
            	 displayError (new Exception (status.getErrors().get(0)), response);
             else
			     displayError(new Exception("Error"), response);
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
