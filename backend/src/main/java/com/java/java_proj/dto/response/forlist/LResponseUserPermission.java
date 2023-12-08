package com.java.java_proj.dto.response.forlist;

import com.java.java_proj.entities.enums.PermissionAccessType;

public interface LResponseUserPermission {
    public Integer getId();
    public String getRole();
    public PermissionAccessType getUserManagement();
    public PermissionAccessType getDocumentManagement();


}
