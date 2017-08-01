package ch.blutch.battlephoto.model.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import ch.blutch.battlephoto.model.entity.User;

public class MailUtil {
	
	private static final String SMTP_USERNAME = "gigon.thomas.b@gmail.com";
	private static final String SMTP_PASSWORD = "SidhiCore67";
	private static final String SMTP_HOST = "smtp.gmail.com";
	private static final String SMTP_PORT = "587";
	
	private static Properties googleProps = new Properties();
	static {
		googleProps.put("mail.smtp.auth", "true");
		googleProps.put("mail.smtp.starttls.enable", "true");
		googleProps.put("mail.smtp.host", SMTP_HOST);
		googleProps.put("mail.smtp.port", SMTP_PORT);		
	}
	
	private static Session googleSession = Session.getInstance(googleProps, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
				}
			  });
	
	
	public static boolean sendPasswordRequestMail(String email, String recoverLink) throws MessagingException {
		String newLine = "<br />";
		
		Transport transport = googleSession.getTransport("smtp");
		Message message = new MimeMessage(googleSession);
		message.setFrom(new InternetAddress("gigon.thomas.b@gmail.com"));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
		message.setSubject("Battlephoto - Récupération de mot de passe");
		message.setContent("Bonjour," + newLine + newLine
				+ "Cliquez sur le lien ci-dessous pour récupérer votre mot de passe et suivez les instructions."
				+ newLine + newLine + "<a href='" + recoverLink + "'>" + recoverLink + "</a>"
				+ newLine + newLine + "Avec nos meilleures salutations.", "text/html");
		
		transport.send(message);
		
		return true;
	}
}
