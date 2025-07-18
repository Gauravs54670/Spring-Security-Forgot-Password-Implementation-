package com.gaurav.BasicSpringSecurity.model;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String newPassword;
    private String token;
}
