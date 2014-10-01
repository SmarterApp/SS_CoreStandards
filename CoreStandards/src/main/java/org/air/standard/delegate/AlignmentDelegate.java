/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.delegate;

import org.air.standard.common.Alignment;
import org.air.standard.dao.AlignmentDAO;

public class AlignmentDelegate {
	
	static Alignment alignment;
    static AlignmentDAO alignmentDAL = new AlignmentDAO();
    
	 public static Alignment GetAlignment(String pubKeyA, String pubKeyB)
     {         
         alignment = alignmentDAL.getAlignment(pubKeyA, pubKeyB);
         return alignment;
     }
	 
	 public static Alignment ModifyAlignment(String pubKeyA, String pubKeyB, String status, String username)
     {        
         alignment = alignmentDAL.modifyAlignment(pubKeyA, pubKeyB, status, username);
         return alignment;
     }
}
