package com.java.java_proj.services;

import com.java.java_proj.entities.User;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.UserRepository;
import com.java.java_proj.util.CustomUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public CustomUserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
        User currentUser = userRepository.findUserByEmail(username);
        
        if (currentUser == null) {
            throw new HttpException(HttpStatus.NOT_FOUND, "Email not found, please login again.");
        }
        return new CustomUserDetail(currentUser);
    }
}

