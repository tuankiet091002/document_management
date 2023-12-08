package com.java.java_proj.dto.request.forupdate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class URequestDocument {

    @NotNull(message = "Document id is required.")
    private Integer id;

    private Integer version;

    private String description;

    @NotBlank(message = "Document content is required.")
    private String content;

    @NotNull(message = "Document file is required.")
    private MultipartFile file;

}
