/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.common;

import java.util.Date;

public class User {
	private int _key;
	private String username;
	private String fullname;
	private Date LoggedIn;

	public int get_key() {
		return _key;
	}

	public void set_key(int _key) {
		this._key = _key;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Date getLoggedIn() {
		return LoggedIn;
	}

	public void setLoggedIn(Date loggedIn) {
		LoggedIn = loggedIn;
	}
}
