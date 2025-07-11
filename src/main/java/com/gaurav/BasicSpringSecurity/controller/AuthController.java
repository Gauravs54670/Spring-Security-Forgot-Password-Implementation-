package com.gaurav.BasicSpringSecurity.controller;

import com.gaurav.BasicSpringSecurity.Jwt.JwtUtils;
import com.gaurav.BasicSpringSecurity.model.AuthRequest;
import com.gaurav.BasicSpringSecurity.service.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
