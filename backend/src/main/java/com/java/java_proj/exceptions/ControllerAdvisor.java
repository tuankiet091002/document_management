package com.java.java_proj.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;

@ControllerAdvice
public class ControllerAdvisor {
    @ExceptionHandler(HttpException.class)
    public ResponseEntity<Object> handleSomeException(HttpException e, HandlerMethod handlerMethod) {
//      returning json object
        HttpExceptionResponseBody body = new HttpExceptionResponseBody(e, handlerMethod);
//       "timestamp", "status", "error", "message", "class"
        return new ResponseEntity<>(body, e.getStatus());
    }
}
