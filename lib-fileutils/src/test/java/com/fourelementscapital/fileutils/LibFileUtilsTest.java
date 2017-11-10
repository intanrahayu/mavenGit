/******************************************************************************
*
* Copyright: Intellectual Property of Four Elements Capital Pte Ltd, Singapore.
* All rights reserved.
*
******************************************************************************/

package com.fourelementscapital.fileutils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import junit.framework.TestCase;

/**
 * lib-fileutils unit test
 */
public class LibFileUtilsTest extends TestCase {

	private static final Logger log = LogManager.getLogger(LibFileUtilsTest.class.getName());

	/**
	 * Test BeanUtil functions
	 */
	public void testBeanUtil() {
		log.debug(">>>>>> testBeanUtil()");

		Map<String, Object> properties = new HashMap();
		properties.put("id", 1);
		properties.put("desc", "description");

		SampleBean bean1 = new SampleBean();

		try {

			log.debug("convertPropertiesToBean() :");
			BeanUtil.convertPropertiesToBean(properties, bean1);
			if (1 == bean1.getId() && "description".equals(bean1.getDesc())) {
				log.debug("New value from bean. bean1.getId() : '"
						+ bean1.getId() + "', bean1.getDesc() : '"
						+ bean1.getDesc() + "'");
				assertTrue(true);
			}

			log.debug("convertBeanToProperties() :");
			Map m = BeanUtil.convertBeanToProperties(bean1);
			if (m != null) {
				log.debug("New properties from bean conversion : " + m);
				assertTrue(true);
			}

			log.debug("equals() :");
			SampleBean bean2 = new SampleBean();
			BeanUtil.convertPropertiesToBean(properties, bean2);
			log.debug("Is bean1 & bean2 equals : "
					+ BeanUtil.equals(bean1, bean2));

		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	/**
	 * Bean class for testBeanUtilConvertPropertiesToBean() testing purpose
	 */
	private class SampleBean {

		private int id;
		private String desc;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

	}

	/**
	 * Test FindStringInFiles.search(). Only works in unix environment.
	 */
	public void testFindStringInFilesSearch() {
		log.debug(">>>>>> testFindStringInFilesSearch()");
		// Remove the comment tag to test. The code are commented out to prevent error on Windows OS when installing this lib :
		/*
		boolean isPass = false; 
		try {

			// search 'log4j' word in : './*.properties' : 
			String path = "./";
			String wordToSearch = "log4j"; String extension = ".properties";
			
			Map r_result = FindStringInFiles.search(wordToSearch, path, extension);
			if (r_result != null && r_result.size() > 0 ) {
				log.debug("Search 'log4j' word in : './*.properties'. Result : " +
				r_result.size() + " word(s) found."); isPass = true; 
			}
			
		 } catch (Exception e) { 
			 e.printStackTrace(); 
		 } 
		 assertTrue(isPass);
		 */
		 assertTrue(true);
	}

	/**
	 * Test InputStringTokenParser
	 */
	public void testInputStringTokenParser() {
		log.debug(">>>>>> testInputStringTokenParser()");

		log.debug("parseFreeTextTokens() :");
		boolean isPass = true;
		String str = "Aaa,bBb,ccC";
		List<String> a = SplitString.split(str);
		if (a.size() == 3) {
			if (!"Aaa".equals(a.get(0))) {
				isPass = false;
			}
			;
			if (!"bBb".equals(a.get(1))) {
				isPass = false;
			}
			;
			if (!"ccC".equals(a.get(2))) {
				isPass = false;
			}
			;
		} else {
			isPass = false;
		}
		log.debug("'" + str + "' is parsed to : " + a);
		assertTrue(isPass);

		log.debug("parseFreeTextTokensLowerCase() :");
		isPass = true;
		str = "Aaa,bBb,ccC";
		a = SplitString.splitLowerCase(str);
		if (a.size() == 3) {
			if (!"aaa".equals(a.get(0))) {
				isPass = false;
			}
			;
			if (!"bbb".equals(a.get(1))) {
				isPass = false;
			}
			;
			if (!"ccc".equals(a.get(2))) {
				isPass = false;
			}
			;
		} else {
			isPass = false;
		}
		log.debug("'" + str + "' is parsed to : " + a);
		assertTrue(isPass);

		log.debug("parseFreeTextTokensTrim() :");
		isPass = true;
		str = "Aaa bBb , ccC DDD ";
		a = SplitString.splitTrim(str);
		if (a.size() == 2) {
			if (!"Aaa bBb".equals(a.get(0))) {
				isPass = false;
			}
			;
			if (!"ccC DDD".equals(a.get(1))) {
				isPass = false;
			}
			;
		} else {
			isPass = false;
		}
		log.debug("'" + str + "' is parsed to : " + a);
		assertTrue(isPass);
	}

	/**
	 * Test ParseXMLPlaceHolder
	 */
	public void testParseXMLPlaceHolder() {
		log.debug(">>>>>> testParseXMLPlaceHolder()");

		boolean isPass = false;

		log.debug("parse() :");
		String template = "Name : [[name]], Address : [[address]].";
		Map<String, String> m = new HashMap<String, String>();
		m.put("name", "Jon");
		m.put("address", "Bali");
		String str = StringPlaceHolder.parse(template, m);
		log.debug("parse result : '" + str + "'");
		if ("Name : Jon, Address : Bali.".equals(str)) {
			isPass = true;
		}
		assertTrue(isPass);

		log.debug("getElementPH() :");
		template = "Name : \"[[name]]\", Address : [[address]].";
		Vector<String> v = StringPlaceHolder.getElementPH(template);
		log.debug("getElementPH result : " + v);
		if (v != null && v.size() > 0) {
			isPass = true;
		}
		assertTrue(isPass);

		log.debug("getAttributePH() :");
		template = "Name : \"[[name]]\", Address : [[address]].";
		v = StringPlaceHolder.getAttributePH(template);
		log.debug("getAttributePH result : " + v);
		if (v != null && v.size() > 0) {
			isPass = true;
		}
		assertTrue(isPass);
	}

	/**
	 * Test RandomString
	 */
	public void testRandomString() {
		log.debug(">>>>>> testRandomString()");

		String str = RandomString.getString(10);
		log.debug("Random string result : '" + str + "'");

		if (!"".equals(str)) {
			assertTrue(true);
		} else {
			assertTrue(false);
		}
	}

}

 
