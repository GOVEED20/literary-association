package goveed20.PaymentConcentrator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String emailAddress;

    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    public void sendEmail(String recipientEmail, String subject, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject(subject);
        mailMessage.setFrom(emailAddress);
        mailMessage.setText(text);
        javaMailSender.send(mailMessage);
    }

}
