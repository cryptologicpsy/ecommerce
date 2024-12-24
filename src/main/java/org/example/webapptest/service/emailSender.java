package org.example.webapptest.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class emailSender {
      // Username και Password για την αποστολή του email
      private static final String FROM_EMAIL = "your-email@gmail.com"; // Αντικαταστήστε με το email σας
      private static final String EMAIL_PASSWORD = "your-email-password"; // Αντικαταστήστε με τον κωδικό σας
  
      private final JavaMailSender javaMailSender;
  
      public emailSender(JavaMailSender javaMailSender) {
          this.javaMailSender = javaMailSender;
      }
  
      // Αποστολή email
      public void sendEmail(String toEmail, String subject, String messageBody) {
          SimpleMailMessage message = new SimpleMailMessage();
          message.setFrom(FROM_EMAIL);  // Ο αποστολέας (το email από το οποίο θα αποστέλλονται τα μηνύματα)
          message.setTo(toEmail);      // Ο παραλήπτης (το email του χρήστη)
          message.setSubject(subject); // Θέμα του email
          message.setText(messageBody); // Το περιεχόμενο του email
  
          // Αποστολή του email
          javaMailSender.send(message);
      }
}


