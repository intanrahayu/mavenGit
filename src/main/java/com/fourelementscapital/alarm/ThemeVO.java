/******************************************************************************
*
* Copyright: Intellectual Property of Four Elements Capital Pte Ltd, Singapore.
* All rights reserved.
*
******************************************************************************/ 

package com.fourelementscapital.alarm;

import java.io.Serializable;

/**
 * Theme Value Object to store theme name used by Alarm
 */
public class ThemeVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Theme name
	 */
	private String name;
	
	/**
	 * Constructor with name parameter
	 */	
	public ThemeVO(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}


