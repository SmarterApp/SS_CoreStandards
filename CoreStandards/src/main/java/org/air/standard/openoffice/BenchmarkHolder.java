/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.openoffice;

public class BenchmarkHolder {
	private String benchmark;
	private String grade;

	public BenchmarkHolder(String benchmark, String grade) {
		this.benchmark = benchmark;
		this.grade = grade;
	}

	public String getBenchmark() {
		return benchmark;
	}

	public String getGrade() {
		return grade;
	}
}
