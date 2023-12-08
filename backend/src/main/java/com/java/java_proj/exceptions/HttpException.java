package com.java.java_proj.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class HttpException extends RuntimeException {

    private HttpStatus status;
    private List<String> messageList = new ArrayList<>();

    public HttpException() {
        super();
    }

    public HttpException(HttpStatus status, String message) {
        this.messageList.add(message);
        this.status = status;
    }

    public HttpException(HttpStatus status, BindingResult bindingResult) {
        this.messageList = bindingResult.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        this.status = status;
    }


}
