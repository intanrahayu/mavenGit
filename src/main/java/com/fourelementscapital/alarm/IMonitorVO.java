/******************************************************************************
*
* Copyright: Intellectual Property of Four Elements Capital Pte Ltd, Singapore.
* All rights reserved.
*
******************************************************************************/

package com.fourelementscapital.alarm;

import java.util.ArrayList;
import java.util.List;

/**
 * IMonitor Value Object to store message details
 */

public class IMonitorVO {
	
	private String id;
	private List<String> theme;
	//private String sendingEmailStatus;
	private String subject;
	private List<String> emailReplyTo;
	private List<String> emailBCC;
	private List<String> emailCC;
	private List<String> images;
	private List<String> attachments;
	private boolean sayIt;
	private boolean emailIt;
	private boolean phoneIt;
	private boolean smsIt;
	private String alertLevel;
	private List<String> phoneNumbers;
	private List<String> recipients;
	private List<String> yahooMessenger;
	private String escalation;
    private String message;
    private String body;
    
    private String emailFrom;
    private String emailFromName;
    private String emailPassword;
    
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getTheme() {
		return theme;
	}
	public void setTheme(List<String> theme) {
		this.theme = theme;
	}
/*
	public String getSendingEmailStatus() {
		return sendingEmailStatus;
	}
	public void setSendingEmailStatus(String sendingEmailStatus) {
		this.sendingEmailStatus = sendingEmailStatus;
	}
*/	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public List<String> getEmailReplyTo() {
		return emailReplyTo;
	}
	public void setEmailReplyTo(List<String> emailReplyTo) {
		this.emailReplyTo = emailReplyTo;
	}
	public List<String> getEmailBCC() {
		return emailBCC;
	}
	public void setEmailBCC(List<String> emailBCC) {
		this.emailBCC = emailBCC;
	}
	public List<String> getEmailCC() {
		return emailCC;
	}
	public void setEmailCC(List<String> emailCC) {
		this.emailCC = emailCC;
	}
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
	public List<String> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<String> attachments) {
		this.attachments = attachments;
	}
	public boolean isSayIt() {
		return sayIt;
	}
	public void setSayIt(boolean sayIt) {
		this.sayIt = sayIt;
	}
	public boolean isEmailIt() {
		return emailIt;
	}
	public void setEmailIt(boolean emailIt) {
		this.emailIt = emailIt;
	}
	public boolean isPhoneIt() {
		return phoneIt;
	}
	public void setPhoneIt(boolean phoneIt) {
		this.phoneIt = phoneIt;
	}
	public boolean isSmsIt() {
		return smsIt;
	}
	public void setSmsIt(boolean smsIt) {
		this.smsIt = smsIt;
	}
	public String getAlertLevel() {
		return alertLevel;
	}
	public void setAlertLevel(String alertLevel) {
		this.alertLevel = alertLevel;
	}
	public List<String> getPhoneNumbers() {
		return phoneNumbers;
	}
	public void setPhoneNumbers(List<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}
	public List<String> getRecipients() {
		return recipients;
	}
	public void setRecipients(List<String> recipients) {
		this.recipients = recipients;
	}
	public List<String> getYahooMessenger() {
		return yahooMessenger;
	}
	public void setYahooMessenger(List<String> yahooMessenger) {
		this.yahooMessenger = yahooMessenger;
	}
	public String getEscalation() {
		return escalation;
	}
	public void setEscalation(String escalation) {
		this.escalation = escalation;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	public String getEmailFrom() {
		return emailFrom;
	}
	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}
	public String getEmailFromName() {
		return emailFromName;
	}
	public void setEmailFromName(String emailFromName) {
		this.emailFromName = emailFromName;
	}
	public String getEmailPassword() {
		return emailPassword;
	}
	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}
    


    
}

 
