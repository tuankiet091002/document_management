package com.java.java_proj.controllers;

import com.java.java_proj.dto.request.forcreate.CRequestUser;
import com.java.java_proj.dto.request.forupdate.URequestUser;
import com.java.java_proj.dto.request.forupdate.URequestUserPassword;
import com.java.java_proj.dto.request.security.RequestLogin;
import com.java.java_proj.dto.request.security.RequestRefreshToken;
import com.java.java_proj.dto.response.fordetail.DResponseUser;
import com.java.java_proj.dto.response.security.ResponseJwt;
import com.java.java_proj.dto.response.security.ResponseRefreshToken;
import com.java.java_proj.entities.RefreshToken;
import com.java.java_proj.entities.User;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.services.templates.RefreshTokenService;
import com.java.java_proj.services.templates.UserService;
import com.java.java_proj.util.CustomUserDetail;
import com.java.java_proj.util.JWTTokenProvider;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/user")
@Api(tags = "User")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    JWTTokenProvider tokenProvider;
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public ResponseEntity<ResponseJwt> login(@Valid @RequestBody RequestLogin requestLogin) {
        // verified user account
        User loginUser = userService.verifyUser(requestLogin);

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(requestLogin.getEmail(), requestLogin.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        refreshTokenService.deActiveUserToken(loginUser.getId());

        ResponseJwt response = new ResponseJwt();
        response.setToken(tokenProvider.generateToken((CustomUserDetail) authentication.getPrincipal()));
        response.setEmail(loginUser.getEmail());
        response.setId(loginUser.getId());
        response.setRefreshToken(refreshTokenService.createToken(loginUser.getEmail()).getToken());
        response.setPermission(loginUser.getRole());

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<ResponseRefreshToken> reFreshToken(@Valid @RequestBody RequestRefreshToken requestRefreshToken) {

        RefreshToken refreshToken = refreshTokenService.findActiveToken(requestRefreshToken.getRefreshToken());

        if (refreshToken == null) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Refresh token is expired");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();

        ResponseRefreshToken response = new ResponseRefreshToken();
        response.setAccessToken(tokenProvider.generateToken(userDetail));
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @PostMapping("/register")
    public ResponseEntity<DResponseUser> addUser(@Valid @RequestBody CRequestUser requestUser,
                                                 BindingResult bindingResult) {

        // get validation error
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        DResponseUser user = userService.createUser(requestUser);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Page<DResponseUser>> getAllUser(@RequestParam(value = "id", defaultValue = "0") Integer id,
                                                          @RequestParam(value = "name", defaultValue = "") String name,
                                                          @RequestParam(value = "email", defaultValue = "") String email,
                                                          @RequestParam(value = "orderBy", defaultValue = "dob") String orderBy,
                                                          @RequestParam(value = "pageNo", defaultValue = "0") Integer page,
                                                          @RequestParam(value = "pageSize", defaultValue = "10") Integer size,
                                                          @RequestParam(value = "orderDirection", defaultValue = "DESC") String orderDirection) {

        List<String> allowedFields = Arrays.asList("id", "name", "email", "dob", "gender", "role");
        if (!allowedFields.contains(orderBy)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Order by column " + orderBy + " is illegal!");
        }

        List<String> allowedSort = Arrays.asList("ASC", "DESC");
        if (!allowedSort.contains(orderDirection)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Sort Direction " + orderDirection + " is illegal!");
        }

        Page<DResponseUser> userPage = userService.getAllUser(id, name, email, orderBy, page, size, orderDirection);

        return new ResponseEntity<>(userPage, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{userEmail}")
    public ResponseEntity<DResponseUser> getUser(@PathVariable String userEmail) {

        DResponseUser user = userService.getUser(userEmail);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<DResponseUser> updateUser(@Valid @RequestBody URequestUser requestUser,
                                                    BindingResult bindingResult) {
        // get validation error
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        DResponseUser user = userService.updateUser(requestUser);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/role")
    public ResponseEntity<DResponseUser> updateUserRole(@PathVariable Integer id, @RequestParam("role") String role) {

        if (!role.equals("admin") && !role.equals("user")) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Role is invalid");
        }

        DResponseUser user = userService.updateUserRole(id, role);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/password")
    public ResponseEntity<DResponseUser> changeUserPassword(@PathVariable Integer id, @RequestBody URequestUserPassword requestUserPassword) {

        DResponseUser user = userService.changePassword(id, requestUserPassword);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
