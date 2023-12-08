package com.java.java_proj.services;

import com.java.java_proj.dto.request.forupdate.URequestUserPermission;
import com.java.java_proj.dto.response.forlist.LResponseUserPermission;
import com.java.java_proj.entities.User;
import com.java.java_proj.entities.UserPermission;
import com.java.java_proj.entities.enums.PermissionAccessType;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.UserPermissionRepository;
import com.java.java_proj.services.templates.UserPermissionService;
import com.java.java_proj.util.CustomUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserPermissionServiceImpl implements UserPermissionService {

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    private User getOwner() {
        return SecurityContextHolder.getContext().getAuthentication() == null ? null :
                ((CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
    }

    public List<LResponseUserPermission> getAll() {
        return userPermissionRepository.findAllBy();
    }

    public List<LResponseUserPermission> updateAll(List<URequestUserPermission> userPermissionList) {

        User owner = getOwner();

        for (URequestUserPermission request : userPermissionList) {

            // check if permission exist
            UserPermission userPermission = userPermissionRepository.findById(request.getId())
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Permission not found."));

            // check role constraint
            if (Objects.equals(userPermission.getRole(), "super_admin")
                    && (owner == null || !Objects.equals(owner.getRole().getRole(), "super_admin"))) {
                throw new HttpException(HttpStatus.BAD_REQUEST, "Role other than “Super Admin” can not change permission of Role “Super Admin”");
            }

            try {
                userPermission.setUserManagement(PermissionAccessType.valueOf(request.getUserManagement()));
            } catch (IllegalArgumentException e) {
                throw new HttpException(HttpStatus.BAD_REQUEST,
                        "Wrong permission access value, accepted value: ACCESS_DENIED, VIEW, MODIFY, CREATE, ALL_ACCESS");
            }


            userPermissionRepository.save(userPermission);
        }

        return userPermissionRepository.findAllBy();
    }

}

