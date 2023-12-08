package com.java.java_proj.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.method.HandlerMethod;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
public class HttpExceptionResponseBody {

    private Date timestamp;
    private Integer status;
    private String error;
    private List<String> message;
    private String source;

    public HttpExceptionResponseBody(HttpException e, HandlerMethod handlerMethod) {
        timestamp = new Date(System.currentTimeMillis());
        status = e.getStatus().value();
        error = e.getStatus().getReasonPhrase();
        message = e.getMessageList();
        source = handlerMethod.getMethod().toString();
    }
}
