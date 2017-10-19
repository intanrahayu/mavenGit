/******************************************************************************
*
* Copyright: Intellectual Property of Four Elements Capital Pte Ltd, Singapore.
* All rights reserved. lala
*
******************************************************************************/

package com.fourelementscapital.alarm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Send alarm to iMonitor API
 */
public class Alarm {
	
	private static final Logger log = LogManager.getLogger(Alarm.class.getName());

	/**
	 * Private constructor
	 */	
	private Alarm(){}
	
	
	/**
	 * Construct alarm message with default parameters and send it to iMonitor API
	 * @param themes ThemeVO array list
	 * @param alarm_type Alarm type
	 * @param subject Subject
	 * @param message Message
	 * @param say Is Say
	 * @param email Is Email
	 * @param phone Is Phone
	 * @param attachmentfile Attachment file
	 * @param exc_add Exclude Address
	 * @throws IOException
	 */
	public static void sendAlarm(ArrayList<ThemeVO> themes, AlarmType alarm_type,String subject,String message, boolean say, boolean email, boolean phone, String attachmentfile,String exc_add) throws IOException {
		
		if(themes!=null && themes.size()>0 && message!=null && alarm_type!=null){
			
			// ignore alarm ? :
			String c_noalarm=getConfigValue("ignore.alarm");				
			if(c_noalarm!=null && c_noalarm.equalsIgnoreCase("true")) {
				//do nothing
			}else{

				//String xml=constructXMLWithFile(themes,subject,message,say,email,phone,attachmentfile,exc_add);
				String socketMessage=constructSocketMessageWithFile(themes,subject,message,say,email,phone,attachmentfile,exc_add);
				//log.debug("Sending alarm:xml:"+xml);
				//log.debug("Sending alarm:socketMessage:"+socketMessage);
					
			    // check whether dump XML to file or send to iMonitor API :
			    
				boolean isDump = Boolean.parseBoolean(getConfigValue("alarm.dump"));

				if (isDump) {
					// create folder if not exist. format : 'yyyyMMdd' (today's date)
					String dumpFolder = getConfigValue("alarm.dump.folder");
					SimpleDateFormat sdfDumpFolder = new SimpleDateFormat("yyyyMMdd");
					dumpFolder = dumpFolder + sdfDumpFolder.format(new Date()) + "/"; // setup today folder
					File dir = new File(dumpFolder);
					dir.mkdirs();
					
					// write dump file. format : 'HHmmss.xml' (HHmmss : current time)
					SimpleDateFormat sdfFile = new SimpleDateFormat("HHmmssSSS");
					PrintWriter writer = new PrintWriter(dumpFolder + sdfFile.format(new Date()) + ".xml", "UTF-8");
					//writer.println(xml);
					//writer.println(socketMessage);
					writer.close();
				}
				else {
					//sendXML(xml);  // send to iMonitor
					sendSocketMessage(socketMessage);  // send to iMonitor
				}					
			}
		}
	}
	
	
	/**
	 * Send phone alarm with list of theme and a message
	 * @param themes ThemeVO array list
	 * @param message Message
	 * @throws IOException
	 */
	public static void sendAlarm(ArrayList<ThemeVO> themes, String message) throws IOException {
		Alarm.sendAlarm(themes,AlarmType.PHONE,null,message,false,false,true,null,null);
	}
	
	
	/**
	 * Send phone alarm with a theme and a message
	 * @param theme Theme
	 * @param message Message
	 * @throws IOException
	 */
	public static void sendAlarm(String theme, String message) throws IOException {
		ArrayList<ThemeVO> themes = new ArrayList<ThemeVO>();
		themes.add(new ThemeVO(theme));
		Alarm.sendAlarm(themes,AlarmType.PHONE,null,message,false,false,true,null,null);
	}

	
	/**
	 * Construct XML with file attachment
	 * @param themes Themes
	 * @param subject Subject
	 * @param bodymsg Body message
	 * @param say Is say
	 * @param email Is email alert
	 * @param phone Is phone alert
	 * @param attachment Attachment
	 * @param exc_add Exclude Address
	 * @return XML
	 */			
	private static String constructXMLWithFile(List<ThemeVO> themes,String subject,String bodymsg,boolean say,boolean email,boolean phone, String attachment,String exc_add) {
		subject = escapeChar(subject);
		String xml="<IMONITOR>";
		
		for (int i=0; i<themes.size(); i++) {
			ThemeVO themeVO = themes.get(i);
			if (themeVO != null) {
				xml+="<THEME>"+themeVO.getName()+"</THEME>";	 
			}
		}
		
		xml+="<SUBJECT>"+subject+"</SUBJECT>";
		xml+="<BODY><![CDATA["+bodymsg+"]]></BODY>";
		xml+="<OVERRIDEALARMLEVEL>m</OVERRIDEALARMLEVEL> ";		
		xml+="<SAY>"+(say+"").toUpperCase()+"</SAY> ";
		xml+="<EMAIL>"+(email+"").toUpperCase()+"</EMAIL>";
		xml+="<PHONE>"+(phone+"").toUpperCase()+"</PHONE>";
		xml+="<ESCALATETICKS>5</ESCALATETICKS>"; 
		xml+="<ESCALATEINTERVAL>15</ESCALATEINTERVAL>";
		if(attachment!=null){
			xml+="<EMAILIMAGES>"+attachment+"</EMAILIMAGES>";
		}
		if(exc_add!=null){
			xml+="<EXCLUDEADDRESS>"+exc_add+"</EXCLUDEADDRESS>";
		}
		xml+="</IMONITOR>";		
		return xml;
	}

	/**
	 * Construct message with file attachment
	 * @param themes Themes
	 * @param subject Subject
	 * @param bodymsg Body message
	 * @param say Is say
	 * @param email Is email
	 * @param phone Is phone
	 * @param attachment Attachment
	 * @param exc_add Exclude Address
	 * @return XML
	 */			
	private static String constructSocketMessageWithFile(List<ThemeVO> themes,String subject,String bodymsg,boolean say,boolean email,boolean phone, String attachment,String exc_add) {
		subject = escapeChar(subject);
		
		String message = "";
		for (int i=0; i<themes.size(); i++) {
			ThemeVO themeVO = themes.get(i);
			if (themeVO != null) {
				message+="~theme$#="+themeVO.getName();	 
			}
		}
		message+="~subject$#="+subject;
		message+="~body$#="+bodymsg;
		//message+="~recipients$#="+theme;
		message+="~sayIt$#="+(say+"").toUpperCase();
		message+="~emailIt$#="+(email+"").toUpperCase();
		message+="~phoneCall$#="+(phone+"").toUpperCase();
		message+="~emailAttachments$#="+attachment;
		//message+="~ym$#="+theme;
		//message+="~sms$#="+theme;
		message+="<EOF>";
		
		// remove first char : '~'
		message=message.substring(1, message.length());
		
		/*
		xml+="<OVERRIDEALARMLEVEL>m</OVERRIDEALARMLEVEL> ";		
		xml+="<ESCALATETICKS>5</ESCALATETICKS>"; 
		xml+="<ESCALATEINTERVAL>15</ESCALATEINTERVAL>";
		if(attachment!=null){
			xml+="<EMAILIMAGES>"+attachment+"</EMAILIMAGES>";
		}
		if(exc_add!=null){
			xml+="<EXCLUDEADDRESS>"+exc_add+"</EXCLUDEADDRESS>";
		}
		*/

		return message;
	}
	
	/**
	 * Send XML to iMonitor API
	 * @param xml XML
	 * @throws IOException
	 */		
	private static void sendXML(String xml) throws IOException {
		try{
			String url=getConfigValue("imonitor.url");
	
			PostMethod post = new PostMethod(url);
		    try {
		    	post.setRequestEntity(new StringRequestEntity(xml,"text/xml",null ));
		        post.setRequestHeader("Content-type",    "text/xml; utf-8");
		        HttpClient httpclient = new HttpClient();
		        int result = httpclient.executeMethod(post);
		    } catch (IOException e) {
		       // e.printStackTrace();
		        throw e;
		    } finally {
		        post.releaseConnection();
		    }
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * Send message via Socket to iMonitor API
	 * @param message Message
	 * @throws Exception
	 */		
	private static void sendSocketMessage(String socketMessage) throws IOException {
		String ip = getConfigValue("imonitor.ip");
		int port =  Integer.parseInt(getConfigValue("imonitor.port"));
	    Socket socket = new Socket(ip, port);
	    OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
        osw.write(socketMessage, 0, socketMessage.length());
        osw.flush();
        socket.close();
	}
	
	/**
	 * Escape XML characters
	 * @param str String
	 * @return String with escape characters
	 */		
	private static String escapeChar(String str) {
		String result = null;
		if (str != null) {
			result = str.replaceAll("&", "&amp;");
			result = result.replaceAll("\"", "&quot;");
			result = result.replaceAll("'", "&apos;");
			result = result.replaceAll("<", "&lt;");
			result = result.replaceAll(">", "&gt;");
		}
		return result;
	}
	
	
	/**
	 * Get the value of a property defined in config file
	 * @param propertyName - the name or key of the property you want to retrieve
	 * @return the string value 
	 * @throws IOException
	 */
	private static String getConfigValue(String propertyName) throws IOException
	{
	 	Properties prop = new Properties();
			String propFileName = "config_alarm.properties";

			InputStream inputStream = Alarm.class.getClassLoader().getResourceAsStream(propFileName);
			
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

 
