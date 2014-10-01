/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.openoffice.test;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import org.air.shared.db.AbstractConnectionManager;
import org.air.shared.db.standalone.StandaloneConnectionManager;
import org.air.shared.resources.ResourceExistsException;
import org.air.shared.resources.ResourceInitializationException;
import org.air.shared.resources.ResourceManager;
import org.air.standard.common.ReturnStatus;
import org.air.standard.exceptions.DaoException;
import org.air.standard.openoffice.OpenOfficeExporter;
import org.air.standard.openoffice.OpenOfficeImporter;

public class UnitTestOpenOffice {
	private static final String OOO_EXEC_FOLDER = "C:/Program Files (x86)/OpenOffice.org 3/program";
	private static String TEST_SESSION_KEY = "e8bfb77c-80fb-11e2-b973-00215ae9";

	public static void main(String argv[]) throws DaoException /*
																 * throws
																 * SQLException
																 */{
		try {
			registerDatabaseResourceManager();
		} catch (Exception exp) {
			System.err.println(exp.getMessage());
			System.err.println(exp.getStackTrace());
		}

		OpenOfficeImporter importer = new OpenOfficeImporter();

		// System.out.println("Starting");

		/* EF: Change the file name to your file before running this program! */
		ReturnStatus<String> retSt = new ReturnStatus<String>("success");
		importer.importFromFile(OOO_EXEC_FOLDER,
				"C:/Users/efurman/Downloads/temp.spare.ods", TEST_SESSION_KEY,
				retSt);
		// System.out.println("ReturnStatus.status " + retSt.getStatus());
		List<String> theErrors = retSt.getErrors();
		for (String s : theErrors) {
			// System.out.println("ReturnStatus.error: " + s);
		}

		// System.out.println("Done importing from file!");

		// System.out.println("Starting exporting to file");

		OpenOfficeExporter exporter = new OpenOfficeExporter();
		ReturnStatus<String> retSt1 = new ReturnStatus<String>("success");
		String exportFileName = null;
		/* try { */
		exportFileName = exporter.exportToFile(OOO_EXEC_FOLDER,
				"C:/Users/efurman/Downloads", TEST_SESSION_KEY, retSt1);
		/*
		 * } catch (SQLException e) {
		 * System.out.println("ExportToFile SQL exception: " + e.getMessage());
		 * }
		 */
		// System.out.println("Done exporting to file: " + exportFileName);
		// System.out.println("ReturnStatus.status " + retSt1.getStatus());
		theErrors = retSt1.getErrors();
		for (String s : theErrors) {
			// System.out.println("ReturnStatus.error: " + s);
		}

		// System.out.println("Starting second  import");
		retSt = new ReturnStatus<String>("success");
		importer.importFromFile(OOO_EXEC_FOLDER,
				"C:/Users/efurman/Downloads/temp.spare.ods", TEST_SESSION_KEY,
				retSt);
		// System.out.println("ReturnStatus.status " + retSt.getStatus());
		theErrors = retSt.getErrors();
		for (String s : theErrors) {
			// System.out.println("ReturnStatus.error: " + s);
		}
		// System.out.println("Done second importing from file!");

	}

	private static void registerDatabaseResourceManager()
			throws ResourceInitializationException {
		String jdbcURL = "jdbc:mysql://10.100.116.175:3306/StandardsRepository_Dev";
		String jdbcUser = "root";
		String jdbcPassword = "";
		String jdbcDriver = "com.mysql.jdbc.Driver";
		StandaloneConnectionManager connectionManager = new StandaloneConnectionManager(
				jdbcURL, jdbcUser, jdbcPassword, jdbcDriver);

		try {
			ResourceManager.getInstance().addToResources(
					AbstractConnectionManager.class.getName(),
					connectionManager, false);
		} catch (ResourceExistsException exp) {
			// we will never throw the exception here as we set the last
			// parameter to false above.
		}
	}

}
