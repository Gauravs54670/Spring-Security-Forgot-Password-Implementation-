package com.gaurav.BasicSpringSecurity.service;

import com.gaurav.BasicSpringSecurity.model.UserDTO;
import com.gaurav.BasicSpringSecurity.model.UserSignup;
import com.gaurav.BasicSpringSecurity.model.UserResponse;
import com.gaurav.BasicSpringSecurity.model.UserRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    public UserResponse createUser(UserSignup userRequest);
    public UserResponse findByUsername(String username);
    public List<UserResponse> findAllUsers();
    public void deleteUser(String username);
    public UserResponse updateUser(String username, UserDTO userDTO);
    public UserResponse updateRole(String username, UserRole userRole);
}
