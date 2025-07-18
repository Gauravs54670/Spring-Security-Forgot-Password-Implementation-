package com.gaurav.BasicSpringSecurity.controller;

import com.gaurav.BasicSpringSecurity.Jwt.JwtUtils;
import com.gaurav.BasicSpringSecurity.model.AuthRequest;
import com.gaurav.BasicSpringSecurity.model.ForgotPasswordRequest;
import com.gaurav.BasicSpringSecurity.model.ResetPasswordRequest;
import com.gaurav.BasicSpringSecurity.service.CustomUserDetailsService;
import com.gaurav.BasicSpringSecurity.service.ForgotPasswordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/public/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private ForgotPasswordService forgotPasswordService;
    @PostMapping
    public ResponseEntity<?> generateToken(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword()));
            log.info("Authentication Complete");
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(authRequest.getUsername());
            String token = jwtUtils.generatingToken(userDetails);
            Map<String, String> map = new HashMap<>();
            map.put("token", token);
            map.put("message","User logged in successfully");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            //If authentication failed
            Map<String,String> map = new HashMap<>();
            map.put("message","Token generation failed please try again");
            map.put("response",e.getMessage());
            return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        log.info("request parsed");
        try {
            this.forgotPasswordService.forgotPasswordProcess(forgotPasswordRequest.getEmail());
            return ResponseEntity.ok(Map.of("message","Password reset link sent check email"));
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("message","error occurred please try again","response",e.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        try {
            this.forgotPasswordService.resetPassword(resetPasswordRequest.getToken(),resetPasswordRequest.getNewPassword());
            return ResponseEntity.ok(Map.of("message","Password has been reset successfully"));
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("message","error in reset password","response",e.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }
}
