package com.example.bookstorebackend.Domain.Mailsender;

import com.example.bookstorebackend.Domain.Model.User.User;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@AllArgsConstructor
public class EmailSenderImp implements EmailSender {


    private JavaMailSender mailSender;

    @Override
    @Async
    public void send(String url, User theUser) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "Bookstores Registration Portal Service";
        String content = "<p> Hi, " + theUser.getLastName() + ", </p> " +
                "<p> Thanks you for Registering with us, " +
                "Please, follow the link below to complete your registration.</p>" +
                "<a href=\"" + url + "\">Verify your email to activate your account</a>" +
                " Thanks you. <br> User Bookstores Registration Portal Service";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("thenghia25022003@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);
        mailSender.send(message);
    }
}
