package com.gaurav.BasicSpringSecurity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RoleUpdateRequest {
    private String userRole;
}
