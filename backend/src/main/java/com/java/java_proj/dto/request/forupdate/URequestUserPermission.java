package com.java.java_proj.dto.request.forupdate;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class URequestUserPermission {

    @NotNull
    private Integer id;

    @NotNull
    public String syllabus;

    @NotNull
    public String trainingProgram;

    @NotNull
    public String classManagement;

    @NotNull
    public String learningMaterial;

    @NotNull
    public String userManagement;

}


