package org.example.webapptest.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class OrderEmailSender {

    private final JavaMailSender mailSender;

    public OrderEmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOrderEmail(String to, String subject, String content) {
        sendEmail(to, subject, content);
    }

    public void sendCustomerEmail(String customerEmail, String subject, String content) {
        sendEmail(customerEmail, subject, content);
    }

    private void sendEmail(String to, String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // true for HTML content

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
