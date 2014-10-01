/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.shared.db.standalone;

import java.sql.*;

import org.air.shared.db.AbstractConnectionManager;
import org.air.shared.db.SQLConnection;
import org.air.shared.resources.ResourceInitializationException;
import org.air.shared.utils.StringUtils;

public class StandaloneConnectionManager extends AbstractConnectionManager{
	private String mJdbcURL = null;
	private String mJdbcUser = null;
	private String mJdbcPassword = null;
	private String mJdbcDriver = null;
	
	public StandaloneConnectionManager(String jdbcURL, String jdbcUser, String jdbcPassword, String jdbcDriver)
	{
		this.mJdbcPassword = jdbcPassword;
		this.mJdbcURL = jdbcURL;
		this.mJdbcUser = jdbcUser;
		this.mJdbcDriver = jdbcDriver;
	}
	
	public SQLConnection getConnection() throws SQLException
	{
		Connection connection = DriverManager.getConnection(mJdbcURL, mJdbcUser, mJdbcPassword);
		SQLConnection sqlConnection = new SQLConnection(connection);
		return sqlConnection;
	}
	
	public void closeConnection(SQLConnection conn) throws SQLException
	{
		if (!conn.isClosed())
			conn.close();
	}
	
	public void load() throws ResourceInitializationException{
		try{
			Class.forName(mJdbcDriver);
		}
		catch (ClassNotFoundException exp)
		{
			throw new ResourceInitializationException(StringUtils.format("Class {0} not found.", mJdbcDriver), exp);
		}
	}
	
	public void unload()
	{
		//nothing to do in this case.
	}
	
	public void finalize()
	{
		unload();
	}
}
