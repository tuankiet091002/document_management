package com.java.java_proj.dto.response.fordetail;

import com.java.java_proj.dto.response.forlist.LResponseUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class DResponseDocumentVersion {

    private String version;

    private String content;

    private String filename;

    private String url;

    private LResponseUser createdBy;

    private LocalDateTime createdDate;
}
