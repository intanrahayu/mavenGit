/******************************************************************************
*
* Copyright: Intellectual Property of Four Elements Capital Pte Ltd, Singapore.
* All rights reserved.
*
******************************************************************************/

package com.fourelementscapital.alarm;

/**
 * An interface to be implemented to do a process then being injected to SocketServer. 
 */
public interface SocketProcess {
	
	/**
	 * Run process
	 * @param strReadLine Message received by SocketServer to be processed further.
	 */		
	void run(String strReadLine);

}

 
