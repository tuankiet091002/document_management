package com.java.java_proj.controllers;

import com.java.java_proj.dto.request.forupdate.URequestUserPermission;
import com.java.java_proj.dto.response.forlist.LResponseUserPermission;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.services.templates.UserPermissionService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
@Api(tags = "User Permission")
public class UserPermissionController {

    @Autowired
    UserPermissionService userPermissionService;

    @GetMapping("")
    public ResponseEntity<List<LResponseUserPermission>> getAllUserPermissions() {

        List<LResponseUserPermission> userPermissionList = userPermissionService.getAll();

        return new ResponseEntity<>(userPermissionList, HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<List<LResponseUserPermission>>
    updateAllUserPermission(@RequestBody List<URequestUserPermission> newUserPermission,
                            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        List<LResponseUserPermission> userPermissionList = userPermissionService.updateAll(newUserPermission);

        return new ResponseEntity<>(userPermissionList, HttpStatus.OK);
    }
}
