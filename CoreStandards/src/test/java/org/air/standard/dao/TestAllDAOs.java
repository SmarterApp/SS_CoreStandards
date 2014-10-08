/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.dao;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	    AlignmentDAOTest.class,
	    AuthorizationDAOTest.class,
	    LoaderTablesDAOTest.class,
	    PublicationDAOTest.class,
	    PublisherDAOTest.class,
	    RelationshipDAOTest.class,
	    StandardDAOTest.class,
	    SubjectDAOTest.class,
})
public class TestAllDAOs {	

}
