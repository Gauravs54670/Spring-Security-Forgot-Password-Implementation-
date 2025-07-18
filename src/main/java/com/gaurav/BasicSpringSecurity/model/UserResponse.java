package com.gaurav.BasicSpringSecurity.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String username;
    private String email;
    private String fullName;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private Set<UserRole> userRoles;
}
