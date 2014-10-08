/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.exceptions;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoException extends Exception {

	private String mStatus = null;
	private String mErrorMessage = null;

	public DaoException(String status, String errorMessage) {
		super();
		if (status == null)
			status = "";
		this.mStatus = status;
		this.mErrorMessage = errorMessage;
	}

	/**
	 * @return message
	 */

	public String getMessage() {
		return mErrorMessage;
	}

	/**
	 * @return status
	 */
	public String getStatus() {
		return mStatus;
	}

	public boolean isError() {
		if ("".equals(this.mStatus) || "success".equalsIgnoreCase(this.mStatus))
			return false;
		return true;
	}

	/**
	 * This method process resultset to find error resultset or success data
	 * resultset from database. If the resultset contains status, reason columns
	 * then throws exception considering errors present, if not flow continues
	 * normally
	 * 
	 * @param resultset
	 * @throws SQLException 
	 */
	public static DaoException getInstanceIfAvailable(ResultSet rs)
			throws DaoException, SQLException {
		DaoException exception = null;
		try {

			/*
			 * Check status,reason columns exists first with out iterating resultset. if the
			 * status column exists in resultset, meaning we received error from
			 * DB, so throw DAOException. If not this method simply returns
			 * null.
			 */

			rs.findColumn("status");
			rs.findColumn("reason");
			while (rs.next()) {
				String status = rs.getString("status");
				String reason = rs.getString("reason");
				exception = new DaoException(status, reason);
				if (exception.isError())
					throw exception;
			}
		} catch (SQLException exp) {
		 
		    return exception;
		  
		}
		return exception;
	}
	
	 public static DaoException checkForError(ResultSet rs)
	      throws DaoException, SQLException {
	    DaoException exception = null;
	    try {

	      /*
	       * Check status,reason columns exists first with out iterating resultset. if the
	       * status column exists in resultset, meaning we received error from
	       * DB, so throw DAOException. If not this method simply returns
	       * null.
	       */

	      rs.findColumn("status");
	      rs.findColumn("reason");
	      while (rs.next()) {
	        String status = rs.getString("status");
	        String reason = rs.getString("reason");
	        exception = new DaoException(status, reason);
	        if (exception.isError())
	          throw exception;
	      }
	    } catch (SQLException exp) {
	      int rowCount=0;
	      while(rs.next ()){
	        rowCount++;
	      }
	      rs.beforeFirst();
	      if(rowCount == 0){
	      exception = new DaoException("", "");
	      throw exception;
	      }
	      else{
	        return exception;
	      }
	    }
	    return exception;
	  }

}
