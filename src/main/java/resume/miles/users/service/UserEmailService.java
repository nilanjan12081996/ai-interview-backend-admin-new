package resume.miles.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class UserEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @org.springframework.beans.factory.annotation.Value("${spring.mail.username}")
    private String senderEmail;

    public void sendRegistrationEmail(String email, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(email);
        message.setSubject("Welcome to TalentFold - Your Account Details");
        message.setText("Congratulations! Your account has been created successfully.\n\n" +
                "Username/Email: " + email + "\n" +
                "Password: " + password + "\n\n" +
                "Please use these credentials to login and change your password from the Settings section after logging in.");
        mailSender.send(message);
    }
}
