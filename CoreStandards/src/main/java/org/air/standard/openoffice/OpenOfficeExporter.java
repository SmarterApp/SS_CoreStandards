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
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ooo.connector.BootstrapSocketConnector;
import ooo.connector.server.OOoServer;

import org.air.standard.common.LoaderBenchmarkGrade;
import org.air.standard.common.LoaderCategory;
import org.air.standard.common.LoaderSock;
import org.air.standard.common.Publication;
import org.air.standard.common.ReturnStatus;
import org.air.standard.common.Standard;
import org.air.standard.dao.CategoryDAO;
import org.air.standard.dao.GradeDAO;
import org.air.standard.dao.PublicationDAO;
import org.air.standard.dao.SocksDAO;
import org.air.standard.dao.StandardDAO;
import org.air.standard.exceptions.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
/*import com.sun.star.table.XCell; */
import com.sun.star.text.XText;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;


public class OpenOfficeExporter {
	
	Logger _logger = LoggerFactory.getLogger(OpenOfficeExporter.class);
	
	static final String TEST_SESSION_KEY = "";
	static final String PUBLICATION_TAB = "Publication";
	static final String CATEGORIES_TAB = "Categories";
	static final String STANDARDS_TAB = "Standards";
	static final String SOCKS_TAB = "SOCKs";
	static final String BENCHMARK_GRADES_TAB = "Benchmark Grades";

	static final String CATEGORIES_CATEGORY_COLUMN = "Category";
	static final String CATEGORIES_TREE_LEVEL_COLUMN = "Tree Level";

	static final String PUBLICATION_PUBLISHER_NAME_COLUMN = "PublisherName";
	static final String PUBLICATION_PUBLICATION_DESCRIPTION_COLUMN = "PublicationDescription";
	static final String PUBLICATION_SUBJECT_COLUMN = "Subject";
	static final String PUBLICATION_VERSION_COLUMN = "Version";

	static final String STANDARDS_LEVEL_COLUMN = "Level";
	static final String STANDARDS_KEY_COLUMN = "Key";
	static final String STANDARDS_NAME_COLUMN = "Name";
	static final String STANDARDS_DESCRIPTION_COLUMN = "Description";
	static final String STANDARDS_SHORT_NAME_COLUMN = "ShortName";

	static final String SOCKS_KNOWLEDGE_CATEGORY_COLUMN = "Knowledge Category";
	static final String SOCKS_DESCRIPTION_COLUMN = "Description";

	static final String BENCHMARK_GRADES_BENCHMARK_KEY_COLUMN = "Benchmark Key";
	static final String BENCHMARK_GRADES_GRADE_COLUMN = "Grade";

    static final int MAX_OO_ATTEMPTS = 5;
	static final int DELAY_MILLISEC = 3000;
	private String _oooExecFolder = null;
	private String _tempFileFolder = null;
	private XComponentContext _xRemoteContext = null;
	private XMultiComponentFactory _xRemoteServiceManager = null;
	private XSpreadsheetDocument _xSpreadsheetDocument = null;
	private XSpreadsheets _xSpreadsheets = null;
	private XComponent _xSpreadsheetComponent = null;
	private BootstrapSocketConnector _bootstrapSocketConnector = null;
	private String _exportFileName = null;

	private PublicationDAO _mPubDao = new PublicationDAO();
	private CategoryDAO _mCatDAO = new CategoryDAO();
	private StandardDAO _mStandDAO = new StandardDAO();
	private SocksDAO _mSocksDAO = new SocksDAO();
	private GradeDAO _mGradeDAO = new GradeDAO();

	public OpenOfficeExporter() {
		super();
	}

	private void insertColumnsHeaders() {
		com.sun.star.sheet.XSpreadsheet xSpreadsheet;
		try {
			xSpreadsheet = (com.sun.star.sheet.XSpreadsheet) UnoRuntime
					.queryInterface(com.sun.star.sheet.XSpreadsheet.class,
							_xSpreadsheets.getByName(PUBLICATION_TAB));

			xSpreadsheet.getCellByPosition(0, 0).setFormula(
					PUBLICATION_PUBLISHER_NAME_COLUMN);
			xSpreadsheet.getCellByPosition(1, 0).setFormula(
					PUBLICATION_PUBLICATION_DESCRIPTION_COLUMN);
			xSpreadsheet.getCellByPosition(2, 0).setFormula(
					PUBLICATION_SUBJECT_COLUMN);
			xSpreadsheet.getCellByPosition(3, 0).setFormula(
					PUBLICATION_VERSION_COLUMN);

			xSpreadsheet = (com.sun.star.sheet.XSpreadsheet) UnoRuntime
					.queryInterface(com.sun.star.sheet.XSpreadsheet.class,
							_xSpreadsheets.getByName(CATEGORIES_TAB));

			xSpreadsheet.getCellByPosition(0, 0).setFormula(
					CATEGORIES_CATEGORY_COLUMN);
			xSpreadsheet.getCellByPosition(1, 0).setFormula(
					CATEGORIES_TREE_LEVEL_COLUMN);

			xSpreadsheet = (com.sun.star.sheet.XSpreadsheet) UnoRuntime
					.queryInterface(com.sun.star.sheet.XSpreadsheet.class,
							_xSpreadsheets.getByName(STANDARDS_TAB));

			xSpreadsheet.getCellByPosition(0, 0).setFormula(
					STANDARDS_LEVEL_COLUMN);
			xSpreadsheet.getCellByPosition(1, 0).setFormula(
					STANDARDS_KEY_COLUMN);
			xSpreadsheet.getCellByPosition(2, 0).setFormula(
					STANDARDS_NAME_COLUMN);
			xSpreadsheet.getCellByPosition(3, 0).setFormula(
					STANDARDS_DESCRIPTION_COLUMN);
			xSpreadsheet.getCellByPosition(4, 0).setFormula(
					STANDARDS_SHORT_NAME_COLUMN);

			xSpreadsheet = (com.sun.star.sheet.XSpreadsheet) UnoRuntime
					.queryInterface(com.sun.star.sheet.XSpreadsheet.class,
							_xSpreadsheets.getByName(SOCKS_TAB));

			xSpreadsheet.getCellByPosition(0, 0).setFormula(
					SOCKS_KNOWLEDGE_CATEGORY_COLUMN);
			xSpreadsheet.getCellByPosition(1, 0).setFormula(
					SOCKS_DESCRIPTION_COLUMN);

			xSpreadsheet = (com.sun.star.sheet.XSpreadsheet) UnoRuntime
					.queryInterface(com.sun.star.sheet.XSpreadsheet.class,
							_xSpreadsheets.getByName(BENCHMARK_GRADES_TAB));

			xSpreadsheet.getCellByPosition(0, 0).setFormula(
					BENCHMARK_GRADES_BENCHMARK_KEY_COLUMN);
			xSpreadsheet.getCellByPosition(0, 0).setFormula(
					BENCHMARK_GRADES_GRADE_COLUMN);

		} catch (Exception exp) {
			_logger.info("Failed setting columns headers in spreadsheet doc");
			/*System.err
					.println("Failed setting columns headers in spreadsheet doc"); */

		}
	}

	/**
	 * @throws RuntimeException
	 * @throws Exception
	 */
	private void initDoc() throws RuntimeException, Exception {
		Object aColumnObjTarget;
		XPropertySet xPropSetTarget;

        this._xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
				.queryInterface(XSpreadsheetDocument.class,
						_xSpreadsheetComponent);
		this._xSpreadsheets = _xSpreadsheetDocument.getSheets();

		_xSpreadsheets.insertNewByName(PUBLICATION_TAB, (short) 0);
		_xSpreadsheets.insertNewByName(CATEGORIES_TAB, (short) 1);
		_xSpreadsheets.insertNewByName(STANDARDS_TAB, (short) 2);
		_xSpreadsheets.insertNewByName(SOCKS_TAB, (short) 3);
		_xSpreadsheets.insertNewByName(BENCHMARK_GRADES_TAB, (short) 4);
		/* make sure all tabs were successfully created in output document */

		if (!_xSpreadsheets.hasByName(PUBLICATION_TAB)) {
			_logger.error("Failed to create Publication tab");
			/*System.err.println("Target: Failed to create Publication tab"); */
			throw new com.sun.star.container.NoSuchElementException(
					"Failed to create Publication tab");
		} else if (!_xSpreadsheets.hasByName(CATEGORIES_TAB)) {
			_logger.error("Failed to create Categories tab");
			/*System.err.println("Target: Failed to create Categories tab");*/
			throw new com.sun.star.container.NoSuchElementException(
					"Failed to create Categories tab");
		} else if (!_xSpreadsheets.hasByName(STANDARDS_TAB)) {
			_logger.error("Failed to create Standards tab");
			/*System.err.println("Target: Failed to create Standards tab");*/
			throw new com.sun.star.container.NoSuchElementException(
					"Failed to create Standards tab");
		} else if (!_xSpreadsheets.hasByName(SOCKS_TAB)) {
			_logger.error("Failed to create SOCKs tab");
			/*System.err.println("Target: Failed to create SOCKs tab"); */
			throw new com.sun.star.container.NoSuchElementException(
					" Failed to create SOCKs tab");
		} else if (!_xSpreadsheets.hasByName(BENCHMARK_GRADES_TAB)) {
			_logger.error("Failed to create Benchmark Grades tab");
			/*System.err.println("Target: Failed to create Benchmark Grades tab"); */
			throw new com.sun.star.container.NoSuchElementException(
					"Failed to create Benchmark Grades tab");
		}

		com.sun.star.sheet.XSpreadsheet xSpreadsheetTarget = (com.sun.star.sheet.XSpreadsheet) UnoRuntime
				.queryInterface(com.sun.star.sheet.XSpreadsheet.class,
						_xSpreadsheets.getByName(PUBLICATION_TAB));
		com.sun.star.table.XColumnRowRange xCRRangeTarget = (com.sun.star.table.XColumnRowRange) UnoRuntime
				.queryInterface(com.sun.star.table.XColumnRowRange.class,
						xSpreadsheetTarget);
		com.sun.star.table.XTableColumns xColumnsTarget = xCRRangeTarget
				.getColumns();
		for (int i = 0; i < 4; i++) {
			aColumnObjTarget = xColumnsTarget.getByIndex(i);
			xPropSetTarget = (com.sun.star.beans.XPropertySet) UnoRuntime
					.queryInterface(com.sun.star.beans.XPropertySet.class,
							aColumnObjTarget);
			switch (i) {
			case 0:
				xPropSetTarget.setPropertyValue("Width", new Integer(2700));
				break;
			case 1:
				xPropSetTarget.setPropertyValue("Width", new Integer(10000));
				break;
			case 2:
				xPropSetTarget.setPropertyValue("Width", new Integer(3800));
				break;
			case 3:
				xPropSetTarget.setPropertyValue("Width", new Integer(1700));
				break;
			}
		}

		xSpreadsheetTarget = (com.sun.star.sheet.XSpreadsheet) UnoRuntime
				.queryInterface(com.sun.star.sheet.XSpreadsheet.class,
						_xSpreadsheets.getByName(CATEGORIES_TAB));
		xCRRangeTarget = (com.sun.star.table.XColumnRowRange) UnoRuntime
				.queryInterface(com.sun.star.table.XColumnRowRange.class,
						xSpreadsheetTarget);
		xColumnsTarget = xCRRangeTarget.getColumns();
		for (int i = 0; i < 2; i++) {
			aColumnObjTarget = xColumnsTarget.getByIndex(i);
			xPropSetTarget = (com.sun.star.beans.XPropertySet) UnoRuntime
					.queryInterface(com.sun.star.beans.XPropertySet.class,
							aColumnObjTarget);
			switch (i) {
			case 0:
				xPropSetTarget.setPropertyValue("Width", new Integer(6600));
				break;
			case 1:
				xPropSetTarget.setPropertyValue("Width", new Integer(3100));
				xPropSetTarget.setPropertyValue("HoriJustify",
						com.sun.star.table.CellHoriJustify.CENTER);
				break;
			}
		}

		xSpreadsheetTarget = (com.sun.star.sheet.XSpreadsheet) UnoRuntime
				.queryInterface(com.sun.star.sheet.XSpreadsheet.class,
						_xSpreadsheets.getByName(STANDARDS_TAB));
		xCRRangeTarget = (com.sun.star.table.XColumnRowRange) UnoRuntime
				.queryInterface(com.sun.star.table.XColumnRowRange.class,
						xSpreadsheetTarget);
		xColumnsTarget = xCRRangeTarget.getColumns();
		for (int i = 0; i < 5; i++) {
			aColumnObjTarget = xColumnsTarget.getByIndex(i);
			xPropSetTarget = (com.sun.star.beans.XPropertySet) UnoRuntime
					.queryInterface(com.sun.star.beans.XPropertySet.class,
							aColumnObjTarget);
			switch (i) {
			case 0:
				xPropSetTarget.setPropertyValue("Width", new Integer(1700));
				xPropSetTarget.setPropertyValue("HoriJustify",
						com.sun.star.table.CellHoriJustify.CENTER);
				break;
			case 1:
				xPropSetTarget.setPropertyValue("Width", new Integer(5600));
				xPropSetTarget.setPropertyValue("HoriJustify",
						com.sun.star.table.CellHoriJustify.CENTER);
				break;
			case 2:
				xPropSetTarget.setPropertyValue("Width", new Integer(5600));
				xPropSetTarget.setPropertyValue("HoriJustify",
						com.sun.star.table.CellHoriJustify.CENTER);
				break;
			case 3:
				xPropSetTarget.setPropertyValue("Width", new Integer(12000));
				xPropSetTarget.setPropertyValue("IsTextWrapped", true);

				break;
			case 4:
				xPropSetTarget.setPropertyValue("Width", new Integer(5300));
				xPropSetTarget.setPropertyValue("HoriJustify",
						com.sun.star.table.CellHoriJustify.CENTER);
				xPropSetTarget.setPropertyValue("IsTextWrapped", true);
				break;
			}
		}

		xSpreadsheetTarget = (com.sun.star.sheet.XSpreadsheet) UnoRuntime
				.queryInterface(com.sun.star.sheet.XSpreadsheet.class,
						_xSpreadsheets.getByName(SOCKS_TAB));
		xCRRangeTarget = (com.sun.star.table.XColumnRowRange) UnoRuntime
				.queryInterface(com.sun.star.table.XColumnRowRange.class,
						xSpreadsheetTarget);
		xColumnsTarget = xCRRangeTarget.getColumns();
		for (int i = 0; i < 2; i++) {
			aColumnObjTarget = xColumnsTarget.getByIndex(i);
			xPropSetTarget = (com.sun.star.beans.XPropertySet) UnoRuntime
					.queryInterface(com.sun.star.beans.XPropertySet.class,
							aColumnObjTarget);
			switch (i) {
			case 0:
				xPropSetTarget.setPropertyValue("Width", new Integer(4600));
				break;
			case 1:
				xPropSetTarget.setPropertyValue("Width", new Integer(4100));
				break;
			}
		}
		xSpreadsheetTarget = (com.sun.star.sheet.XSpreadsheet) UnoRuntime
				.queryInterface(com.sun.star.sheet.XSpreadsheet.class,
						_xSpreadsheets.getByName(BENCHMARK_GRADES_TAB));
		xCRRangeTarget = (com.sun.star.table.XColumnRowRange) UnoRuntime
				.queryInterface(com.sun.star.table.XColumnRowRange.class,
						xSpreadsheetTarget);
		xColumnsTarget = xCRRangeTarget.getColumns();
		for (int i = 0; i < 2; i++) {
			aColumnObjTarget = xColumnsTarget.getByIndex(i);
			xPropSetTarget = (com.sun.star.beans.XPropertySet) UnoRuntime
					.queryInterface(com.sun.star.beans.XPropertySet.class,
							aColumnObjTarget);
			switch (i) {
			case 0:
				xPropSetTarget.setPropertyValue("Width", new Integer(5700));
				break;
			case 1:
				xPropSetTarget.setPropertyValue("Width", new Integer(1800));
				xPropSetTarget.setPropertyValue("HoriJustify",
						com.sun.star.table.CellHoriJustify.CENTER);
				break;
			}
		}
		/* insert column headers */
		insertColumnsHeaders(); 
	}

	/**
	 * @param retSt
	 */
	private void connect(ReturnStatus<String> retSt) {
	
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
				
				PropertyValue[] loadProps = new PropertyValue[1];
				loadProps[0] = new com.sun.star.beans.PropertyValue();
				loadProps[0].Name = "Hidden";
				loadProps[0].Value = new Boolean(true);

				this._xSpreadsheetComponent = xComponentLoader
						.loadComponentFromURL("private:factory/scalc", "_blank", 0, loadProps);
				
				return;
			} catch (RuntimeException re) {
				/* probably OO is not installed properly */
				_logger.error(
						"Check if OpenOffice installed properly on server box! "
								+ re.getMessage());
				retSt.appendToErrors("Problems with OpenOffice install on server box");
				retSt.setStatus("failed");
				return;
				
			}  catch (BootstrapException be) {
				
			    /* get here if _bootstrapSocketConnector.connect() failed,
			     *  no need to call disconnect() */
				if (be.getMessage() == null)
					retSt.appendToErrors("Problem accessing Open Office software.");
				else
					retSt.appendToErrors("Problem accessing Open Office software."
							+ be.getMessage());			   
				retSt.setStatus("failed");
				return;
			}	catch (Exception e) {
							
				/*System.out.println("Attempt number " + i
						+ " ERROR: failed initializing OO doc. "
						+ e.getMessage()); */

				if (i == MAX_OO_ATTEMPTS - 1) {
					_logger.error("failed initializing OO doc."
							+ e.getMessage());
					retSt.appendToErrors("ERROR:  failed initializing OO doc."
							+ e.getMessage());
					retSt.setStatus("failed");
					/* bootstrap connection was established at this point, let's disconnect it before returning */
					_bootstrapSocketConnector.disconnect();
					return;
				} else {
					 _bootstrapSocketConnector.disconnect();
					 _logger.debug("loadComponent,Hitting delay"); 
					/*System.out.println("loadComponent,Hitting delay"); */
					delay();
				}
			}
		}
    }

	/**
	 * @param oooExecFolder
	 *            Path to local folder containing OpenOffice executable file
	 *            soffice
	 * @param tempFileFolder
	 *            local directory where exportToFile method places output OO file
	 * @param sessionKey
	 *            unique session key
	 * @param retSt
	 *            return status class instance, method may add entries to list of
	 *            errors.
	 *            retSt.getStatus is set to 'success' or 'failed'
	 * @return
	 *            method returns name of the output OO file it created.
	 *            null is returned in case of errors.
	 * @throws DaoException 
	 */
	public String exportToFile(String oooExecFolder, String tempFileFolder,
			String sessionKey, ReturnStatus<String> retSt) throws DaoException  {
		this._oooExecFolder = oooExecFolder;
		this._tempFileFolder = tempFileFolder;
		List<String> errors = new ArrayList<String>();

		setExportFileName(sessionKey);

		connect(retSt);
	    if (retSt.getStatus().equalsIgnoreCase("failed")) {
			/* all closings were already done in connect method */	
			return null;
		}
		try {
			initDoc();
		} catch (Exception e) {
			_logger.error ("failed initializing output file: "
					+ e.getMessage());
			/*System.out.println("init outputfile problems: " + e.getMessage()); */
			retSt.appendToErrors("failed initializing output file: "
					+ e.getMessage());
			retSt.setStatus("failed");
			e.printStackTrace();
			return null;
		}
        try {
		List<Publication> listPub = _mPubDao
				.getPublicationsBySessionKey(sessionKey);
        
		for (int i = 0; i < listPub.size(); i++) {
			Publication pub = listPub.get(i);
			
			try {
				insertIntoTargetPublication(i + 1, pub);
			} catch (Exception ex) {
				String msg = "Failed exporting to Publication tab. row "
						+ (i + 1) + ex.getMessage();
				/*System.err.println(msg); */
				_logger.error(msg)	;	
				
                retSt.appendToErrors (msg);
				retSt.setStatus("failed");
				_xSpreadsheetComponent.dispose();
				closeOpenOffice();
				return null;
			}
		}

		List<LoaderCategory> listCat = _mCatDAO
				.getCategoryBySessionKey(sessionKey);
		for (int i = 0; i < listCat.size(); i++) {
			LoaderCategory cat = listCat.get(i);

			try {
				insertIntoTargetCategories(i + 1, cat);
			} catch (Exception ex) {
				String msg = "Failed exporting to Cateogires tab. row " + (i + 1) + ex.getMessage();
				/*System.err.println(msg); */
				_logger.error(msg)	;
				
                retSt.appendToErrors(msg);
				retSt.setStatus("failed");
				_xSpreadsheetComponent.dispose();
				closeOpenOffice();
				return null;
			}
		}

		List<Standard> listStand = _mStandDAO
				.getStandardsBySessionKey(sessionKey);
		for (int i = 0; i < listStand.size(); i++) {
			Standard stand = listStand.get(i);

			try {
				insertIntoTargetStandards(i + 1, stand);
			} catch (Exception ex) {
				String msg = "Failed exporting to Standards tab. row "
						+ (i + 1) + ex.getMessage();
				/*System.err.println(msg); */
				_logger.error(msg)	;
				
				retSt.appendToErrors(msg);
				retSt.setStatus("failed");
				_xSpreadsheetComponent.dispose();
				closeOpenOffice();
				return null;
			}
		}

		List<LoaderSock> listSocks = _mSocksDAO
				.getSocksBySessionKey(sessionKey);
		for (int i = 0; i < listSocks.size(); i++) {
			LoaderSock socks = listSocks.get(i);

			try {
				insertIntoTargetSocks(i + 1, socks);
			} catch (Exception ex) {
				String msg = "Failed exporting to SOCKS tab. row "
						+ (i + 1) + ex.getMessage();
				/*System.err.println(msg); */
				_logger.error(msg)	;
				
				retSt.appendToErrors(msg);
				retSt.setStatus("failed");
				_xSpreadsheetComponent.dispose();
				closeOpenOffice();
				return null;
			}
		}

		List<LoaderBenchmarkGrade> listBench = _mGradeDAO
				.getBenchmarkGradesBySessionKey(sessionKey);
		for (int i = 0; i < listBench.size(); i++) {
			LoaderBenchmarkGrade bench = listBench.get(i);

			try {
				insertIntoTargetBenchmark(i + 1, bench);
			} catch (Exception ex) {
				String msg = "Failed exporting to BenchmarkGrades tab. row "
						+ (i + 1) + ex.getMessage();
				/*System.err.println(msg); */
				_logger.error(msg)	;
				
				retSt.appendToErrors(msg);
				_xSpreadsheetComponent.dispose();
				closeOpenOffice();
				return null;
			}
		}
        } catch (SQLException se) {
            String msg = "SQL problem retrieving data from DB. " + se.getMessage();
            /*LoggerFactory.LogError(msg,  OpenOfficeExporter.class); */
            retSt.appendToErrors(msg);
            retSt.setStatus("failed");
            _xSpreadsheetComponent.dispose();
			closeOpenOffice();
			return null;
        }
		try {
			saveTargetFile(sessionKey);
			closeOpenOffice();
            retSt.setStatus("success");
			return _exportFileName;
		} catch (Exception exp) {
			String msg = "Failed to save temp file" + exp.getMessage();
			/*System.out.println(msg + exp.getMessage()); */
			_logger.error(msg)	;
			
			_xSpreadsheetComponent.dispose();
			closeOpenOffice();
			retSt.appendToErrors(msg);
			retSt.setStatus("failed");
			return null;
		}
	}

	/**
	 * @param rowNumber
	 * @param pub
	 * @throws Exception
	 */
	/**
	 * @param rowNumber
	 * @param pub
	 * @throws Exception
	 */
	private void insertIntoTargetPublication(int rowNumber,
			Publication pub) throws Exception {
		XText xText = null;
		com.sun.star.sheet.XSpreadsheet xSpreadsheet = (com.sun.star.sheet.XSpreadsheet) UnoRuntime
				.queryInterface(com.sun.star.sheet.XSpreadsheet.class,
						_xSpreadsheets.getByName(PUBLICATION_TAB));
		
		xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
				com.sun.star.text.XText.class,
				xSpreadsheet.getCellByPosition(0, rowNumber));
		xText.setString(pub.getKey());

		xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
				com.sun.star.text.XText.class,
				xSpreadsheet.getCellByPosition(1, rowNumber));

		xText.setString(pub.getDescription());

		xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
				com.sun.star.text.XText.class,
				xSpreadsheet.getCellByPosition(2, rowNumber));

		xText.setString(pub.getSubjectLabel());

		xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
				com.sun.star.text.XText.class,
				xSpreadsheet.getCellByPosition(3, rowNumber));

		xText.setString(Double.toString(pub.getVersion()));

	}

	/**
	 * @param rowNumber
	 * @param cat
	 * @throws Exception
	 */
	private void insertIntoTargetCategories(int rowNumber, LoaderCategory cat)
			throws Exception {
		XText xText = null;
		com.sun.star.sheet.XSpreadsheet xSpreadsheet = (com.sun.star.sheet.XSpreadsheet) UnoRuntime
				.queryInterface(com.sun.star.sheet.XSpreadsheet.class,
						_xSpreadsheets.getByName(CATEGORIES_TAB));
		xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
				com.sun.star.text.XText.class,
				xSpreadsheet.getCellByPosition(0, rowNumber));

		xText.setString(cat.getCategory());
		xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
				com.sun.star.text.XText.class,
				xSpreadsheet.getCellByPosition(1, rowNumber));
		xText.setString(Integer.toString(cat.getTreeLevel()));

	}

	/**
	 * @param rowNumber
	 * @param stand
	 * @throws Exception
	 */
	private void insertIntoTargetStandards(int rowNumber, Standard stand)
			throws Exception {
		XText xText = null;
		com.sun.star.sheet.XSpreadsheet xSpreadsheet = (com.sun.star.sheet.XSpreadsheet) UnoRuntime
				.queryInterface(com.sun.star.sheet.XSpreadsheet.class,
						_xSpreadsheets.getByName(STANDARDS_TAB));

		xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
				com.sun.star.text.XText.class,
				xSpreadsheet.getCellByPosition(0, rowNumber));
		
		xText.setString(Integer.toString(stand.getTreeLevel()));

		xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
				com.sun.star.text.XText.class,
				xSpreadsheet.getCellByPosition(1, rowNumber));
		xText.setString(stand.getKey());

		xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
				com.sun.star.text.XText.class,
				xSpreadsheet.getCellByPosition(2, rowNumber));
		xText.setString(stand.getName());

		xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
				com.sun.star.text.XText.class,
				xSpreadsheet.getCellByPosition(3, rowNumber));
		xText.setString(stand.getDescription());

		xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
				com.sun.star.text.XText.class,
				xSpreadsheet.getCellByPosition(4, rowNumber));
		xText.setString(stand.getShortName());
	}

	/**
	 * @param rowNumber
	 * @param socks
	 * @throws Exception
	 */
	private void insertIntoTargetSocks(int rowNumber, LoaderSock socks)
			throws Exception {
		XText xText = null;
		com.sun.star.sheet.XSpreadsheet xSpreadsheet = (com.sun.star.sheet.XSpreadsheet) UnoRuntime
				.queryInterface(com.sun.star.sheet.XSpreadsheet.class,
						_xSpreadsheets.getByName(SOCKS_TAB));

		xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
				com.sun.star.text.XText.class,
				xSpreadsheet.getCellByPosition(0, rowNumber));
		
		xText.setString(socks.getKnowledgeCategory());

		xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
				com.sun.star.text.XText.class,
				xSpreadsheet.getCellByPosition(1, rowNumber));
		
		xText.setString(socks.getNewDescription());
	}

	/**
	 * @param rowNumber
	 * @param ben
	 * @throws Exception
	 */
	private void insertIntoTargetBenchmark(int rowNumber, LoaderBenchmarkGrade ben)
			throws Exception {
		XText xText = null;
		com.sun.star.sheet.XSpreadsheet xSpreadsheet = (com.sun.star.sheet.XSpreadsheet) UnoRuntime
				.queryInterface(com.sun.star.sheet.XSpreadsheet.class,
						_xSpreadsheets.getByName(BENCHMARK_GRADES_TAB));

		xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
				com.sun.star.text.XText.class,
				xSpreadsheet.getCellByPosition(0, rowNumber));
		
		xText.setString(ben.getBenchmark());

		xText = (com.sun.star.text.XText) UnoRuntime.queryInterface(
				com.sun.star.text.XText.class,
				xSpreadsheet.getCellByPosition(1, rowNumber));
		xText.setString(ben.getGrade());
	}

	/**
	 * @param sessionKey
	 *             output file name is generated as session key, followed by underscore,
	 *             followed by date in yyyy.MM.dd_HH.mm.ss followed by ".ods"
	 *             Note that HH in the date format is the 24 hours scale.
	 */
	private void setExportFileName(String sessionKey) {
		Calendar date = Calendar.getInstance();
		SimpleDateFormat dateformatter = new SimpleDateFormat(
				"yyyy.MM.dd_HH.mm.ss");
		this._exportFileName = sessionKey + "_"
				+ dateformatter.format(date.getTime()) + ".ods";
	}

	/**
	 * @param sessionKey
	 * @throws Exception
	 */
	private void saveTargetFile(String sessionKey) throws Exception {
		com.sun.star.frame.XStorable xStorable = (com.sun.star.frame.XStorable) UnoRuntime
				.queryInterface(com.sun.star.frame.XStorable.class,
						_xSpreadsheetComponent);

		String filePath = _tempFileFolder + "/" + _exportFileName;

		java.io.File targetFile = new java.io.File(filePath);

		StringBuffer sbTmp = new StringBuffer("file:///");
		sbTmp.append(targetFile.getCanonicalPath().replace('\\', '/'));
		String sStoreUrl = sbTmp.toString();		
		/*System.out.println("export file path:" + sStoreUrl); */

		PropertyValue[] propertyStoreValue = new com.sun.star.beans.PropertyValue[2];
		propertyStoreValue[0] = new com.sun.star.beans.PropertyValue();
		propertyStoreValue[0].Name = "Overwrite";
		propertyStoreValue[0].Value = new Boolean(true);
		propertyStoreValue[1] = new com.sun.star.beans.PropertyValue();
		propertyStoreValue[1].Name = "FilterName";
		propertyStoreValue[1].Value = "StarOffice XML (Writer)";

		xStorable.storeAsURL(sStoreUrl, propertyStoreValue);

		com.sun.star.util.XCloseable xStoreCloseable = (com.sun.star.util.XCloseable) UnoRuntime
				.queryInterface(com.sun.star.util.XCloseable.class,
						_xSpreadsheetComponent);
		if (xStoreCloseable != null) {
			xStoreCloseable.close(false);
		} else {
			_xSpreadsheetComponent.dispose();
		}
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
			/*System.err.println("Failed to kill OpenOffice"); */
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
     *   This function causes process to delay for DELAY_MILLISEC milliseconds
     */
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
