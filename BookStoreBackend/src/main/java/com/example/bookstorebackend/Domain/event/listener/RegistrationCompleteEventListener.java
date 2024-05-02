package com.example.bookstorebackend.Domain.event.listener;

import com.example.bookstorebackend.Domain.Mailsender.EmailSender;
import com.example.bookstorebackend.Domain.Model.User.User;
import com.example.bookstorebackend.Domain.UserService.UserServices;
import com.example.bookstorebackend.Domain.event.RegistrationCompleteEvent;
import com.example.bookstorebackend.Persistence.DAO.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final UserServices userServices;
    private final UserRepository userRepository;
    private final EmailSender emailSender;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //1.get the newly registered user
        User theUser = event.getUser();
        //2.Create a verification token for the user
        String verificationToken = UUID.randomUUID().toString();
        //3.Save the verification token for the user
        userServices.saveUserVerificationToken(theUser, verificationToken);
        //4.Build the verification url to be sent to the user
        String url = event.getApplicationUrl() + "/api/auth/verifyEmail?token=" + verificationToken;
        //5.send the email.
        log.info("Click the link to verify your registration: {}", url);
        try {
            emailSender.send(url, theUser);
        } catch (MessagingException e) {
            userRepository.delete(theUser);
            throw new RuntimeException("Gặp vấn đề khi gửi email xác minh", e);
        } catch (UnsupportedEncodingException e) {
            userRepository.delete(theUser);
            throw new RuntimeException("Lỗi khi mã hóa dữ liệu không được hỗ trợ", e);
        }

    }


}
