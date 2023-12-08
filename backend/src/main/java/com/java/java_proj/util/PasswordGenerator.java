package com.java.java_proj.util;

import com.java.java_proj.entities.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class PasswordGenerator {

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "_#?!@$%^&*-";

    private final SecureRandom random = new SecureRandom();

    public String generatePassword(User user) {
        int length = 12;
        String password = generateRandomString(length);
        if (isValidPassword(password, user)) {
            return password;
        } else {
            return null;
        }
    }

    private String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        String allCharacters = UPPER + LOWER + DIGITS + SPECIAL;

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(allCharacters.length());
            sb.append(allCharacters.charAt(randomIndex));
        }

        return sb.toString();
    }

    private boolean containsAtLeastOne(String str, String characters) {
        for (char c : characters.toCharArray()) {
            if (str.contains(String.valueOf(c))) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidPassword(String password, User user) {
        // Kiểm tra các yêu cầu về mật khẩu
        return password != null &&
                password.length() >= 8 &&
                containsAtLeastOne(password, UPPER) &&
                containsAtLeastOne(password, LOWER) &&
                containsAtLeastOne(password, DIGITS) &&
                containsAtLeastOne(password, SPECIAL) &&
                containsConsecutiveCharacters(password, user.getName()) &&
                containsConsecutiveCharacters(password, user.getEmail());
    }

// ...

    private boolean containsConsecutiveCharacters(String str, String sequence) {
        int consecutiveCount = 0;

        for (int i = 0; i < str.length() - 1; i++) {
            if (str.regionMatches(true, i, sequence, 0, sequence.length())) {
                consecutiveCount++;
                if (consecutiveCount > 2) {
                    return false;
                }
            } else {
                consecutiveCount = 0;
            }
        }

        return true;
    }

}
