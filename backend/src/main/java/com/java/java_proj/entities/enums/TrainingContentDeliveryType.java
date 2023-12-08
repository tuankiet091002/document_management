package com.java.java_proj.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TrainingContentDeliveryType {
    ASSIGNMENT_LAB("assignment_lab"),
    CONCEPT_LECTURE("concept_lecture"),
    GUIDE_REVIEW("guide_review"),
    TEST_QUIZ("test_quiz"),
    EXAM("exam"),
    SEMINAR_WORKSHOP("seminar_workshop");

    private final String value;
}
