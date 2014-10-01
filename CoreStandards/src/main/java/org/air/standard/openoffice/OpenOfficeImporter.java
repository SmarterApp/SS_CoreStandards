/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.openoffice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ooo.connector.BootstrapSocketConnector;
import ooo.connector.server.OOoServer;

import org.air.standard.common.LoaderBenchmarkGrade;
import org.air.standard.common.LoaderCategory;
import org.air.standard.common.LoaderPublication;
import org.air.standard.common.LoaderSock;
import org.air.standard.common.LoaderStandard;
import org.air.standard.common.ReturnStatus;
import org.air.standard.common.exception.InvalidArgumentException;
import org.air.standard.dao.LoaderTablesDAO;
import org.air.standard.dao.PublicationDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.star.beans.PropertyValue;
import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.sheet.XCellRangesQuery;
import com.sun.star.sheet.XSheetCellRanges;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.text.XText;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

/*
 import com.sun.star.comp.helper.Bootstrap;
 import com.sun.star.beans.XPropertySet;
 import com.sun.star.beans.PropertyValue;
 import com.sun.star.beans.XPropertySet;
 import com.sun.star.uno.XComponentContext;
 import com.sun.star.container.XEnumeration;
 import com.sun.star.container.XEnumerationAccess;
 import com.sun.star.frame.XController;
 import com.sun.star.frame.XModel;
 import com.sun.star.sheet.XCellAddressable;
 import com.sun.star.sheet.XSpreadsheetView;
 import com.sun.star.table.XColumnRowRange;
 import com.sun.star.table.XTableRows;
 import com.sun.star.table.XTableColumns;
 import com.sun.star.text.XTextTable;
 import com.sun.star.table.XCell;
 */
public class OpenOfficeImporter {
	
	Logger _logger = LoggerFactory.getLogger(OpenOfficeImporter.class);
	/*
	 * private static final String OOO_EXEC_FOLDER =
	 * "C:/Program Files (x86)/OpenOffice.org 3/program";
	 */

	static final String[] requiredSheets = { "publication", "categories",
			"standards", "benchmarkgrades", "socks" };

	static final String[] publicationCols = { "PublisherName",
			"PublicationDescription", "Subject", "Version" };
	static final String[] categoriesCols = { "Category", "TreeLevel" };
	static final String[] standardsCols = { "Level", "Key", "Name",
			"Description", "ShortName" };
	static final String[] benchmarkGradesCols = { "Benchmarkkey", "Grade" };
	static final String[] socksCols = { "KnowledgeCategory", "Description" };
	static final int MAX_OO_ATTEMPTS = 5;
	static final int DELAY_MILLISEC = 3000;

	private XComponentContext _xRemoteContext = null;
	private XMultiComponentFactory _xRemoteServiceManager = null;
	private XSpreadsheetDocument _xSpreadsheetDocument = null;
	private XSpreadsheets _xSpreadsheets = null;
	private XComponent _xSpreadsheetComponent = null;
	private String _theUrl = null;
	private String _oooExecFolder = null;
	private BootstrapSocketConnector _bootstrapSocketConnector = null;
	private String _sessionKey = null;

	// my DAO.
	private LoaderTablesDAO _mDao = new LoaderTablesDAO();
	private PublicationDAO _mPubDao = new PublicationDAO();

	public OpenOfficeImporter() {
		super();
	}

	/**
	 * @param retSt
	 *            Method may add errors to list of errors in ReturnStatus retSt
	 * @param sUrl
	 *            name of the created OpenOffice otuput file
	 */
	private void connect(ReturnStatus<String> retSt, String sUrl) {
		// get the remote office component context
		List oooOptions = OOoServer.getDefaultOOoOptions();
		oooOptions.add("-nofirststartwizard");
		OOoServer oooServer = new OOoServer(_oooExecFolder, oooOptions);
		this._bootstrapSocketConnector = new BootstrapSocketConnector(oooServer);
		for (int i = 0; i < MAX_OO_ATTEMPTS; i++) {
			try {

				this._xRemoteContext = _bootstrapSocketConnector.connect();
				this._xRemoteServiceManager = _xRemoteContext
						.getServiceManager();
				Object desktop = _xRemoteServiceManager
						.createInstanceWithContext(
								"com.sun.star.frame.Desktop", _xRemoteContext);
				XComponentLoader xComponentLoader = (XComponentLoader) UnoRuntime
						.queryInterface(XComponentLoader.class, desktop);

				java.io.File sourceFile = new java.io.File(sUrl);

				StringBuffer sbTmp = new StringBuffer("file:///");
				sbTmp.append(sourceFile.getCanonicalPath().replace('\\', '/'));
				String filePath = sbTmp.toString();

				/* System.out.println("sUrl: " + filePath); */
				PropertyValue[] loadProps = new PropertyValue[1];
				loadProps[0] = new com.sun.star.beans.PropertyValue();
				loadProps[0].Name = "Hidden";
				loadProps[0].Value = new Boolean(true);

				this._xSpreadsheetComponent = xComponentLoader
						.loadComponentFromURL(filePath, "_blank", 0, loadProps);
				this._xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
						.queryInterface(XSpreadsheetDocument.class,
								_xSpreadsheetComponent);
				if (this._xSpreadsheetDocument == null)
					throw new InvalidArgumentException("file",
							"an OpenOffice file");
				this._xSpreadsheets = _xSpreadsheetDocument.getSheets();

				_logger.debug("Initialized OpenOffice connection"
						);
				return;

			} catch (RuntimeException re) {
				/* probably OO is not installed properly */
				_logger.error(
						"Check if OpenOffice installed properly on server box! "
								+ re.getMessage());
				retSt.appendToErrors("Problems with OpenOffice install on server box");
				retSt.setStatus("failed");
				return;
			} catch (BootstrapException be) {

				/*
				 * get here if _bootstrapSocketConnector.connect() failed, no
				 * need to call disconnect()
				 */
				_logger.error("Could not bootstrap default Office"
						+ be.getMessage());
				if (be.getMessage() == null)
					retSt.appendToErrors("Problem accessing Open Office software.");
				else
					retSt.appendToErrors("Problem accessing Open Office software."
							+ be.getMessage());
				retSt.setStatus("failed");
				return;
			} catch (InvalidArgumentException invEx) {
				_logger.error(invEx.getMessage());
						
				retSt.appendToErrors(invEx.getMessage());
				retSt.setStatus("failed");
				_bootstrapSocketConnector.disconnect();
				return;
			} catch (Exception e) {

				/* got here if xComponentLoader.loadComponentFromUrl failed */
				/*
				 * String msg = "Attempt number " + i +
				 * " ERROR: failed initializing OO doc. " + e.getMessage();
				 * System.out.println(msg);
				 */

				if (i == MAX_OO_ATTEMPTS - 1) {
					_logger.error(
							"failed initializing OO doc." + e.getMessage());
						
					retSt.appendToErrors("ERROR:  failed initializing OO doc."
							+ e.getMessage());
					retSt.setStatus("failed");
					/*
					 * bootstrap connection was established at this point, let's
					 * disconnect it before returning
					 */
					_bootstrapSocketConnector.disconnect();
					return;
				} else {
					_bootstrapSocketConnector.disconnect();
					_logger.debug("loadComponent,Hitting delay");
							
					/* System.out.println("loadComponent,Hitting delay"); */
					delay();
				}
			}
		}

	}

	/**
	 * @param sUrl
	 *            Location of the file containing spreadsheet doc
	 * @throws RuntimeException
	 * @throws Exception
	 */
	private void initDoc(String sUrl) throws RuntimeException, Exception {

		Object desktop = _xRemoteServiceManager.createInstanceWithContext(
				"com.sun.star.frame.Desktop", _xRemoteContext);
		XComponentLoader xComponentLoader = (XComponentLoader) UnoRuntime
				.queryInterface(XComponentLoader.class, desktop);

		java.io.File sourceFile = new java.io.File(sUrl);

		StringBuffer sbTmp = new StringBuffer("file:///");
		sbTmp.append(sourceFile.getCanonicalPath().replace('\\', '/'));
		sUrl = sbTmp.toString();

		PropertyValue[] loadProps = new PropertyValue[1];
		loadProps[0] = new com.sun.star.beans.PropertyValue();
		loadProps[0].Name = "Hidden";
		loadProps[0].Value = new Boolean(true);

		this._xSpreadsheetComponent = xComponentLoader.loadComponentFromURL(
				sUrl, "_blank", 0, loadProps);

		this._xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
				.queryInterface(XSpreadsheetDocument.class,
						_xSpreadsheetComponent);

		this._xSpreadsheets = _xSpreadsheetDocument.getSheets();
	}

	/**
	 * @param oooExecFolder
	 *            Path to local folder containing OpenOffice executable file
	 *            soffice
	 * @param fileName
	 *            Location of the file containing spreadsheet doc
	 * @param sessionKey
	 *            SessionKey
	 * @param retSt
	 *            return status class; method may add entries to the list of
	 *            errors; each entry corresponds to one error
	 */

	public void importFromFile(String oooExecFolder, String fileName,
			String sessionKey, ReturnStatus<String> retSt) {

		_logger.debug("importFromFile called for file " + fileName
				);
		List<String> errorsLocal = new ArrayList<String>();

		this._theUrl = fileName;
		this._oooExecFolder = oooExecFolder;
		this._sessionKey = sessionKey;

		connect(retSt, fileName);
		if (retSt.getStatus().equalsIgnoreCase("failed"))
			return;
		/*
		 * try { initDoc(fileName); } catch (Exception e) {
		 * 
		 * retSt.appendToErrors("Failed to load OpenOffice file from " +
		 * fileName); retSt.setStatus("failed"); return; }
		 */
		HashSet<String> processedSheets = new HashSet<String>();

		String[] tabNames = _xSpreadsheets.getElementNames();
		for (String tabName : tabNames) {
			String processedName = tabName.toLowerCase().replace(" ", "")
					.replace("_", "");
			try {
				int maxRows = 0;
				com.sun.star.sheet.XSpreadsheet xSpreadsheet = (com.sun.star.sheet.XSpreadsheet) UnoRuntime
						.queryInterface(com.sun.star.sheet.XSpreadsheet.class,
								_xSpreadsheets.getByName(tabName));
				/*
				 * we use empty array of errors for validating each sheet
				 * because we do not want to bail out from processing all sheets
				 * is cumulative errors array already has some recs
				 */
				errorsLocal.clear();

				switch (processedName) {
				case "publication":
					maxRows = validateSheet(xSpreadsheet, processedName,
							publicationCols, errorsLocal);
					if (errorsLocal.isEmpty())
						doPublicationTab(xSpreadsheet, maxRows, retSt);
					break;
				case "categories":
					maxRows = validateSheet(xSpreadsheet, processedName,
							categoriesCols, errorsLocal);
					if (errorsLocal.isEmpty())
						doCategoriesTab(xSpreadsheet, maxRows, retSt);
					break;
				case "standards":
					maxRows = validateSheet(xSpreadsheet, processedName,
							standardsCols, errorsLocal);
					if (errorsLocal.isEmpty())
						doStandardsTab(xSpreadsheet, maxRows, retSt);
					break;
				case "socks":
					maxRows = validateSheet(xSpreadsheet, processedName,
							socksCols, errorsLocal);
					if (errorsLocal.isEmpty())
						doSocksTab(xSpreadsheet, maxRows, retSt);
					break;
				case "benchmarkgrades":
					maxRows = validateSheet(xSpreadsheet, processedName,
							benchmarkGradesCols, errorsLocal);
					if (errorsLocal.isEmpty())
						doBenchmarkTab(xSpreadsheet, maxRows, retSt);
					break;
				}
				if (!errorsLocal.isEmpty()) {
					retSt.appendToErrors(errorsLocal);
				}
			} catch (Exception exp) {
				String msg = "Problems reading sheet " + processedName + " "
						+ exp.getMessage();
				/* System.err.println(msg); */
				_logger.error(msg);
				retSt.appendToErrors(msg);
			}
			processedSheets.add(processedName);
		}
		missingSheets(requiredSheets, processedSheets, retSt);

		if (retSt.getErrors().size() == 0)
			retSt.setStatus("success");
		else
			retSt.setStatus("failed");

		closeObjects();
		closeOpenOffice();
		/*
		 * LoggerFactory.LogDebug( "Finished importFromFile with status: " +
		 * retSt.getStatus(), OpenOfficeImporter.class);
		 */

	}

	/**
	 * @param xSpreadsheet
	 *            Spreadsheet containing Publication Tab data
	 * @param maxRows
	 *            max number of rows on this sheet
	 * @param retSt
	 *            return status class; method may add entries to the list of
	 *            errors; each entry corresponds to one error
	 */
	private void doPublicationTab(XSpreadsheet xSpreadsheet, int maxRows,
			ReturnStatus<String> retSt) {
		List<String> errorsLocal = new ArrayList<String>();
		XText xText = null;
		/*
		 * LoggerFactory.LogDebug(
		 * "Starting processing of publication tab, number of rows: " + maxRows,
		 * OpenOfficeImporter.class);
		 */
		if (maxRows == 0)
			return; /*
					 * no data rows on a tab, should not happen here, already
					 * checked in validateSheet
					 */
		try {

			for (int i = 1; i <= maxRows; i++) {

				errorsLocal.clear();

				xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
						com.sun.star.text.XText.class,
						xSpreadsheet.getCellByPosition(0, i));
				String tempPublisherName = xText.getString();

				xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
						com.sun.star.text.XText.class,
						xSpreadsheet.getCellByPosition(1, i));
				String tempPublicationDescription = xText.getString();

				xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
						com.sun.star.text.XText.class,
						xSpreadsheet.getCellByPosition(2, i));
				String tempSubject = xText.getString();

				xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
						com.sun.star.text.XText.class,
						xSpreadsheet.getCellByPosition(3, i));
				String tempVersion = xText.getString();

				if (tempPublisherName.isEmpty()
						&& tempPublicationDescription.isEmpty()
						&& tempSubject.isEmpty() && tempVersion.isEmpty()) {
					_logger.debug("Skipping empty pub rec");
							
					continue;
				}

				if (tempPublisherName.isEmpty()) {
					String ms = "Publication:   " + " PublisherName in cell "
							+ getCellAddressString(0, i) + " must not be empty";

					errorsLocal.add(ms);
				}
				if (tempPublicationDescription.isEmpty()) {
					String ms = "Publication:   "
							+ " PublicationDescription in cell "
							+ getCellAddressString(1, i) + " must not be empty";

					errorsLocal.add(ms);
				}
				if (tempSubject.isEmpty()) {
					String ms = "Publication:   " + " Subject in cell "
							+ getCellAddressString(2, i) + " must not be empty";

					errorsLocal.add(ms);
				}
				int tempVersionInt = 0;
				if (tempVersion.isEmpty()) {
					String ms = "Publication:   " + " Version in cell "
							+ getCellAddressString(3, i) + " must not be empty";

					errorsLocal.add(ms);
				} else if (!isNumeric(tempVersion)) {

					String ms = "Publication:   " + "Version " + tempVersion
							+ " in cell " + getCellAddressString(3, i)
							+ " is not numeric";

					errorsLocal.add(ms);
				} else
					tempVersionInt = Integer.parseInt(tempVersion);

				if (errorsLocal.isEmpty()) {

					LoaderPublication pub = new LoaderPublication();
					pub.setSessionKey(_sessionKey);
					pub.setPublisherName(tempPublisherName);
					pub.setPublicationDescription(tempPublicationDescription);
					pub.setSubject(tempSubject);
					pub.setVersion(tempVersionInt);
					_logger.debug(
							"About to insert Publication row to database. ronwnum is "
									+ i);
					_mDao.addLoaderPublication(pub);
				} else {
					_logger.debug(
							"validation error for pub tab, row number " + i);
												retSt.appendToErrors(errorsLocal);
				}
			}
		} catch (Exception exp) {
			String ms = "Failed processing publication tab: "
					+ exp.getMessage();
			_logger.debug(
					"Exception while processing row from publication tab, row number "
							+ exp.getMessage());
			retSt.appendToErrors(ms);
		}
	}

	/**
	 * @param xSpreadsheet
	 *            Spreadsheet containing Categories Tab data
	 * @param maxRows
	 *            max number of rows on this sheet
	 * @param retSt
	 *            return status class; method may add entries to the list of
	 *            errors; each entry corresponds to one error
	 */
	private void doCategoriesTab(XSpreadsheet xSpreadsheet, int maxRows,
			ReturnStatus<String> retSt) {

		List<String> errorsLocal = new ArrayList<String>();
		XText xText = null;
		/*
		 * LoggerFactory.LogDebug(
		 * "Starting processing of categories tab, number of rows: " + maxRows,
		 * OpenOfficeImporter.class);
		 */
		if (maxRows == 0)
			return; /* no data rows on a tab */
		try {
			int currentLevel = 1;
			errorsLocal.clear();
			for (int i = 1; i <= maxRows; i++) {

				xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
						com.sun.star.text.XText.class,
						xSpreadsheet.getCellByPosition(0, i));
				String tempCategory = xText.getString();

				xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
						com.sun.star.text.XText.class,
						xSpreadsheet.getCellByPosition(1, i));
				String tempTreeLevel = xText.getString();

				if (tempCategory.isEmpty() && tempTreeLevel.isEmpty()) {
					_logger.debug("Skipping empty categories rec");
							
					continue;
				}

				if (tempCategory.isEmpty()) {
					String ms = "Categories: category in cell "
							+ getCellAddressString(0, i) + " must not be empty";

					errorsLocal.add(ms);
				}

				int tempTreeLevelInt = 0;
				if (tempTreeLevel.isEmpty() || !isNumeric(tempTreeLevel)) {
					String ms = "Categories: tree level " + tempTreeLevel
							+ " in cell " + getCellAddressString(1, i)
							+ " invalid, expected " + currentLevel;

					errorsLocal.add(ms);
				} else {
					tempTreeLevelInt = Integer.parseInt(tempTreeLevel);
					if (tempTreeLevelInt != currentLevel) {
						String ms = "Categories: tree level " + tempTreeLevel
								+ " in cell " + getCellAddressString(1, i)
								+ " invalid, expected " + currentLevel;

						errorsLocal.add(ms);
					}
				}
				if (errorsLocal.isEmpty()) {
					_logger.debug(
							"About to insert categories row to databse. ronwnum is "
									+ i);
					LoaderCategory cat = new LoaderCategory();
					cat.setSessionKey(_sessionKey);
					cat.setCategory(tempCategory);
					cat.setTreeLevel(tempTreeLevelInt);
					_mDao.addLoaderCategories(cat);
				} else {
					_logger.debug(
							"validation error for categories tab, row number "
									+ i);
					retSt.appendToErrors(errorsLocal);
				}
				currentLevel++;
			} /* for i= */
		} catch (Exception exp) {
			String ms = "Failed processing categories tab: " + exp.getMessage();
			_logger.debug(
					"Exception while processing row from categories tab, row number "
							+ exp.getMessage());
			retSt.appendToErrors(ms);
		}
	}

	/**
	 * @param xSpreadsheet
	 *            Spreadsheet containing Standards Tab data
	 * @param maxRows
	 *            max number of rows on this sheet
	 * @param retSt
	 *            return status class; method may add entries to the list of
	 *            errors; each entry corresponds to one error
	 */
	private void doStandardsTab(XSpreadsheet xSpreadsheet, int maxRows,
			ReturnStatus<String> retSt) {

		List<String> errorsLocal = new ArrayList<String>();
		/*
		 * LoggerFactory.LogDebug(
		 * "Starting processing of standards tab, number of rows: " + maxRows,
		 * OpenOfficeImporter.class);
		 */

		XText xText = null;
		if (maxRows == 0)
			return; /* no data rows on a tab */
		try {
			errorsLocal.clear();
			for (int i = 1; i <= maxRows; i++) {

				int tempLevelInt = 0;
				xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
						com.sun.star.text.XText.class,
						xSpreadsheet.getCellByPosition(0, i));
				String tempLevel = xText.getString();

				xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
						com.sun.star.text.XText.class,
						xSpreadsheet.getCellByPosition(1, i));
				String tempKey = xText.getString();

				xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
						com.sun.star.text.XText.class,
						xSpreadsheet.getCellByPosition(2, i));
				String tempName = xText.getString();

				xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
						com.sun.star.text.XText.class,
						xSpreadsheet.getCellByPosition(3, i));
				String tempDescription = xText.getString();

				xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
						com.sun.star.text.XText.class,
						xSpreadsheet.getCellByPosition(4, i));
				String tempShortName = xText.getString();

				if (tempLevel.isEmpty() && tempKey.isEmpty()
						&& tempName.isEmpty()) {
					_logger.debug("Skipping empty standards rec");
					
					continue;
				}

				if (tempLevel.isEmpty() || !isNumeric(tempLevel)) {
					String ms = "Standards: level in cell "
							+ getCellAddressString(0, i)
							+ " must be present and numeric";

					errorsLocal.add(ms);
				} else {
					tempLevelInt = Integer.parseInt(tempLevel);
				}

				if (tempKey.isEmpty()) {
					String ms = "Standards: key in cell "
							+ getCellAddressString(1, i) + " must not be empty";

					errorsLocal.add(ms);
				}

				if (tempName.isEmpty()) {
					String ms = "Standards: name in cell "
							+ getCellAddressString(2, i) + " must not be empty";

					errorsLocal.add(ms);
				}

				if (tempDescription.isEmpty())
					tempDescription = "";

				if (tempShortName.isEmpty())
					tempShortName = "";

				if (errorsLocal.isEmpty()) {
					_logger.debug(
							"About to insert standards row to databse. ronwnum is "
									+ i);
					LoaderStandard std = new LoaderStandard();
					std.setSessionKey(_sessionKey);
					std.setLevel(tempLevelInt);
					std.setKey(tempKey);
					std.setName(tempName);
					std.setDescription(tempDescription);
					std.setShortName(tempShortName);

					_mDao.addLoaderStandards(std);
				} else {
					_logger.debug(
							"validation error for standards tab, row number "
									+ i);
					retSt.appendToErrors(errorsLocal);
				}
			}

		} catch (Exception exp) {
			String ms = "Failed processing standards tab: " + exp.getMessage();
			_logger.debug(
					"Exception while processing row from standards tab, row number "
							+ exp.getMessage());
			retSt.appendToErrors(ms);
		}
	}

	/**
	 * @param xSpreadsheet
	 *            Spreadsheet containing SOCKs Tab data
	 * @param maxRows
	 *            max number of rows on this sheet
	 * @param retSt
	 *            return status class; method may add entries to the list of
	 *            errors; each entry corresponds to one error
	 */
	private void doSocksTab(XSpreadsheet xSpreadsheet, int maxRows,
			ReturnStatus<String> retSt) {

		XText xText = null;
		/*
		 * LoggerFactory.LogDebug(
		 * "Starting processing of SOCKs tab, number of rows: " + maxRows,
		 * OpenOfficeImporter.class);
		 */
		if (maxRows == 0)
			return; /* no data rows on a tab */
		try {
			for (int i = 1; i <= maxRows; i++) {
				xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
						com.sun.star.text.XText.class,
						xSpreadsheet.getCellByPosition(0, i));
				String tempKnowledgeCategory = xText.getString();

				xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
						com.sun.star.text.XText.class,
						xSpreadsheet.getCellByPosition(1, i));
				String tempDescription = xText.getString();

				if (tempKnowledgeCategory.isEmpty()) {
					_logger.debug("Skipping empty SOCKs rec");
							
					continue;
				}
				_logger
						.debug(
								"About to insert SOCKs row to databse. ronwnum is "
										+ i);
				LoaderSock socks = new LoaderSock();
				socks.setSessionKey(_sessionKey);
				socks.setKnowledgeCategory(tempKnowledgeCategory);
				socks.setDescription(tempDescription);
				_mDao.addLoaderSOCKs(socks);

			}
		} catch (Exception exp) {
			String ms = "Failed processing SOCKs tab: " + exp.getMessage();
			_logger.debug(
					"Exception while processing row from SOCKs tab, row number "
							+ exp.getMessage());
			retSt.appendToErrors(ms);
		}
	}

	/**
	 * @param xSpreadsheet
	 *            Spreadsheet containing BenchmarkGrades Tab data
	 * @param maxRows
	 *            max number of rows on this sheet
	 * @param retSt
	 *            return status class; method may add entries to the list of
	 *            errors; each entry corresponds to one error
	 */
	private void doBenchmarkTab(XSpreadsheet xSpreadsheet, int maxRows,
			ReturnStatus<String> retSt) {
		List<String> errorsLocal = new ArrayList<String>();
		XText xText = null;
		/*
		 * LoggerFactory.LogDebug(
		 * "Starting processing of benchmark grades tab, number of rows: " +
		 * maxRows, OpenOfficeImporter.class);
		 */
		if (maxRows == 0)
			return; /* no data rows on a tab */
		try {
			errorsLocal.clear();

			for (int i = 1; i <= maxRows; i++) {
				xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
						com.sun.star.text.XText.class,
						xSpreadsheet.getCellByPosition(0, i));
				String tempBenchmarkKey = xText.getString();

				xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
						com.sun.star.text.XText.class,
						xSpreadsheet.getCellByPosition(1, i));
				String tempGrade = xText.getString();

				if (tempBenchmarkKey.isEmpty() && tempGrade.isEmpty())
					continue;
				if (tempBenchmarkKey.isEmpty()) {
					String ms = "BenchmarkGrades: Key in cell "
							+ getCellAddressString(0, i) + " must not be empty";

					errorsLocal.add(ms);
				}
				if (tempGrade.isEmpty()) {
					String ms = "BenchmarkGrades: Grade in cell "
							+ getCellAddressString(1, i) + " must not be empty";

					errorsLocal.add(ms);
				}
				if (errorsLocal.isEmpty()) {
					_logger.debug(
							"About to insert BenchmarkGrades row to databse. ronwnum is "
									+ i);
					LoaderBenchmarkGrade benchmark = new LoaderBenchmarkGrade();
					benchmark.setSessionKey(_sessionKey);
					benchmark.setBenchmark(tempBenchmarkKey);
					benchmark.setGrade(tempGrade);

					_mDao.addLoaderBenchmarkGrades(benchmark);

				} else {
					_logger.debug(
							"validation error for benchmarkgrades tab, row number "
									+ i);
					retSt.appendToErrors(errorsLocal);
				}
			}
		} catch (Exception exp) {
			String ms = "Failed processing benchmarkgrades tab: "
					+ exp.getMessage();
			_logger.debug(
					"Exception while processing row from benchmarkgrades tab, row number "
							+ exp.getMessage());
			retSt.appendToErrors(ms);
		}
	}

	/**
	 * @param xSpreadsheet
	 *            Current Spreadsheet to be validated
	 * @param sheetName
	 *            Spreadsheet name (lower cased, spaces and underscores stripped
	 *            )
	 * @param colsNames
	 *            Array of column names this spreadsheet is expected to have
	 * @param errors
	 *            Method may add errors to list of error
	 * @return Returns max number of rows on this spreadsheet
	 */
	private int validateSheet(XSpreadsheet xSpreadsheet, String sheetName,
			String[] colsNames, List<String> errors) {
		int maxRows = 0;
		try {

			XCellRangesQuery xCellQuery = (XCellRangesQuery) UnoRuntime
					.queryInterface(XCellRangesQuery.class, xSpreadsheet);
			/* _xSpreadsheets.getByName(sheetName)); */
			XSheetCellRanges xFormulaCells = xCellQuery
					.queryContentCells((short) (com.sun.star.sheet.CellFlags.VALUE | com.sun.star.sheet.CellFlags.STRING));
			com.sun.star.table.CellRangeAddress addressRange[] = xFormulaCells
					.getRangeAddresses();
			maxRows = calculateRowsNumber(addressRange);
			if (maxRows == 0
					&& (sheetName.compareTo("publication") == 0
							|| sheetName.compareTo("categories") == 0
							|| sheetName.compareTo("standards") == 0 || sheetName
							.compareTo("benchmarkgrades") == 0)) {

				errors.add(sheetName + " tab must not be empty");
				return 0;
			}
			if (maxRows != 1 && (sheetName.compareTo("publication") == 0)) {
				errors.add("Publication tab must have only one rec");
				return 0;
			}
			for (int cCnt = 0; cCnt < colsNames.length; cCnt++) {
				String sheetColName = xSpreadsheet.getCellByPosition(cCnt, 0)
						.getFormula().trim().replace(" ", "");
				String colName = colsNames[cCnt].toLowerCase().replace(" ", "");
				if (!sheetColName.equalsIgnoreCase(colName)) {
					errors.add("Sheet "
							+ sheetName
							+ ":cannot find column name or column name is in a"
							+ "different location; column name in the sheet is "
							+ sheetColName + " and expecting " + colName);
				}
			}
		} catch (Exception exp) {
			errors.add(exp.getMessage() + exp.getCause());
			_logger.error(
					"Expection while validating spreadsheet tab " + sheetName);
					
			return 0;
		}
		_logger.debug("Streadsheet tab " + sheetName
				+ ", will be processging " + maxRows + " rows");
			
		return maxRows;

	}

	private void closeObjects() {
		_xSpreadsheetComponent.dispose();
	}

	/**
	 * Our best attempt to brute force terminate OpenOffice after we are done.
	 * Testing and following research showed that OpenOffice still runs even
	 * after our application finished its processing. The below functionality
	 * was proposed on OpenOffice forum:
	 * http://forum.openoffice.org/en/forum/viewtopic.php?f=44&t=2520&start=30
	 */
	private void closeOpenOffice() {
		_bootstrapSocketConnector.disconnect();
		/* oooServer.kill(); */
		String osName = System.getProperty("os.name");
		try {
			if (osName.startsWith("Windows"))
				windowsKillOpenOffice();
			else if (osName.startsWith("SunOS") || osName.startsWith("Linux"))
				unixKillOpenOffice();
		} catch (IOException exp) {
			_logger.error("Failed to kill OpenOffice");
				
			/* System.err.println("Failed to kill OpenOffice"); */
		}

	}

	private static void windowsKillOpenOffice() throws IOException {
		Runtime.getRuntime().exec("tskill soffice");
	}

	private static void unixKillOpenOffice() throws IOException {
		Runtime runtime = Runtime.getRuntime();

		String pid = getOpenOfficeProcessID();
		if (pid != null) {
			while (pid != null) {
				String[] killCmd = { "/bin/bash", "-c", "kill -9 " + pid };
				runtime.exec(killCmd);

				// Is another OpenOffice prozess running?
				pid = getOpenOfficeProcessID();
			}
		}
	}

	/**
	 * Get OpenOffice process id.
	 */
	private static String getOpenOfficeProcessID() throws IOException {
		Runtime runtime = Runtime.getRuntime();

		// Get process id
		String[] getPidCmd = { "/bin/bash", "-c",
				"ps -e|grep soffice|awk '{print $1}'" };
		Process getPidProcess = runtime.exec(getPidCmd);

		// Read process id
		InputStreamReader isr = new InputStreamReader(
				getPidProcess.getInputStream());
		BufferedReader br = new BufferedReader(isr);

		return br.readLine();
	}

	/**
	 * @param requiredSheets
	 *            Array if strings containing names of required spreadsheet tabs
	 * @param processedSheets
	 *            Set; contains list of spreadsheet tab names that were
	 *            processed
	 * @param retSt
	 *            return status class; method may add entries to the list of
	 *            errors; each entry corresponds to one error
	 */
	private void missingSheets(String[] requiredSheets,
			HashSet<String> processedSheets, ReturnStatus<String> retSt) {

		for (String s : requiredSheets) {
			if (!processedSheets.contains(s)) {
				String ms = "Missing required sheet " + s + " in file";

				retSt.appendToErrors(ms);
			}
		}
	}

	/**
	 * @param addressRange
	 *            Array of address ranges for the given spreadsheet
	 * @return Returns number that is max row row number for any address in the
	 *         range
	 */
	private static int calculateRowsNumber(
			com.sun.star.table.CellRangeAddress addressRange[]) {
		int nm = 0;
		for (int i = 0; i < addressRange.length; i++) {
			if (addressRange[i].EndRow > nm)
				nm = addressRange[i].EndRow;
		}

		return nm;
	}

	private static boolean isNumeric(String str) {
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c))
				return false;
		}

		return true;
	}

	private String getCellAddressString(int nColumn, int nRow) {
		String aStr = "";
		if (nColumn > 25)
			aStr += (char) ('A' + nColumn / 26 - 1);
		aStr += (char) ('A' + nColumn % 26);
		aStr += (nRow + 1);
		return aStr;
	}

	private static String replaceString(String s, String sMatch, String sReplace) {
		if (sReplace == null)
			sReplace = "";

		if (sMatch == null || "".equals(sMatch) || sMatch.equals(sReplace))
			return s;

		if (s == null || s.equals("")) {
			return "";
		}

		int i = 0;
		int j = s.indexOf(sMatch);

		if (j < 0) {
			return s;
		}

		StringBuffer sb = new StringBuffer(s.length());

		while (true) {
			sb.append(s.substring(i, j));
			sb.append(sReplace);

			i = j + sMatch.length();
			j = s.indexOf(sMatch, i);

			if (j < 0) {
				sb.append(s.substring(i));
				break;
			}
		}

		return sb.toString();
	}

	private static void delay() {

		int count = 0;
		long exittime = System.currentTimeMillis() + DELAY_MILLISEC;

		while (true) {
			count++;

			if (System.currentTimeMillis() > exittime) {
				break;
			}
		}
	}
}
