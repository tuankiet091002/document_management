package com.java.java_proj.services.templates;

import com.java.java_proj.entities.RefreshToken;

public interface RefreshTokenService {

    RefreshToken createToken(String userEmail);

    RefreshToken findActiveToken(String token);

    void deActiveUserToken(int userId);
}
