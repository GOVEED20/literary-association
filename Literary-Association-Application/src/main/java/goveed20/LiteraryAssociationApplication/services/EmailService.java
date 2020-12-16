package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.model.User;
import goveed20.LiteraryAssociationApplication.model.VerificationToken;
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
    public void sendVerificationEmail(User user, VerificationToken verificationToken, String pID) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete registration");
        mailMessage.setFrom(emailAddress);
        mailMessage.setText(String.format("Dear %s %s,%nTo confirm your account please click here: %n"
                + "http://localhost:9090/api/register/verification/" + pID + "?token=" +
                verificationToken.getDisposableHash(), user.getName(), user.getSurname()));
        javaMailSender.send(mailMessage);
    }
}
