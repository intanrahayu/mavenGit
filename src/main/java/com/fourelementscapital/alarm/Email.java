/******************************************************************************
*
* Copyright: Intellectual Property of Four Elements Capital Pte Ltd, Singapore.
* All rights reserved.
*
******************************************************************************/

package com.fourelementscapital.alarm;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.StringUtils;


/**
 * A java class to compose and send email
 */
public class Email {
	
	/**
	 * Send email
	 * @param emailVO Email value object
	 * @throws IOException
	 */			
	public static void send(EmailVO emailVO) throws Exception {
		
		final String inputFrom = emailVO.getFrom();
		final String inputPassword = emailVO.getPassword();
		final String inputRecipient = StringUtils.join(emailVO.getRecipient(), ",");
		final String inputRecipientCC = StringUtils.join(emailVO.getRecipientCC(), ",");
		final String inputRecipientBCC = StringUtils.join(emailVO.getRecipientBCC(), ",");
		final String inputReplyTo = StringUtils.join(emailVO.getReplyTo(), ",");	

		Properties props = new Properties();
		
		props.put("mail.smtp.auth", Config.getConfigValue_email("mail.smtp.auth"));
		props.put("mail.smtp.starttls.enable", Config.getConfigValue_email("mail.smtp.starttls.enable"));
		props.put("mail.smtp.host", Config.getConfigValue_email("mail.smtp.host"));
		props.put("mail.smtp.port", Config.getConfigValue_email("mail.smtp.port"));

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {    
        	protected PasswordAuthentication getPasswordAuthentication() {    
        		return new PasswordAuthentication(inputFrom,inputPassword);  
        	}  
        });    

		try {
			
			if (inputRecipient == null || "".equals(inputRecipient)) {
				throw new Exception("There's no email recipient");
			}

			Message message = new MimeMessage(session);
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(inputRecipient));
			// set cc if not null
			if (inputRecipientCC != null && "".equals(inputRecipientCC.trim())) {
				message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(inputRecipientCC));
			}
			// set bcc if not null
			if (inputRecipientBCC != null && "".equals(inputRecipientCC.trim())) {
				message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(inputRecipientBCC));		
			}
			// set replyTo if not null
			if (inputReplyTo != null && "".equals(inputReplyTo.trim())) {
				message.setReplyTo(InternetAddress.parse(inputReplyTo));
			}
			message.setSubject(emailVO.getSubject());
			message.setFrom(new InternetAddress(inputFrom, emailVO.getFromName()));

			BodyPart messageBodyPart = new MimeBodyPart();

			Multipart multipart = new MimeMultipart();
			
			// set images :
			List<String> imageList = emailVO.getImages();
			String images = "";
			if (imageList != null && imageList.size() > 0) {
				for (int i=0; i<imageList.size(); i++) {
					addImages(multipart, imageList.get(i), i+"");
					images += "<img alt='embeddedimage_" + i + "' src=cid:embeddedImage_" + i + ">";
				}
			}
			
			// set body with images in it
			String body = emailVO.getBody();
			
			// if body is null then insert "" string to prevent error
			if (body == null) {
				body = "";
			}
			
			if (body.contains("{{{IMG}}}")) {
				body = body.replace("{{{IMG}}}", images);
			}
			else {
				body += "<br/>" + images;
			}
			messageBodyPart.setContent(body, "text/html");
			multipart.addBodyPart(messageBodyPart);
		
			// set attachments :
			List<String> attachmentPathList = emailVO.getAttachmentPath();
			
			if (attachmentPathList != null && attachmentPathList.size() > 0) {
				for (int i=0; i<attachmentPathList.size(); i++) {
					addAttachment(multipart, attachmentPathList.get(i));
				}
			}
			
			// Send the complete message parts
			message.setContent(multipart);
			
			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	
	private static void addAttachment(Multipart multipart, String filename) throws MessagingException {
	    DataSource source = new FileDataSource(filename);
	    BodyPart messageBodyPart = new MimeBodyPart();        
	    messageBodyPart.setDataHandler(new DataHandler(source));
	    messageBodyPart.setFileName(filename);
	    multipart.addBodyPart(messageBodyPart);
	}
	
	private static void addImages(Multipart multipart, String filename, String id) throws MessagingException {
	    DataSource source = new FileDataSource(filename);
	    BodyPart messageBodyPart = new MimeBodyPart();        
	    messageBodyPart.setDataHandler(new DataHandler(source));
	    messageBodyPart.setHeader("Content-ID", "<embeddedImage_"+ id +">");
	    multipart.addBodyPart(messageBodyPart);
	}

}

 