package com.java.java_proj.dto.request.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class RequestLogin {

    @NotBlank(message = "Email is null")
    @Email(message = "Email address is invalid. Please check and input again.")
    private String email;

    @NotBlank(message = "password is null")
    private String password;

}
