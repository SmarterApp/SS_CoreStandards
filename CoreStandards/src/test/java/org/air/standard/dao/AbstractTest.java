/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.dao;

import java.io.*;
import java.sql.SQLException;
import java.util.Properties;

import org.air.shared.db.AbstractConnectionManager;
import org.air.shared.db.standalone.StandaloneConnectionManager;
import org.air.shared.resources.*;
import org.air.standard.controller.ContextBeans;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AbstractTest {

	private Properties mProperties = new Properties();

	public void setUp() throws Exception {

		//initialize the bean factory
	    ContextBeans.set(new ClassPathXmlApplicationContext(new String[] {"applicationContext-security-test.xml"}));
		
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("run.properties");
		mProperties.load(in);
		in.close();
		
		registerDatabaseResourceManager();	
	}

	public void tearDown() throws Exception {

	}

	public <T> T getPropetyValue(String key) {
		if (mProperties.containsKey(key))
			return (T) mProperties.get(key);
		else
			return null;
	}

	private void registerDatabaseResourceManager()
			throws ResourceInitializationException {
		String jdbcURL = this.<String> getPropetyValue("jdbc.url");
		String jdbcUser = this.<String> getPropetyValue("jdbc.userName");
		String jdbcPassword = this.<String> getPropetyValue("jdbc.password");
		String jdbcDriver = this.<String> getPropetyValue("jdbc.driver");
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
