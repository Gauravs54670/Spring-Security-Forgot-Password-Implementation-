package com.gaurav.BasicSpringSecurity.controller;

import com.gaurav.BasicSpringSecurity.model.RoleUpdateRequest;
import com.gaurav.BasicSpringSecurity.model.UserDTO;
import com.gaurav.BasicSpringSecurity.model.UserResponse;
import com.gaurav.BasicSpringSecurity.model.UserRole;
import com.gaurav.BasicSpringSecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    //get all users
    @GetMapping("/get-all")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserResponse> userResponses = this.userService.findAllUsers();
            Map<String,Object> map = new HashMap<>();
            map.put("message","Users list fetched successfully");
            map.put("response",userResponses);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch(Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("message","error in fetching users");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }
    //get a user
    @GetMapping
    public ResponseEntity<?> getUser(@RequestParam(value = "username") String username) {
        try {
            UserResponse userResponse = this.userService.findByUsername(username);
            Map<String,Object> map = new HashMap<>();
            map.put("message","User fetched successfully");
            map.put("response",userResponse);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch(Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("message","error in fetching users");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }
    //delete user
    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestParam(value = "username") String username) {
        try {
            this.userService.deleteUser(username);
            Map<String,Object> map = new HashMap<>();
            map.put("message","Users deleted successfully");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch(Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("message","error in fetching users");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }
    //update user
     @PutMapping("/update-user")
    public ResponseEntity<?> updateUser(@RequestParam(value = "username") String username,
                                        @RequestBody UserDTO userDTO) {
        try {
            UserResponse userResponse = this.userService.updateUser(username,userDTO);
            Map<String,Object> map = new HashMap<>();
            map.put("message","Users updated successfully");
            map.put("response",userResponse);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch(Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("message","error in updating users");
            map.put("response",e.getMessage());
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
     }
     //update user role
    @PutMapping("/update-role")
    public ResponseEntity<?> updateUserRole(@RequestParam(value = "username") String username,
                                            @RequestBody RoleUpdateRequest roleUpdateRequest) {
        try {
            UserRole role = UserRole.valueOf(roleUpdateRequest.getUserRole().toUpperCase());
            UserResponse userResponse = this.userService.updateRole(username,role);
            Map<String,Object> map = new HashMap<>();
            map.put("message","Role updated successfully");
            map.put("response",userResponse);
            return new ResponseEntity<>(map,HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Map.of("message","Invalid Role"),HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<>(Map.of("error",e.getMessage()),HttpStatus.CONFLICT);
        }
    }
}
