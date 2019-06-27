package cloud.data.integration.common.util;

import java.io.File;
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

import org.apache.log4j.Logger;

import cloud.data.integration.module.invoice.util.InvoiceUtil;



/**
 * 
 * @author LBerwa
 *
 */
public class MailManager {
	
	
	final static Logger logger = Logger.getLogger(MailManager.class);
	

	
	/*public static void main(String args[]) {
	//	MailManager mail = new MailManager();
	//	mail.sendMailWithAttachment("sbalawatri@cherryroad.com", "sbalawatri@cherryroad.com", "Hello", "Hello Scott.....Test Message......Coming from the integration app....", "C:\\sud\\TEST_REPORT_REQUEST.xml");
	}*/

	public static void sendMail(String from, String to,String cc, String subject, String appMessage) {
		// Get system properties
	      Properties properties = System.getProperties();
	      properties.setProperty("mail.smtp.host", InvoiceUtil.getPropertyValue("MAIL_HOST_NAME"));
	      //properties.setProperty("mail.smtp.password", "lnb@broomfield");
	      properties.put("mail.smtp.auth", InvoiceUtil.getPropertyValue("MAIL_SMTP_AUTH"));
	      properties.put("mail.smtp.starttls.enable", "true");
	      properties.put("mail.smtp.port", InvoiceUtil.getPropertyValue("MAIL_HOST_PORT"));
	      //properties.put("mail.debug", "true");
	      //properties.setProperty("mail.smtp.password", MAIL_HOST_PASSWORD);
	    
	      // Get the default Session object.
	      //Session session = Session.getDefaultInstance(properties);
	       
	      Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(InvoiceUtil.getPropertyValue("MAIL_USER_NAME"), 
							InvoiceUtil.getPropertyValue("MAIL_USER_PASSWORD"));
				}
			  });
	     
	      try {
	         MimeMessage message = new MimeMessage(session);
	         message.setFrom(new InternetAddress(from));
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
	         if(cc!=null) {
	         message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
	         }
	         message.setSubject(subject);
	         message.setText(appMessage);
	         
	         Transport.send(message);
	         logger.debug("Sent message successfully....");
	      }catch (MessagingException mex) {
	         mex.printStackTrace();
	      }
	}
	
	public static void sendMailWithAttachment(String from, String to,String cc, String subject, String appMessage, String fileName) {
		// Get system properties
	      Properties properties = System.getProperties();
	     

        
	      properties.setProperty("mail.smtp.host", InvoiceUtil.getPropertyValue("MAIL_HOST_NAME"));
	      properties.put("mail.smtp.auth", InvoiceUtil.getPropertyValue("MAIL_SMTP_AUTH"));
	      properties.put("mail.smtp.starttls.enable", "true");
	      properties.put("mail.smtp.port", InvoiceUtil.getPropertyValue("MAIL_HOST_PORT"));
	      //properties.put("mail.debug", "true");
	      //properties.setProperty("mail.smtp.password", MAIL_HOST_PASSWORD);
	    
	      // Get the default Session object.
	      //Session session = Session.getDefaultInstance(properties);
	       
	      Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(InvoiceUtil.getPropertyValue("MAIL_USER_NAME"), 
							InvoiceUtil.getPropertyValue("MAIL_USER_PASSWORD"));
				}
			  });
	     
	      try {
	         MimeMessage message = new MimeMessage(session);
	         message.setFrom(new InternetAddress(from));
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
	         if(cc!=null) {
		         message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
		     }
	         message.setSubject(subject);
	        
	         BodyPart messageBodyPart = new MimeBodyPart();
	         messageBodyPart.setText(appMessage);
	         Multipart multipart = new MimeMultipart();
	         multipart.addBodyPart(messageBodyPart);
	         messageBodyPart = new MimeBodyPart();
	         File  fileStream =  new File(fileName);
	         DataSource source = new FileDataSource(fileStream);
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName(fileStream.getName());
	         multipart.addBodyPart(messageBodyPart);
	         // Send the complete message parts
	         message.setContent(multipart);
	         
	         Transport.send(message);
	         logger.debug("Sent message successfully....");
	      }catch (MessagingException mex) {
	         mex.printStackTrace();
	      }
	}
	
}
