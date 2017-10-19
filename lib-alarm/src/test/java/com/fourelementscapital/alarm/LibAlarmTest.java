/******************************************************************************
*
* Copyright: Intellectual Property of Four Elements Capital Pte Ltd, Singapore.
* All rights reserved.
*
******************************************************************************/

package com.fourelementscapital.alarm;

import java.util.ArrayList;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * lib-alarm unit test
 */

public class LibAlarmTest extends TestCase {


public LibAlarmTest( String testName )
    {
        super( testName );
    }
    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( LibAlarmTest.class );
    }

    /**
     * Empty Test
     */
    public void testApp()
    {
        assertTrue( true );
    }


	/**
	 * Test send alarm
	 */	 	
    public void testSendAlarm()
    {
    	
		try{    	

	    	ArrayList<ThemeVO> themes=new ArrayList<ThemeVO>();
	    	themes.add(new ThemeVO("computing"));
	    	themes.add(new ThemeVO("etrading"));
	    	
	    	// Remove the comment tag to test. The code are commented out to prevent sending alarm when installing this lib :
		
			// send alarm (default) :
			//Alarm.sendAlarm( themes, AlarmType.EMAIL, "Test subject", "Test message", false, true, false, null, null);

			// send alarm with list of theme & a message :			
			//Alarm.sendAlarm( themes, "Test message 2");

			// send alarm with a theme & a message :			
			//Alarm.sendAlarm( "computing", "Test message 3");
			
			assertTrue(true);
		}catch(Exception e){
			e.printStackTrace();
			assertTrue(false);			
		}   	

    }


    /**
     * This is to show how to send message to socket server. 
     * The code is commented out because it will throw error if server is not running. 
     */ 

    public void testSocketClientSendMessage()
    {
        try {
            // Remove the comment tag to test. The code are commented out to prevent sending message to socket server (set socket server & edit properties file first) :
            //String message = "theme$#=itools~theme$#=computing~subject$#=test~body$#=-iMonitor~recipients$#=Manas@4ecap.com~sayIt$#=FALSE~emailIt$#=TRUE~phoneCall$#=FALSE~ym$#=FALSE~sms$#=FALSE<EOF>";
            //SocketClient.sendMessage(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue( true );
    } 


    public void testSocketServerListen()
    {
        try {
            // To test, provide socketProcess object first :
            // SocketProcess socketProcess = ...;
            //SocketServer.listen(socketProcess);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue( true );
    }


    public void testParseMessage()
    {
        try {
            // Remove the comment tag to test. The code are commented out to prevent getting data from database (edit properties file first) :
            // StackAlarmVO stackAlarmVO = StackAlarm.collectRecipients("computing", "HIGH");
            assertTrue( true );
        }
        catch (Exception e) {
            e.printStackTrace();
            assertTrue( false );
        }
        
    }  

    
    public void testProcessMessage()
    {    
		StringBuilder sb = new StringBuilder();
		sb.append("theme$#=computing,java");
		sb.append("~subject$#=This is just a test");
		sb.append("~body$#=This is {{{IMG}}} the email body");
		sb.append("~recipients$#=ari@4ecap.com");
		sb.append("~sayIt$#=true");
		sb.append("~emailIt$#=false");
		sb.append("~phoneCall$#=false");
		sb.append("~sms$#=FALSE");
		sb.append("~ym$#=FALSE");
		sb.append("~numbers$#=+65 8297 1508");
		sb.append("~alarmLevel$#=LOW");
		sb.append("<EOF>");
		
		String message = sb.toString();
		
		try {
			IMonitorVO iMonitorVO = IMonitor.processMessage(message);
			if (iMonitorVO.isSayIt()) {
				assertTrue(true);
			}
			else {
				assertTrue(false);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
    }

    
    
    /**
     * Test Process(). Do email / phone / text to speech according to ImonitorVO value.
     */
    public void testProcess()
    {    
    	// Remove the comment tag to test. The code are commented out to prevent phone calling to +6285866115005 (Ari's phone) :
    	/*
    	try {
	    	IMonitorVO iMonitorVO = new IMonitorVO();
	    	List<String> phonenumbersList = new ArrayList<String>();
	    	phonenumbersList.add("+6285866115005");
	    	iMonitorVO.setPhoneNumbers(phonenumbersList);
	    	iMonitorVO.setPhoneIt(true);
	    	iMonitorVO.setSubject("This is just a test");
	    	IMonitor.process(iMonitorVO);
	    	assertTrue(true);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		assertTrue(false);
    	}
    	*/
    	assertTrue(true);
    }

    public void testSendEmail()
    {
        try {
            
            EmailVO vo = new EmailVO();
            
            vo.setFrom("ari@4ecap.com");
            vo.setFromName("Intan 4ecap");
            vo.setPassword("password");
            
            vo.setSubject("Testing Subject");
            //vo.setBody("<strong>Dear GMail</strong>,<br/>{{{IMG}}}<br/> This is just a test.");
            vo.setBody("<strong>Dear GMail</strong>,<br/> This is just a test.");

            ArrayList<String> recipientList = new ArrayList<String>();
            recipientList.add("intan@kronosinvestments.net");
            vo.setRecipient(recipientList);
                        
            ArrayList<String> recipientCCList = new ArrayList<String>();
            recipientCCList.add("cc@gmail.com");
            recipientCCList.add("dd@gmail.com");
            vo.setRecipientCC(recipientCCList);
            
            ArrayList<String> recipientBCCList = new ArrayList<String>();
            recipientBCCList.add("bcc@gmail.com");
            recipientBCCList.add("bcc2@gmail.com");
            vo.setRecipientBCC(recipientBCCList);

            ArrayList<String> replyToAL = new ArrayList<String>();
            replyToAL.add("replyto@gmail.com");
            replyToAL.add("reply@yahoo.com");
            vo.setReplyTo(replyToAL);           
        
            ArrayList<String> attachmentList = new ArrayList<String>();
            attachmentList.add("G:\\_tmp\\test_email_attachments\\1.txt");
            attachmentList.add("G:\\_tmp\\test_email_attachments\\2.txt");
            vo.setAttachmentPath(attachmentList);
                    
            ArrayList<String> imagesList = new ArrayList<String>();
            imagesList.add("C:\\Users\\Public\\Pictures\\Sample Pictures\\Lighthouse.jpg");
            imagesList.add("C:\\Users\\Public\\Pictures\\Sample Pictures\\Hydrangeas.jpg");
            vo.setImages(imagesList);
            
            // remove the comment tag to test. The code are commented out to prevent sending email when installing this lib :
            //Email.send(vo);
            
            assertTrue( true );     
        }
        catch (Exception e) {
            e.printStackTrace();
            assertTrue( false );        
        }
        
    }       
    


    public void testTTS()
    {    
        try {
            // Remove the comment tag to test. The code are commented out to prevent executing text to speech (setup MBROLA & edit properties file first) :
            /*
            TTS tts = new TTS();  
            tts.init(TTS.VOICE_MBROLA_US1);
            tts.doSpeak("This is just a test");  
            //tts.terminate();  // don't terminate, to prevent java.lang.IllegalThreadStateException
            */
        }
        catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        assertTrue( true );
    }   
    
}


 
