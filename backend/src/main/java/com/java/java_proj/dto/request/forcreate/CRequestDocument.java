package com.java.java_proj.dto.request.forcreate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CRequestDocument {

    @NotBlank(message = "Name  is required.")
    private String name;

    @NotBlank(message = "Document description is required.")
    private String description;

    @NotBlank(message = "Document content is required.")
    private String content;

    @NotNull(message = "Document file is required.")
    private MultipartFile file;

}