package com.simpleproductapp.application.service;

import com.simpleproductapp.SimpleProductAppApplication;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import javax.mail.*;
import javax.mail.internet.*;

import java.io.File;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {

    ApplicationHome applicationHome = new ApplicationHome(SimpleProductAppApplication.class);

    @Override
    public void sendEmail(String subject, String bodyText, String email){
        String username = "simple.product.app@gmail.com";
        String password = "Qob24078";
        String fromEmail = "simple.product.app@gmail.com";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", 587);
        try {
            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            msg.setSubject(subject);

            Multipart emailContent = new MimeMultipart();
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setContent(bodyText,"text/html; charset=utf-8");
            emailContent.addBodyPart(textBodyPart);

            msg.setContent(emailContent);
            Transport.send(msg);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.METHOD_FAILURE, "Mail se nije uspio poslati - email ne postoji u bazi", e);
        }
    }

    @Override
    public void sendEmail(String subject, String bodyText, String pdfName, String email) {
        String username = "simple.product.app@gmail.com";
        String password = "Qob24078";
        String fromEmail = "simple.product.app@gmail.com";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", 587);
        try {
            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            msg.setSubject(subject);

            Multipart emailContent = new MimeMultipart();
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setContent(bodyText,"text/html; charset=utf-8");

            MimeBodyPart pdfAttachment = new MimeBodyPart();
            pdfAttachment.attachFile("src/main/resources/myUsersList.pdf");

            emailContent.addBodyPart(textBodyPart);
            emailContent.addBodyPart(pdfAttachment);

            msg.setContent(emailContent);
            Transport.send(msg);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.METHOD_FAILURE, "Mail se nije uspio poslati - email ne postoji u bazi", e);
        }
    }
}
