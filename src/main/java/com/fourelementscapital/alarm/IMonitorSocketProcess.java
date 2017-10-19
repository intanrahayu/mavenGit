/******************************************************************************
*
* Copyright: Intellectual Property of Four Elements Capital Pte Ltd, Singapore.
* All rights reserved.
*
******************************************************************************/

package com.fourelementscapital.alarm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fourelementscapital.alarm.SocketProcess;

/**
 * A class implement SocketProcess to be used as dependency injection in SocketServer, so SocketServer serve as StackAlarm listener. 
 */
public class IMonitorSocketProcess implements SocketProcess {
	
	private static final Logger log = LogManager.getLogger(IMonitorSocketProcess.class.getName());
	
	/**
	 * Run stack alarm process
	 * @param strReadLine Message received by SocketServer to be processed by StackAlarm.
	 */			
	public void run(String strReadLine) {

		try {
			
			IMonitorVO iMonitorVO = IMonitor.processMessage(strReadLine);  // RECEIVE socket message and process it (compose data & store to iMonitorVO)
			IMonitor.process(iMonitorVO); // vo is ready to be processed (SEND to email / phone / say)
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		
	}	

}

 
