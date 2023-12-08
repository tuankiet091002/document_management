package com.java.java_proj.util;

import com.java.java_proj.entities.Email;
import com.java.java_proj.entities.User;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private EmailRepository emailRepository;

    private void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);

        javaMailSender.send(mimeMessage);
    }


    public void sendRegistrationSuccessEmail(User user, String password) {
        try {
            Email email = emailRepository.findById(1)
                    .orElseThrow( () -> new HttpException(HttpStatus.NOT_FOUND, "Email content not found!"));
            // Mail template here
            this.sendEmail(user.getEmail(),
                    email.getSubject(),
                    String.format(email.getBody(), user.getName(), user.getEmail(), password));
        } catch (Exception ex) {
            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot send email.");
        }
    }
}


