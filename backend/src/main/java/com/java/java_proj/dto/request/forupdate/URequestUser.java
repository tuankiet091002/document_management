package com.java.java_proj.dto.request.forupdate;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class URequestUser {

    private Integer id;

    @NotBlank(message = "User name is required.")
    private String name;

    @NotBlank(message = "Password is required.")
    private String password;

    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^0\\d{9}")
    private String phone;

    @NotBlank(message = "Date of birth is required.")
    private String dob;

    private Boolean gender;

}
