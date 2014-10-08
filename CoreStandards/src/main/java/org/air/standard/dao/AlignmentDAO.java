/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.dao;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.air.shared.db.AbstractDAO;
import org.air.shared.db.SQLConnection;
import org.air.shared.utils.StringUtils;
import org.air.standard.common.Alignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* 
 * ALIGNMENT DAO
 * */
public class AlignmentDAO extends AbstractDAO {
  
  
 private static final  Logger _logger = LoggerFactory.getLogger(AlignmentDAO.class);

	/*
	 * GET ALIGNMENT OBJECT WITH GIVEN PARAMETERS
	 */
	private final String CMD_GET_ALIGNMENT = "{call GetAlignment(?, ?) }";

	public Alignment getAlignment(String pubKeyA, String pubKeyB) {
		Alignment alignment = null;
		try (SQLConnection connection = getSQLConnection()) {
			try(CallableStatement callstatement = connection
					.prepareCall(CMD_GET_ALIGNMENT)){
			callstatement.setString(1, pubKeyA);
			callstatement.setString(2, pubKeyB);
			if (callstatement.execute()) {
				ResultSet rs = callstatement.getResultSet();
				while (rs.next()) {
					alignment = new Alignment();
					alignment.set_Key_Publication_A(rs.getString("SubjectKey"));
					alignment.set_Key_Publication_A(rs
							.getString("SubjectLabel"));
				
					rs.getString("SubjectLabel");
				}
			}
			}
		} catch (SQLException e) {
			
		}
		return alignment;
	}

	String getalign() {
		String attach = null;
		return attach;
	}

	/*
	 * MODIFY ALIGNMENT OBJECT WITH GIVEN PARAMETERS
	 */
	public Alignment modifyAlignment(String pubKeyA, String pubKeyB,
			String status, String username) {
		Alignment alignment = null;
		try (SQLConnection connection = getSQLConnection()) {
		  try(CallableStatement callstatement = connection
					.prepareCall("{call ModifyAlignment(?) }")){
			callstatement.setString(1, pubKeyA);
			callstatement.setString(2, pubKeyB);
			callstatement.setString(3, status);
			callstatement.setString(4, username);
			if (callstatement.execute()) {
				ResultSet rs = callstatement.getResultSet();
				getAlignments(rs);
			}
		  }
		} catch (SQLException e) {
			

		}
		return alignment;
	}

	private Alignment getAlignments(ResultSet resultset) throws SQLException {
		Alignment alignment = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
		ArrayList<Alignment> list = new ArrayList<Alignment>();
		try {
			while (resultset.next()) {

				alignment.set_Key_Publication_A(resultset
						.getString("_Key_Publication_A"));
				alignment.set_Key_Publication_B(resultset
						.getString("_Key_Publication_B"));
				alignment.setStatus(resultset.getString("Status"));
				alignment.setModifiedBy(resultset.getString("modifiedBy"));
				try {
					alignment.setModified(dateFormat.parse(resultset
							.getString("Modified")));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				list.add(alignment);
				break;
			}
		} catch (SQLException e) {
		  
		  String message = StringUtils.format("Error while geting Alignment result set in getAlignments method {0}", e.getMessage());
      printStackTraceToLogger(message, e);
		  throw e;
          
		}
		alignment = (list.size() > 0 ? list.get(0) : null);
		return alignment;
	}

}
