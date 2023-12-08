package com.java.java_proj.entities.enums;

import com.java.java_proj.exceptions.HttpException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Getter
public enum PermissionAccessType {
    ACCESS_DENIED(1),
    VIEW(2),
    MODIFY(3),
    CREATE(4),
    FULL_ACCESS(5);

    private final Integer value;

    public static Optional<PermissionAccessType> valueOf(int value) {
        return Arrays.stream(values())
                .filter(t -> t.value == value)
                .findFirst();
    }

    public List<String> getAuthorityStringList(String prefix) {
        List<String> result = new ArrayList<>();

        for (int i = 1; i <= value; i++) {
            result.add(prefix + PermissionAccessType.valueOf(i)
                    .orElseThrow(() -> new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, "Wrong enum value.")));
        }

        return result;
    }
}