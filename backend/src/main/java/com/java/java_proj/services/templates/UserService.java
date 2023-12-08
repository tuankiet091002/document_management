package com.java.java_proj.services.templates;

import com.java.java_proj.dto.request.forcreate.CRequestUser;
import com.java.java_proj.dto.request.forupdate.URequestUser;
import com.java.java_proj.dto.request.forupdate.URequestUserPassword;
import com.java.java_proj.dto.request.security.RequestLogin;
import com.java.java_proj.dto.response.fordetail.DResponseUser;
import com.java.java_proj.entities.User;
import org.springframework.data.domain.Page;

public interface UserService {

    Page<DResponseUser> getAllUser(Integer id, String name, String email,
                                   String orderBy, Integer page, Integer size, String orderDirection);

    public DResponseUser getUser(String userEmail);

    public DResponseUser createUser(CRequestUser user);

    public DResponseUser updateUser(URequestUser user);

    public DResponseUser updateUserRole(Integer id, String role);

    public DResponseUser changePassword(Integer id, URequestUserPassword requestUserPassword);

    public User verifyUser(RequestLogin requestLogin);
}
