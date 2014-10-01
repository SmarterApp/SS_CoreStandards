/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.shared.db.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.air.shared.db.AbstractConnectionManager;
import org.air.shared.db.SQLConnection;
import org.air.shared.resources.ResourceInitializationException;
import org.air.shared.utils.StringUtils;

/*
 * based on tutorial from here: http://www.mulesoft.com/tomcat-mysql
 */
public class DataSourceConnectionManager extends AbstractConnectionManager {
	
	private DataSource mDataSource = null;
	
	public DataSourceConnectionManager (DataSource dataSource)
	{
		this.mDataSource = dataSource;
	}
	
	public SQLConnection getConnection() throws SQLException
	{
		Connection connection = mDataSource.getConnection();
		SQLConnection sqlConnection = new SQLConnection(connection);
		return sqlConnection;
	}
	
	public void closeConnection(SQLConnection conn) throws SQLException
	{
		if (!conn.isClosed())
			conn.close();
	}
	
	public void load() throws ResourceInitializationException{
		/*
		 * if it is a datasource we already have loaded up driver etc. nothing to do here.
		 */
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
