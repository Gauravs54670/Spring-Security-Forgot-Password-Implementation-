package com.gaurav.BasicSpringSecurity.model;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSignup {
    @Indexed(unique = true)
    private String username;
    @Indexed(unique = true)
    private String password;
    private String fullName;
}
