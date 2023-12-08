package com.java.java_proj.dto.response.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseRefreshToken {

    private String accessToken;
    private String type = "Bearer";

}
