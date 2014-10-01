/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.shared.db;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.air.shared.utils.SpringApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractDAO {
	private static final Logger _logger = LoggerFactory.getLogger(AbstractDAO.class);
	
	private DataSource applicationDataSource;
	{
	  applicationDataSource = SpringApplicationContext.getBean ("applicationDataSource",DataSource.class);
	}
	
	public SQLConnection getSQLConnection() throws SQLException
	{
	  
	    return new SQLConnection (applicationDataSource.getConnection ());
	}
	
	protected void printStackTraceToLogger(String message, Exception e)
	{
		_logger.error(message);
		StringWriter strn = new StringWriter();
		PrintWriter prn = new PrintWriter(strn);
		e.printStackTrace(prn);
		prn.close();
		_logger.error(strn.toString());	
	}
}
