package com.simpleproductapp.application.service;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public interface EmailService {

    void sendEmail(String subject, String bodyText, String email);

    void sendEmail(String subject, String bodyText, String pdfName, String email);
}
