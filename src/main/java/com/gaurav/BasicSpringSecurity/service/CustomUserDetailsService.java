package com.gaurav.BasicSpringSecurity.service;

import com.gaurav.BasicSpringSecurity.model.UserEntity;
import com.gaurav.BasicSpringSecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
        List<GrantedAuthority> grantedAuthorities = user.getUserRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
                .collect(Collectors.toList());
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(grantedAuthorities)
                .build();
    }
}
