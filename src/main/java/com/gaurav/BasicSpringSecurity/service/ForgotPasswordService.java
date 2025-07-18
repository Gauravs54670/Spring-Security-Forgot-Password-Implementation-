package com.gaurav.BasicSpringSecurity.service;

import com.gaurav.BasicSpringSecurity.model.UserEntity;
import com.gaurav.BasicSpringSecurity.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@Slf4j
public class ForgotPasswordService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender javaMailSender;
    //getting the random token from the console
    public void forgotPasswordProcess(String email) {
        UserEntity userEntity = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found please user correct email"));
        System.out.println(userEntity);
        String resetToken = generateNumericToken();
        Instant expirationTime = Instant.now().plus(15, ChronoUnit.MINUTES);
        userEntity.setResetToken(resetToken);
        userEntity.setTokenExpiry(expirationTime);
        this.userRepository.save(userEntity);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(userEntity.getEmail());
        simpleMailMessage.setSubject("Reset Password Request");
        simpleMailMessage.setText(
                "Hi " + userEntity.getUsername() + ",\n\n" +
                        "To reset your password, use this following code:\n" + resetToken +
                        "\n\nIf you did not request this, please ignore this email."
        );
        javaMailSender.send(simpleMailMessage);
        System.out.println("Email sent to " + userEntity.getEmail());
    }

    //matching the random token
    @Transactional
    public void resetPassword(String token, String newPassword) {
        UserEntity userEntity = this.userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token used please try again"));
        if(userEntity.getResetToken() == null || userEntity.getTokenExpiry().isBefore(Instant.now()))
            throw new RuntimeException("Token is expired please try again with new token");
        if(passwordEncoder.matches(newPassword,userEntity.getPassword()))
            throw new RuntimeException("Can't use the old password again");
        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userEntity.setTokenExpiry(null);
        userEntity.setResetToken(null);
        this.userRepository.save(userEntity);
    }
    //Helper method
    private String generateNumericToken() {
        int token = (int)(Math.random() * 900000) + 100000;
        return String.valueOf(token);
    }
}
