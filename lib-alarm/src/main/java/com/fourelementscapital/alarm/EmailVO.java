/******************************************************************************
*
* Copyright: Intellectual Property of Four Elements Capital Pte Ltd, Singapore.
* All rights reserved.
*
******************************************************************************/

package com.fourelementscapital.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Email Value Object
 */		
public class EmailVO implements Serializable {

	private String from;
	private String fromName;
	private String password;
	
	private String subject;
	private String body;
	
	private List<String> recipient;
	private List<String> recipientCC;
	private List<String> recipientBCC;
	private List<String> replyTo;
	
	private List<String> attachmentPath;
	private List<String> images;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public List<String> getRecipient() {
		return recipient;
	}

	public void setRecipient(List<String> recipient) {
		this.recipient = recipient;
	}

	public List<String> getRecipientCC() {
		return recipientCC;
	}

	public void setRecipientCC(List<String> recipientCC) {
		this.recipientCC = recipientCC;
	}

	public List<String> getRecipientBCC() {
		return recipientBCC;
	}

	public void setRecipientBCC(List<String> recipientBCC) {
		this.recipientBCC = recipientBCC;
	}

	public List<String> getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(List<String> replyTo) {
		this.replyTo = replyTo;
	}

	public List<String> getAttachmentPath() {
		return attachmentPath;
	}

	public void setAttachmentPath(List<String> attachmentPath) {
		this.attachmentPath = attachmentPath;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	
}


 