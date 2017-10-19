/******************************************************************************
*
* Copyright: Intellectual Property of Four Elements Capital Pte Ltd, Singapore.
* All rights reserved.
*
******************************************************************************/

package com.fourelementscapital.alarm;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A class for parsing & process stack alarms
 */
public class StackAlarm {
	
	private static final Logger log = LogManager.getLogger(StackAlarm.class.getName());
	
	/**
	 * Get recipients (email) & phone numbers
	 * @param theme Theme
	 * @param alarmLevel Alarm level (HIGH / MEDIUM /LOW)
	 * @return StackAlarmVO
	 */			
    public static StackAlarmVO collectRecipients(String theme, String alarmLevel) throws Exception
    {
		
		// GET RECIPIENTS :

		DBManager dbm = new DBManager("infrastructure");
		dbm.connect();
		
		theme = theme.toLowerCase();
		
		// get alarm level from VO
		if (alarmLevel == null || "".equals(alarmLevel.trim())) {
			throw new Exception("alarmLevel is null");
		}		
		if ("HIGH".equals(alarmLevel.toUpperCase())) {
			alarmLevel = "b.Alarm_High";
		}
		else if ("MEDIUM".equals(alarmLevel.toUpperCase())) {
			alarmLevel = "b.Alarm_Medium";
		}
		else if ("LOW".equals(alarmLevel.toUpperCase())) {
			alarmLevel = "b.Alarm_Low";
		}
		else {
			throw new Exception("No alarm level found !");
		}

		//log.debug("theme : " + theme + " | alarmLevel  : " + alarmLevel);

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT a.user, c.phonenumber, c.yahoo");
		sql.append(" FROM `tblTeamOrganization2` a left join tblRoleDefinition b on a." + theme + " = b.Letter ");
		sql.append(" left join tblContactDetails c on a.user = c.username");
		sql.append(" WHERE " + alarmLevel + "='1'");
		
		//System.out.println(">>> sql : " + sql);

		ResultSet rs = dbm.executeQuery(sql.toString());
		
		StackAlarmVO stackAlarmVO = new StackAlarmVO();
		stackAlarmVO.setTheme(theme);
		
		ArrayList recipients = new ArrayList();
		ArrayList phoneNumbers = new ArrayList();
		
		while(rs.next()){	
			recipients.add(rs.getString("a.user") + "@4ecap.com");
			phoneNumbers.add(rs.getString("c.phonenumber"));
		}
		stackAlarmVO.setRecipients(recipients);
		stackAlarmVO.setPhoneNumbers(phoneNumbers);
		
		//System.out.println(">>> recipients.size() : " + recipients.size());
		
    	return stackAlarmVO;
    }
    

    
}

 