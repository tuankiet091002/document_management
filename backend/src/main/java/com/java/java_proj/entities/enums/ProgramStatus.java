package com.java.java_proj.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProgramStatus {
    DRAFT("draft"),
    ACTIVE("active"),
    INACTIVE("inactive");

    private final String value;
}
