package com.gaurav.BasicSpringSecurity.controller;

import com.gaurav.BasicSpringSecurity.model.UserResponse;
import com.gaurav.BasicSpringSecurity.model.UserSignup;
import com.gaurav.BasicSpringSecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class PublicController {
    @Autowired
    private UserService userService;

    //create
    @PostMapping("/sign-up")
    public ResponseEntity<?> createUser(@RequestBody UserSignup userSignup) {
        try {
            UserResponse userDTO = this.userService.createUser(userSignup);
            Map<String, Object> map = new HashMap<>();
            map.put("message","User created successfully");
            map.put("response",userDTO);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch(Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("message","error in creating users");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }
}
