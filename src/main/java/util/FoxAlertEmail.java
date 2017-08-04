package util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class FoxAlertEmail {

	public void postMail(String recipient, String subject, String message, String from) {
		boolean debug = false;

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Authenticator auth = new SMTPAuthenticator();
		Session session = Session.getDefaultInstance(props, auth);

		session.setDebug(debug);

		Message msg = new MimeMessage(session);

		InternetAddress addressFrom;
		try {
			addressFrom = new InternetAddress(from);

			msg.setFrom(addressFrom);

			InternetAddress[] addressTo = new InternetAddress[1];
			addressTo[0] = new InternetAddress(recipient);
			msg.setRecipients(Message.RecipientType.TO, addressTo);

			msg.setSubject(subject);
			msg.setContent(message, "text/plain");
			Transport.send(msg);
		} catch (AddressException e) {
			LoggerFoxAlert.getLoggerInstance().logError(e.getMessage());
		} catch (MessagingException e) {
			LoggerFoxAlert.getLoggerInstance().logError(e.getMessage());
		}
	}
	
	public void postMailSSL(String recipient, String subject, String message, String from) {
		boolean debug = false;

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.auth", "true");

		Authenticator auth = new SMTPAuthenticator();
		Session session = Session.getDefaultInstance(props, auth);

		session.setDebug(debug);

		Message msg = new MimeMessage(session);

		InternetAddress addressFrom;
		try {
			addressFrom = new InternetAddress(from);

			msg.setFrom(addressFrom);

			InternetAddress[] addressTo = new InternetAddress[1];
			addressTo[0] = new InternetAddress(recipient);
			msg.setRecipients(Message.RecipientType.TO, addressTo);

			msg.setSubject(subject);
			msg.setContent(message, "text/plain");
			Transport.send(msg);
		} catch (AddressException e) {
			LoggerFoxAlert.getLoggerInstance().logError(e.getMessage());
		} catch (MessagingException e) {
			LoggerFoxAlert.getLoggerInstance().logError(e.getMessage());
		}
	}

	private class SMTPAuthenticator extends javax.mail.Authenticator {

		public PasswordAuthentication getPasswordAuthentication() {
			String username = "vnepomuceno@gmail.com";
			String password = Encryptor.decrypt("qrtsdf45@#$%RTFG", 
					"WERTYJKMNBGHfcvg", "VqqNb/k72tQMlhUTgvN+QQ==");
			System.out.println(password);
			return new PasswordAuthentication(username, password);
		}
	}

}
