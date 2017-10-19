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
import java.util.Properties;

/**
 * A class to read config file
 */
class Config {
	
	/**
	 * Get the value of a property defined in config file
	 * @param propertyName - the name or key of the property you want to retrieve
	 * @return the string value 
	 * @throws IOException
	 */
	protected static String getConfigValue_email(String propertyName) throws IOException
	{
		
	 	Properties prop = new Properties();
		String propFileName = "config_email.properties";
		
		InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(propFileName);
		
		if (inputStream != null) {
			
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}

		// get the property value and print it out
		String value = prop.getProperty(propertyName);
		
		return value;
	}	

	protected static String getConfigValue_tts(String propertyName) throws IOException
	{
		
	 	Properties prop = new Properties();
		String propFileName = "config_tts.properties";
		
		InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(propFileName);
		
		if (inputStream != null) {
			
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}

		// get the property value and print it out
		String value = prop.getProperty(propertyName);
		
		return value;
	}	

	protected static String getConfigValue_imonitor(String propertyName) throws IOException
	{
		
	 	Properties prop = new Properties();
		String propFileName = "config_imonitor.properties";
		
		InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(propFileName);
		
		if (inputStream != null) {
			
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}

		// get the property value and print it out
		String value = prop.getProperty(propertyName);
		
		return value;
	}	

	protected static String getConfigValue_socket(String propertyName) throws IOException
	{
		
	 	Properties prop = new Properties();
		String propFileName = "config_socket.properties";
		
		InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(propFileName);
		
		if (inputStream != null) {
			
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}

		// get the property value and print it out
		String value = prop.getProperty(propertyName);
		
		return value;
	}		

}

 
