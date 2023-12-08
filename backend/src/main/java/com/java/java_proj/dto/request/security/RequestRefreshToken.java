package com.java.java_proj.dto.request.security;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RequestRefreshToken {

    @NotBlank
    private String refreshToken;

}
