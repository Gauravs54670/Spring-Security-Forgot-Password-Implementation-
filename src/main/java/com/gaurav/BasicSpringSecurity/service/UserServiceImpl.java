package com.gaurav.BasicSpringSecurity.service;

import com.gaurav.BasicSpringSecurity.model.*;
import com.gaurav.BasicSpringSecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserResponse createUser(UserSignup userRequest) {
        boolean exist = this.userRepository.findByUsername(userRequest.getUsername()).isPresent();
        if(exist)
            throw new RuntimeException("User name is already taken");
        UserEntity newUserEntity = convertToEntity(userRequest);
        return convertToResponse(this.userRepository.save(newUserEntity));
    }

    @Override
    public UserResponse findByUsername(String username) {
        UserEntity existUserEntity = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToResponse(existUserEntity);
    }

    @Override
    public List<UserResponse> findAllUsers() {
        List<UserEntity> userEntityList = this.userRepository.findAll();
        if(userEntityList.isEmpty())
            throw new RuntimeException("No User Present");
        return userEntityList.stream().map(this::convertToResponse).toList();
    }

    @Override
    public void deleteUser(String username) {
        boolean exist = this.userRepository.findByUsername(username).isPresent();
        if(!exist)
            throw new RuntimeException("User not found");
        this.userRepository.deleteByUsername(username);
    }

    @Override
    public UserResponse updateUser(String username, UserDTO userDTO) {
        UserEntity userEntity = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(userDTO.getUsername() != null && !userDTO.getUsername().isEmpty())
            userEntity.setUsername(userDTO.getUsername());
        if(userDTO.getFullName() != null && !userDTO.getFullName().isEmpty())
            userEntity.setFullName(userDTO.getFullName());
        if(userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            if(passwordEncoder.matches(userDTO.getPassword(),userEntity.getPassword()))
                throw new RuntimeException("Entered password is same as previous password");
            userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        return convertToResponse(this.userRepository.save(userEntity));
    }

    @Override
    public UserResponse updateRole(String username, UserRole userRole) {
        UserEntity existUserEntity = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(existUserEntity.getUserRoles().contains(userRole))
            throw new RuntimeException("Role is already assigned");
        existUserEntity.setUserRoles(Set.of(userRole));
        return convertToResponse(this.userRepository.save(existUserEntity));
    }

    //Helper Methods
    private UserEntity convertToEntity(UserSignup userSignup) {
        return UserEntity.builder()
                .username(userSignup.getUsername())
                .password(passwordEncoder.encode(userSignup.getPassword()))
                .fullName(userSignup.getFullName())
                .createdAt(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .userRoles(Set.of(UserRole.USER))
                .build();
    }
    private UserResponse convertToResponse(UserEntity userEntity) {
        return UserResponse.builder()
                .username(userEntity.getUsername())
                .fullName(userEntity.getFullName())
                .createdAt(userEntity.getCreatedAt())
                .lastLogin(userEntity.getLastLogin())
                .userRoles(userEntity.getUserRoles())
                .build();
    }
}
