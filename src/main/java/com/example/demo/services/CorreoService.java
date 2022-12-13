package com.example.demo.services;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;


public class CorreoService extends Thread{

	private Logger log = LoggerFactory.getLogger(CorreoService.class);
	@Autowired
    private Environment environment;
	
	private String destino;
	private String mensaje;
	public CorreoService(String destino,String mensaje) {
		this.destino = destino;
		this.mensaje = mensaje; 
	}
//	public void sendEmail(String destino,String mensaje) {
//
//	    Properties props = new Properties();
//
//	    props.put("mail.smtp.host", "smtp.gmail.com");
//	    props.put("mail.smtp.starttls.enable", "true");
//	    props.put("mail.smtp.user", environment.getProperty("spring.mail.username"));
//	    props.put("mail.smtp.port", "587");
//
//	    Session session = Session.getInstance(props, null);
//	
//		try {
//			MimeMessage msg = new MimeMessage(session);
//			msg.setFrom(new InternetAddress ("caseStudy.com"));
//		    msg.addRecipient(Message.RecipientType.TO,new InternetAddress(destino) );
//		    msg.setSubject("Compras caseStudy");
//		    msg.setText(mensaje,"ISO-8859-1","html");
//		    
//		    Transport transport = session.getTransport("smtp");
//
//		    transport.connect(environment.getProperty("spring.mail.username"), environment.getProperty("spring.mail.password"));
//		    transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
//		    transport.close();
//		} catch (MessagingException me) {
//			log.error(String.format( "Algo salio mal al enviar el correo",me.getLocalizedMessage()));
//		}
//
//	}
	
	public void run(){
		System.out.println("Mensaje "+mensaje+" Destino "+destino);
		    Properties props = new Properties();

		    props.put("mail.smtp.host", "smtp.gmail.com");
		    props.put("mail.smtp.starttls.enable", "true");
		    props.put("mail.smtp.user","julio.rodrigo.1989.rl@gmail.com");
		    props.put("mail.smtp.port", "587");

		    Session session = Session.getInstance(props, null);
		
			try {
				MimeMessage msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress ("caseStudy.com"));
			    msg.addRecipient(Message.RecipientType.TO,new InternetAddress(destino) );
			    msg.setSubject("Compras caseStudy");
			    msg.setText(mensaje,"ISO-8859-1","html");
			    
			    Transport transport = session.getTransport("smtp");

			    transport.connect("julio.rodrigo.1989.rl@gmail.com", "chieiflmdsjxatxb");
			    transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
			    transport.close();
			} catch (MessagingException me) {
				log.error(String.format( "Algo salio mal al enviar el correo",me.getLocalizedMessage()));
			}

		}
	
}
