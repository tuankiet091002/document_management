package com.java.java_proj.dto.response.forlist;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class LResponseDocument {

    private Integer id;

    private String name;

    private String description;

    private String lastVersion;

    public LocalDateTime lastModified;
}
