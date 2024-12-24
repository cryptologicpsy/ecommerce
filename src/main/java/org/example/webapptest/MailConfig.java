package org.example.webapptest;

import org.example.webapptest.service.OrderEmailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    // Ανάκτηση των παραμέτρων SMTP από το application.properties
    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host); // Ρύθμιση του host από το properties
        mailSender.setPort(port); // Ρύθμιση της θύρας από το properties
        mailSender.setUsername(username); // Ρύθμιση του username από το properties
        mailSender.setPassword(password); // Ρύθμιση του password από το properties

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.mime.charset", "UTF-8");

        return mailSender;
    }

    

    @Bean
    public OrderEmailSender orderEmailSender(JavaMailSender javaMailSender) {
        return new OrderEmailSender(javaMailSender);
    }
}
