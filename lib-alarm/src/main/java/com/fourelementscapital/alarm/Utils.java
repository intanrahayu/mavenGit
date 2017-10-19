/******************************************************************************
*
* Copyright: Intellectual Property of Four Elements Capital Pte Ltd, Singapore.
* All rights reserved.
*
******************************************************************************/

package com.fourelementscapital.alarm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

/**
 * This class contains Static Utility functions
 */
class Utils {
	
	/**
	 * Accepts an ArrayList of strings and then returns a delimiter separated string
	 * @param al The arraylist of strings
	 * @param delimiter The delimiter
	 * @return The delimiter separated string
	 */
	protected static String Join(ArrayList<String> al,String delimiter)
	{
		return al.toString().replaceAll("\\[|\\]", "").replaceAll(", ",delimiter);
	}	

	/**
	 * Get the value of a property defined in config file
	 * @param propertyName - the name or key of the property you want to retrieve
	 * @return the string value 
	 * @throws IOException
	 */
	protected static String GetConfigValue(String propertyName) throws IOException
	{
	 	Properties prop = new Properties();
			String propFileName = "config_stackalarm.properties";

			InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream(propFileName);
			
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

			Date time = new Date(System.currentTimeMillis());

			// get the property value and print it out
			String value = prop.getProperty(propertyName);
			return value;
	}
	
	
	/**
	 * Capitalize first char of a word.
	 * Example : 'this is text. another text' will be converted to 'This Is Text. Another Text'
	 * @param string
	 * @return string
	 */		
	protected static String capitalizeString(String string) {
		char[] chars = string.toLowerCase().toCharArray();
		boolean found = false;
		for (int i = 0; i < chars.length; i++) {
			if (!found && Character.isLetter(chars[i])) {
				chars[i] = Character.toUpperCase(chars[i]);
				found = true;
			} else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
				found = false;
			}
		}
		return String.valueOf(chars);
	}		
	
}


 