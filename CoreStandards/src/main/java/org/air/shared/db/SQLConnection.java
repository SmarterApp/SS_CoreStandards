/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.shared.db;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Properties;
import java.util.concurrent.Executor;

public class SQLConnection implements Connection {
	
	private Connection mConnection = null;
	public SQLConnection(Connection conn)
	{
		this.mConnection = conn;
	}
	
	public Statement createStatement() throws SQLException {
		return mConnection.createStatement();
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException{
		return mConnection.prepareStatement(sql);
	}

	public String nativeSQL(String sql) throws SQLException
	{
		return mConnection.nativeSQL(sql);
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException{
		mConnection.setAutoCommit(autoCommit);
	}

	public boolean getAutoCommit() throws SQLException{
		return mConnection.getAutoCommit();
	}

	public void commit() throws SQLException{
		mConnection.commit();
	}

	public void rollback() throws SQLException
	{
		mConnection.rollback();
	}

	public void close() throws SQLException{
		//todo: SB figure this out.
		mConnection.close();
	}

	public boolean isClosed() throws SQLException{
		return mConnection.isClosed();
	}

	public DatabaseMetaData getMetaData() throws SQLException{
		return mConnection.getMetaData();
	}

	public void setReadOnly(boolean readOnly) throws SQLException{
		mConnection.setReadOnly(readOnly);
	}

	public boolean isReadOnly() throws SQLException{
		return mConnection.isReadOnly();
	}

	public void setCatalog(String catalog) throws SQLException{
		mConnection.setCatalog(catalog);
	}

	public String getCatalog() throws SQLException{
		return mConnection.getCatalog();
	}

	public void setTransactionIsolation(int level) throws SQLException
	{
		mConnection.setTransactionIsolation(level);
	}

	public int getTransactionIsolation() throws SQLException{
		return mConnection.getTransactionIsolation();
	}

	public SQLWarning getWarnings() throws SQLException{
		return mConnection.getWarnings();
	}

	public void clearWarnings() throws SQLException{
		mConnection.clearWarnings();
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException
			{
				return mConnection.createStatement(resultSetType, resultSetConcurrency);
			}
	
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException
			{
				return mConnection.prepareStatement(sql,resultSetConcurrency);
			}

	public java.util.Map<String, Class<?>> getTypeMap() throws SQLException
	{
		return mConnection.getTypeMap();
	}

	public void setTypeMap(java.util.Map<String, Class<?>> map) throws SQLException
	{
		mConnection.setTypeMap(map);
	}

	public void setHoldability(int holdability) throws SQLException
	{
		mConnection.setHoldability(holdability);
	}

	public int getHoldability() throws SQLException
	{
		return mConnection.getHoldability();
	}

	public Savepoint setSavepoint() throws SQLException
	{
		return mConnection.setSavepoint();
	}

	public Savepoint setSavepoint(String name) throws SQLException
	{
		return mConnection.setSavepoint(name);
	}

	public void rollback(Savepoint savepoint) throws SQLException
	{
		mConnection.rollback(savepoint);
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException
	{
		mConnection.releaseSavepoint(savepoint);
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException
			{
		return mConnection.createStatement(resultSetType, resultSetConcurrency);
			}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException{
		return mConnection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}
	
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException{
		return mConnection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException{
		return mConnection.prepareStatement(sql, autoGeneratedKeys);
	}

	public PreparedStatement prepareStatement(String sql, int columnIndexes[])
			throws SQLException{
		return mConnection.prepareStatement(sql, columnIndexes);
	}

	public PreparedStatement prepareStatement(String sql, String columnNames[])
			throws SQLException{
		return mConnection.prepareStatement(sql, columnNames);
	}

	public Clob createClob() throws SQLException
	{
		return mConnection.createClob();
	}

	public Blob createBlob() throws SQLException{
		return mConnection.createBlob();
	}

	public NClob createNClob() throws SQLException
	{
		return mConnection.createNClob();
	}

	public SQLXML createSQLXML() throws SQLException
	{
		return mConnection.createSQLXML();
	}

	public boolean isValid(int timeout) throws SQLException
	{
		return mConnection.isValid(timeout);
	}

	public void setClientInfo(String name, String value) throws SQLClientInfoException
	{
		mConnection.setClientInfo(name, value);
	}

	public void setClientInfo(Properties properties) throws SQLClientInfoException
	{
		mConnection.setClientInfo(properties);
	}

	public String getClientInfo(String name) throws SQLException
	{
		return mConnection.getClientInfo(name);
	}

	public Properties getClientInfo() throws SQLException{
		return mConnection.getClientInfo();
	}

	public Array createArrayOf(String typeName, Object[] elements) throws SQLException
	{
		return mConnection.createArrayOf(typeName, elements);
	}

	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException{
		return mConnection.createStruct(typeName, attributes);
	}

	public void setSchema(String schema) throws SQLException{
		mConnection.setSchema(schema);
	}

	public String getSchema() throws SQLException
	{
		return mConnection.getSchema();
	}

	public void abort(Executor executor) throws SQLException
	{
		mConnection.abort(executor);
	}

	public void setNetworkTimeout(Executor executor, int milliseconds)
			throws SQLException{
		mConnection.setNetworkTimeout(executor, milliseconds);
	}

	public int getNetworkTimeout() throws SQLException
	{
		return mConnection.getNetworkTimeout();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return mConnection.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return mConnection.isWrapperFor(iface);
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		return mConnection.prepareCall(sql);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return mConnection.prepareCall(sql, resultSetType, resultSetConcurrency);
	}
	
	@Override
	protected void finalize() throws Throwable {
		this.close();
		super.finalize();
	}
}
