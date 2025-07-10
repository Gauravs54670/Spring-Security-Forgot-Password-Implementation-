package com.gaurav.BasicSpringSecurity.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDTO {
    private String username;
    private String password;
    private String fullName;
}
