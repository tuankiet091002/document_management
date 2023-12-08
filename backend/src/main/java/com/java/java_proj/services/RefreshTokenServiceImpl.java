package com.java.java_proj.services;

import com.java.java_proj.entities.RefreshToken;
import com.java.java_proj.entities.User;
import com.java.java_proj.repositories.RefreshTokenRepository;
import com.java.java_proj.repositories.UserRepository;
import com.java.java_proj.services.templates.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public RefreshToken createToken(String userEmail) {

        RefreshToken token = new RefreshToken();
        User user = userRepository.findUserByEmail(userEmail);

        // set required fields
        token.setExpiryDate(LocalDate.now().plusDays(30));
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(token);
    }

    @Override
    public RefreshToken findActiveToken(String token) {
        return refreshTokenRepository.findUnexpiredToken(token);
    }

    @Override
    public void deActiveUserToken(int userId) {
        refreshTokenRepository.deActiveUserToken(userId);
    }
}
