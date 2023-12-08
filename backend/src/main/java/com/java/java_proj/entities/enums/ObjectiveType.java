package com.java.java_proj.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ObjectiveType {
    ATTITUDE("attitude"),
    SKILL("skill"),
    KNOWLEDGE("knowledge"),
    HABIT("habit");

    private final String value;
}
