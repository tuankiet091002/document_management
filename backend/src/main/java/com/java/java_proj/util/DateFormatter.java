package com.java.java_proj.util;

import com.java.java_proj.exceptions.HttpException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;


@Service
public class DateFormatter {

    public LocalDate formatDate(String dateString) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);
            return LocalDate.parse(dateString, formatter);

        } catch (DateTimeParseException e) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Wrong date format.");
        }
    }
}
