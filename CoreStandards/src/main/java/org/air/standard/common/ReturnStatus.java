/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.common;

import java.util.ArrayList;
import java.util.List;

public class ReturnStatus<T> {
	private String status = null;
	// a holder for all error messages that were encountered during this request
	// processing.
	private List<String> errors = new ArrayList<String>();
	// a holder for all warning messages that were encountered during this
	// request processing.
	private List<String> warnings = new ArrayList<String>();
	// a holder for all validations that passed during this request processing.
	private List<String> validationsPassed = new ArrayList<String>();

	private T payload = null;

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}

	public void appendToErrors(List<String> errors) {
		if (errors != null && errors.size() > 0)
			errors.addAll(errors);
	}

	public void appendToErrors(String anError) {
		if (anError != null) {
			errors.add(anError);
		}
	}

	public List<String> getErrors() {
		return this.errors;
	}

	public void appendToWarnings(List<String> warnings) {
		if (warnings != null && warnings.size() > 0)
			warnings.addAll(warnings);
	}

	public List<String> getWarnings() {
		return this.warnings;
	}

	public void appendToValidationsPassed(List<String> validations) {
		if (validations != null && validations.size() > 0)
			this.validationsPassed.addAll(validations);
	}

	public List<String> getValidationsPassed() {
		return this.validationsPassed;
	}

	public ReturnStatus(String status) {
		this.status = status;
	}

	public void setPayload(T payload) {
		this.payload = payload;
	}

	public T getPayload() {
		return this.payload;
	}
}
