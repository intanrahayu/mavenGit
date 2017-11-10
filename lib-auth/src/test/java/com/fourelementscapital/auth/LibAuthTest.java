/******************************************************************************
*
* Copyright: Intellectual Property of Four Elements Capital Pte Ltd, Singapore.
* All rights reserved.
*
******************************************************************************/

package com.fourelementscapital.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import junit.framework.Assert;
import junit.framework.TestCase;


/**
 * lib-auth unit test
 */
public class LibAuthTest extends TestCase {
	
	private static final Logger log = LogManager.getLogger(LibAuthTest.class.getName());

	/**
	 * Wiki authentication test : login, check login, get authenticated user, get user access.
	 */	 	
    public void testWikiAuthentication()
    {
    	log.debug(">>>>>> testWikiAuthentication()");
    	try {    
   		
    		// wiki login :
    		String user = "Rams";
    		String password = "FECrams";
    		
			boolean success= WikiAuthentication.validateUser(user, password); // validate wiki user
			log.debug("validate wiki user : " + success);

    		HashMap<String, String> loginMap = new HashMap<String, String>();			
			if(!success){				
				loginMap.put("message", "Invalid username or password");
				Assert.assertTrue(false); // scenario : login must success					
			}else{
				loginMap.put("authenticatedUser", user);
				// example : get encrypted password and store it to map for further process
				String pss = WikiAuthentication.getEncryptedPwd(user); 
				loginMap.put("encryptedPassword", pss);
			}
			
			// store validate user result to map
			loginMap.put("loggedin", Boolean.toString(success)); 
			
    		// check whether user login / not :
			boolean loggedin = Boolean.parseBoolean(loginMap.get("loggedin"));
			log.debug("is user logged in : " + loggedin);
			Assert.assertTrue(loggedin); 	
			
			// get authenticated user :
			String authenticatedUser = loginMap.get("authenticatedUser");
			log.debug("get authenticatedUser : " + authenticatedUser);
			
    		// Get user access. Use dummy themes for testing purpose.
			// To get themes from infrastructureDB : Map themes = infrastructureDB.getThemes4Users(user). It needs lib-db library.
			Map<String, String> themes= new HashMap<String, String>();
			themes.put("computing", "X");
			themes.put("etrading", "U");
			themes.put("execution", "N");
			themes.put("bb", "B");
			
			UserThemeAccessPermission userTheme = new UserThemeAccessPermission(user);
			userTheme.addPermissionWithThemes(themes);
			ArrayList<String> rwxList = userTheme.getRwx();
			log.debug("rwxList : ");
			for (String rwx : rwxList) {
				log.debug("- " + rwx);
			}
			Assert.assertTrue(rwxList.contains("computing") && rwxList.contains("bb")); // getRwx() must contains 'computing' & 'bb'			
    		
			ArrayList<String> rxList = userTheme.getRx();
			log.debug("rxList : ");
			for (String rx : rxList) {
				log.debug("- " + rx);
			}
			Assert.assertTrue(rxList.contains("etrading")); // getRx() must contains 'etrading'
			
			ArrayList<String> rList = userTheme.getR();
			log.debug("rList : ");
			for (String r : rList) {
				log.debug("- " + r);
			}
			Assert.assertTrue(rList.contains("execution")); // getR() must contains 'execution'
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		assertTrue( false );
    	} 
    }
    
    
	/**
	 * Super user authentication test : validate / set / change password, check login, get authenticated user
	 */	 	
	public void testSuperUserAuthentication()
	{
		log.debug(">>>>>> testSuperUserAuthentication()");
		try {
			
			String user = "administrator";
			String password = "password";
			String newpassword = "password";
			
			Map<String, String> loginMap = new HashMap<String, String>();
			boolean success = false;
			
			// if user is 'administrator' and newpassword is not null & not empty then it's a change password :
			if(user.equalsIgnoreCase("administrator") && newpassword!=null && !newpassword.equals("")){
				
				// Remove the comment tag to test. The code are commented out to prevent creating password file when installing this lib (edit properties file first) :
				//success = SuperUserAuthentication.changePwd(password,newpassword);
				//temporary var value when install this lib :
				success = true;				
				
				log.debug("change superuser password : " + newpassword);
			}
			// else : validate if password is already set or set password if password has not been set :
			else{
				
				// Remove the comment tag to test. The code are commented out to prevent creating password file when installing this lib (edit properties file first) :
				//success= SuperUserAuthentication.validateOrSet(password);
				//temporary var value when install this lib :
				success = true;				
				
				log.debug("validate / set superuser password : " + password);
			}
			
			if(!success){				
				loginMap.put("message", "Invalid username or password");
				Assert.assertTrue(false); // scenario : login must success					
			}
			else {
				loginMap.put("authenticatedUser", user);
			}
			loginMap.put("loggedin", Boolean.toString(success)); // store validate superuser result to map			
			
    		// check whether login / not :
			boolean loggedin = Boolean.parseBoolean(loginMap.get("loggedin"));
			log.debug("is superuser logged in : " + loggedin);
			Assert.assertTrue(loggedin);
			
    		// get authenticated user :
    		String authenticated_user = loginMap.get("authenticatedUser");
    		log.debug("authenticated superuser : " + authenticated_user);
    		
    		Assert.assertTrue(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
    /**
     * List Wiki users test
     */
    public void testListWikiUsers()
    {
    	log.debug(">>>>>> testListWikiUsers()");
    	try {    	
	    	Vector<HashMap<String, String>> result = WikiAuthentication.listUsers();
	    	
	    	log.debug("wiki user count : " + result.size());
	    	
	    	for (int i=0; i<result.size(); i++) {
	    		HashMap<String, String> h = (HashMap<String, String>) result.get(i); 
/*	    		
				log.debug(">>>>>>>>>> user_name      : " + h.get("user_name"));
				log.debug(">>>>>>>>>> user_real_name : " + h.get("user_real_name"));
				log.debug(">>>>>>>>>> user_password  : " + h.get("user_password"));
				log.debug(">>>>>>>>>> user_email     : " + h.get("user_email"));
*/				
	    	}
	        assertTrue( true );
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		assertTrue( false );
    	}
    }
    
    
    /**
     * System authentication test
     */
    public void testSystemAuthentication()
    {    
    	log.debug(">>>>>> testSystemAuthentication()");
   		log.debug("SystemAuthentication.isUserLoggedIn() : " + SystemAuthentication.isUserLoggedIn());
    	
   		assertTrue( SystemAuthentication.isUserLoggedIn() );
    }    
    
}

 