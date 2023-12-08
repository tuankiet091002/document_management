package com.java.java_proj.dto.response.fordetail;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class DResponseDocument {

    private Integer id;

    private String name;

    private String description;

    private List<String> versionList;

    private DResponseDocumentVersion documentVersion;
}
