package com.java.java_proj.services.templates;

import com.java.java_proj.dto.request.forupdate.URequestUserPermission;
import com.java.java_proj.dto.response.forlist.LResponseUserPermission;

import java.util.List;

public interface UserPermissionService {

    public List<LResponseUserPermission> getAll();

    public List<LResponseUserPermission> updateAll(List<URequestUserPermission> userPermissionList);
}
