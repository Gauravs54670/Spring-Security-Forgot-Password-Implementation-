package com.gaurav.BasicSpringSecurity.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    @Indexed(unique = true)
    private String password;
    private String fullName;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private Set<UserRole> userRoles;
    private String resetToken;
    private Instant tokenExpiry;
}
