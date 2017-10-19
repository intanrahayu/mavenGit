/******************************************************************************
*
* Copyright: Intellectual Property of Four Elements Capital Pte Ltd, Singapore.
* All rights reserved.
*
******************************************************************************/

package com.fourelementscapital.alarm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fourelementscapital.alarm.Email;
import com.fourelementscapital.alarm.EmailVO;
import com.fourelementscapital.alarm.StackAlarm;
import com.fourelementscapital.alarm.StackAlarmVO;
import com.fourelementscapital.alarm.TTS;

/**
 * A com.fourelementscapital Class to process the message stored in IMonitorVO and send it by email, phone, sms or text to speech
 */
public class IMonitor {
	
	private static final Logger log = LogManager.getLogger(IMonitor.class.getName());
	
    public static IMonitorVO processMessage(String message) throws Exception
    {
    	
    	IMonitorVO iMonitorVO = new IMonitorVO();
    	
    	iMonitorVO.setId("" + (new Date().getTime()) + (int)(Math.random() * 10000)); // to display id on log
    	log.debug(iMonitorVO.getId() + " - new");

    	if (message == null) return null; // return null if message = null

    	message = message.replace("<EOF>",""); // remove '<EOF>' if exist 
    	String[] strArr = message.split("~");
    	
    	MultiValueMap multiMap = new MultiValueMap();
    	if (strArr.length > 0) {
    		for (int i=0; i<strArr.length; i++) {
	    		String[] strArrSub = strArr[i].split("\\$\\#\\=");
	    		multiMap.put(strArrSub[0], strArrSub[1]);
    		}
    	}    	
    	
    	Iterator<String> mapIterator = multiMap.keySet().iterator();    	
		while (mapIterator.hasNext()) {
			String key = mapIterator.next();
			
			Collection<String> values = multiMap.getCollection(key);
			
			for(Iterator<String> entryIterator = values.iterator(); entryIterator.hasNext();) {
				String value = entryIterator.next();
				
				switch (key.toLowerCase()) {
					case "theme":
						iMonitorVO.setTheme(Arrays.asList(value.split("\\s*,\\s*")));
						break;
					case "alertlevel":
						iMonitorVO.setAlertLevel(value);
						break;						
					case "subject":
						iMonitorVO.setSubject(value);
						break;						
					case "body":
						iMonitorVO.setBody(value);
						break;						
					case "recipients":
						iMonitorVO.setRecipients(Arrays.asList(value.split("\\s*,\\s*")));
						break;						
					case "emailcc":
						iMonitorVO.setEmailCC(Arrays.asList(value.split("\\s*,\\s*")));
						break;						
					case "emailbcc":
						iMonitorVO.setEmailBCC(Arrays.asList(value.split("\\s*,\\s*")));
						break;						
					case "emailattachments":
						iMonitorVO.setAttachments(Arrays.asList(value.split("\\s*,\\s*")));
						break;						
					case "emailimages":
						iMonitorVO.setImages(Arrays.asList(value.split("\\s*,\\s*")));
						break;						
					case "emailreplyto":
						iMonitorVO.setEmailReplyTo(Arrays.asList(value.split("\\s*,\\s*")));
						break;						
					case "sayit":
						iMonitorVO.setSayIt(Boolean.parseBoolean(value));
						break;						
					case "emailit":
						iMonitorVO.setEmailIt(Boolean.parseBoolean(value));
						break;						
					case "phonecall":
						iMonitorVO.setPhoneIt(Boolean.parseBoolean(value));
						break;						
					case "ym":
						iMonitorVO.setYahooMessenger(Arrays.asList(value.split("\\s*,\\s*")));
						break;	
					case "sms":
						iMonitorVO.setSmsIt(Boolean.parseBoolean(value));
						break;	
					case "escalation":
						iMonitorVO.setEscalation(value);
						break;	
					case "numbers":
						iMonitorVO.setPhoneNumbers(Arrays.asList(value.split("\\s*,\\s*")));
						break;	
				}
			}
		}
		
		// use lib-stackalarm if recipients or numbers is not provided :
		String theme = iMonitorVO.getTheme().get(0); // TODO: get only first theme.
		
		// TODO: alarm level escalation minutes by minutes
		//String alarmLevel = iMonitorVO.getAlertLevel();
		String alarmLevel = (iMonitorVO.getAlertLevel() == null || "".equals(iMonitorVO.getAlertLevel())) ? "HIGH" : iMonitorVO.getAlertLevel();
		
		boolean isGetRecipientsFromDb = false;
		if (iMonitorVO.isEmailIt()) {
			if (iMonitorVO.getRecipients() == null || iMonitorVO.getRecipients().size() == 0) {
				isGetRecipientsFromDb = true;
			}
		}
		if (iMonitorVO.isPhoneIt() || iMonitorVO.isSmsIt()) {
			if (iMonitorVO.getPhoneNumbers() == null || iMonitorVO.getPhoneNumbers().size() == 0) {
				isGetRecipientsFromDb = true;
			}
		}
		
		// TODO : temporary, bypass stack alarm :
		isGetRecipientsFromDb = false;
		
		if (isGetRecipientsFromDb) {
			StackAlarmVO stackAlarmVO = StackAlarm.collectRecipients(theme, alarmLevel);
		
			if (iMonitorVO.isEmailIt()) {
				if (iMonitorVO.getRecipients() == null || iMonitorVO.getRecipients().size() == 0) {
					List<String> iMonitorVORecipients = new ArrayList<String>();
					List<String> stackAlarmVORecipients = stackAlarmVO.getRecipients();
					for (int i=0; i<stackAlarmVORecipients.size(); i++) {
						iMonitorVORecipients.add(stackAlarmVORecipients.get(i));
					}
					iMonitorVO.setRecipients(iMonitorVORecipients);
				}
			}
			if (iMonitorVO.isPhoneIt() || iMonitorVO.isSmsIt()) {
				if (iMonitorVO.getPhoneNumbers() == null || iMonitorVO.getPhoneNumbers().size() == 0) {
					List<String> iMonitorVOPhoneNumbers = new ArrayList<String>();
					List<String> stackAlarmVOPhoneNumbers = stackAlarmVO.getPhoneNumbers();
					for (int i=0; i<stackAlarmVOPhoneNumbers.size(); i++) {
						iMonitorVOPhoneNumbers.add(stackAlarmVOPhoneNumbers.get(i));
					}
					iMonitorVO.setPhoneNumbers(stackAlarmVOPhoneNumbers);
				}
			}
		
		}
		
		return iMonitorVO;
    }
    
	
	/**
	 * Process the alarm message to do email, phone, etc. 
	 * @param stackAlarmVO Stack alarm value object
	 * @throws Exception
	 */	    
    public static void process(IMonitorVO iMonitorVO) throws Exception {
    	
    	
    	List<String> tmpRecipientsList = iMonitorVO.getRecipients();
    	if (tmpRecipientsList != null && tmpRecipientsList.size() > 0) {
	    	for (int i=0; i<tmpRecipientsList.size(); i++) {
	    		log.debug("=== " + iMonitorVO.getId() + " Recipient[" + i + "] : " + tmpRecipientsList.get(i));
	    	}
    	}
    	
    	List<String> tmpPhoneNumberList = iMonitorVO.getPhoneNumbers();
    	if (tmpPhoneNumberList != null && tmpPhoneNumberList.size() > 0) {
	    	for (int i=0; i<tmpPhoneNumberList.size(); i++) {
	    		log.debug("=== " + iMonitorVO.getId() + " PhoneNumber[" + i + "] : " + tmpPhoneNumberList.get(i));
	    	}
    	}
    	
    	Exception exp = null;
        if (iMonitorVO.isSayIt()) {
        	try {
        		processVOToSay(iMonitorVO);
        		log.debug(iMonitorVO.getId() + " - text to speech done");
        	}
    		catch (Exception e) {
            	exp = e;
    		}
        }
    	if (iMonitorVO.isPhoneIt()) {
        	try {
        		processVOToPhone(iMonitorVO);
        		log.debug(iMonitorVO.getId() + " - phone called");
        	}
    		catch (Exception e) {
            	exp = e;
            }
        }
        if (iMonitorVO.isSmsIt()) {
        	try {
        		processVOToSms(iMonitorVO);
        		log.debug(iMonitorVO.getId() + " - sms sent");
        	}
    		catch (Exception e) {
            	exp = e;
    		}
        }        
        if (iMonitorVO.isEmailIt()) {
        	try {
        		processVOToEmail(iMonitorVO);
        		log.debug(iMonitorVO.getId() + " - email sent");
        	}
            catch (Exception e) {
            	exp = e;
            }
        }
        
        if (exp != null) {
        	//exp.printStackTrace(); 
        	//log.error(exp.getMessage());
        	throw exp;
        }
        
        log.debug(iMonitorVO.getId() + " - all done");
        
    }
    
    
	/**
	 * Process the alarm to send email 
	 * @param iMonitorVO IMonitor value object
	 * @throws Exception
	 */	       
    private static void processVOToEmail(IMonitorVO iMonitorVO) throws Exception {

		EmailVO emailVO = new EmailVO();
		
		List<String> themeList = iMonitorVO.getTheme();
		String theme = "ialarm";
		if (themeList != null && themeList.size() > 0) {
			theme = themeList.get(0);
		}
		List<ThemeEmailVO> themeEmailList = ThemeEmail.readFromExcelFile(theme); // TODO : get first theme only ?
		
		String emailFrom = null;
		String emailName = null;
		String emailPassword = null;
		if (themeEmailList.size() > 0) {
			ThemeEmailVO themeEmailVO = (ThemeEmailVO) themeEmailList.get(0);  // get first email only. should be 1 email per theme.
			emailFrom = themeEmailVO.getEmail();
			emailName = themeEmailVO.getName();
			emailPassword = themeEmailVO.getPassword();
		}
		
		//log.debug("==== emailFrom : " + emailFrom);
		//log.debug("==== emailName : " + emailName);
		//log.debug("==== emailPassword : " + emailPassword);

		iMonitorVO.setEmailFrom(emailFrom);
		iMonitorVO.setEmailFromName(emailName);
		iMonitorVO.setEmailPassword(emailPassword);

		emailVO.setFrom(iMonitorVO.getEmailFrom());
		emailVO.setFromName(iMonitorVO.getEmailFromName());
		emailVO.setPassword(iMonitorVO.getEmailPassword());
		
		String subject = iMonitorVO.isPhoneIt() ? "☎Phone: " + iMonitorVO.getSubject() : iMonitorVO.getSubject(); // add phone icon if send a phone call as well.
		emailVO.setSubject(subject);
		emailVO.setBody(iMonitorVO.getBody());

		emailVO.setRecipient(iMonitorVO.getRecipients());
		emailVO.setRecipientCC(iMonitorVO.getEmailCC());
		emailVO.setRecipientBCC(iMonitorVO.getEmailBCC());
		emailVO.setReplyTo(iMonitorVO.getEmailReplyTo());
		
		emailVO.setAttachmentPath(iMonitorVO.getAttachments());
		emailVO.setImages(iMonitorVO.getImages());
		
		Email.send(emailVO);
    }

	/**
	 * Process the alarm to do phone
	 * @param iMonitorVO IMonitor value object
	 * @throws Exception
	 */	      
    private static void processVOToPhone(IMonitorVO iMonitorVO) throws Exception {
    	
    	//String message = "ThisIsJustATest)";
    	String message = URLEncoder.encode(iMonitorVO.getSubject() == null ? "" : iMonitorVO.getSubject(), "UTF-8");    	
    	int maxDuration = 55; // in seconds
    	
    	List<String> phoneNumbersList = iMonitorVO.getPhoneNumbers();
    	
    	if (phoneNumbersList != null && phoneNumbersList.size() > 0) {
	    	for (int i=0; i<phoneNumbersList.size(); i++) {
	    		String phoneNumber = phoneNumbersList.get(i).replaceAll(Pattern.quote("+"), ""); // replace '+' char
	    		phoneNumber = phoneNumber.replaceAll("\\s+",""); // remove all white spaces
	    		String hoiioUrl = "https://secure.hoiio.com/open/ivr/start/dial?dest=%2B" + phoneNumber + "&access_token=ATJEKFoYHAbGYNxm&app_id=26hsfU4rkHvypTFe&msg=" + message + "&caller_id=private&max_duration=" + maxDuration;    		
	            URL phoneCallUrl = new URL(hoiioUrl);
	            URLConnection yc = phoneCallUrl.openConnection();
	            BufferedReader in = new BufferedReader(
	                                    new InputStreamReader(
	                                    yc.getInputStream()));
	            String inputLine;
	
	            boolean isSuccess = false;
	            while ((inputLine = in.readLine()) != null) { 
	                //System.out.println(inputLine);
	    	        if (inputLine.contains("\"status\":\"success_ok\"")) {
	    	        	isSuccess = true;
	    	        	break;
	    	        }
	            }
	            in.close();
	    	}
    	}
    	
    }	
    
    
	/**
	 * Process the alarm to do sms
	 * @param iMonitorVO IMonitor value object
	 * @throws Exception
	 */	      
    private static void processVOToSms(IMonitorVO iMonitorVO) throws Exception {
    	
    	//String message = "ThisIsJustATest%20My%20Message";
    	String message = URLEncoder.encode(iMonitorVO.getSubject() == null ? "" : iMonitorVO.getSubject(), "UTF-8");  
    	
    	List<String> phoneNumbersList = iMonitorVO.getPhoneNumbers();
    	
    	if (phoneNumbersList != null && phoneNumbersList.size() > 0) {
	    	for (int i=0; i<phoneNumbersList.size(); i++) {
	    		String phoneNumber = "65" + phoneNumbersList.get(i).replaceAll("\\s+",""); // remove all white spaces
	    		String hoiioUrl = "https://secure.hoiio.com/open/sms/send?access_token=ATJEKFoYHAbGYNxm&app_id=26hsfU4rkHvypTFe&dest=%2B" + phoneNumber + "&msg=" + message;
	            URL phoneCallUrl = new URL(hoiioUrl);
	            URLConnection yc = phoneCallUrl.openConnection();
	            BufferedReader in = new BufferedReader(
	                                    new InputStreamReader(
	                                    yc.getInputStream()));
	            String inputLine;
	
	            boolean isSuccess = false;
	            while ((inputLine = in.readLine()) != null) { 
	                //System.out.println(inputLine);
	    	        if (inputLine.contains("\"status\":\"success_ok\"")) {
	    	        	isSuccess = true;
	    	        	break;
	    	        }
	            }
	            in.close();
	    	}
    	}
    	
    }	    
    
    
	/**
	 * Process the alarm to do say
	 * @param iMonitorVO IMonitor value object
	 * @throws Exception
	 */	      
    private static void processVOToSay(IMonitorVO iMonitorVO) throws Exception {
    	TTS tts = new TTS();  
    	tts.init(TTS.VOICE_MBROLA_US1);
    	tts.doSpeak(iMonitorVO.getSubject());  
    	//tts.terminate();	// don't terminate, to prevent java.lang.IllegalThreadStateException  
    }

}

 