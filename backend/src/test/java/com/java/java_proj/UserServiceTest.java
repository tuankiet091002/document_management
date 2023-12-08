package com.java.java_proj;

import com.java.java_proj.dto.request.forcreate.CRequestUser;
import com.java.java_proj.dto.request.forupdate.URequestUser;
import com.java.java_proj.dto.request.forupdate.URequestUserPassword;
import com.java.java_proj.dto.request.security.RequestLogin;
import com.java.java_proj.dto.response.fordetail.DResponseUser;
import com.java.java_proj.entities.User;
import com.java.java_proj.entities.UserPermission;
import com.java.java_proj.entities.enums.PermissionAccessType;
import com.java.java_proj.repositories.UserPermissionRepository;
import com.java.java_proj.repositories.UserRepository;
import com.java.java_proj.services.UserServiceImpl;
import com.java.java_proj.util.DateFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserPermissionRepository userPermissionRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private DateFormatter dateFormatter;

    @Before
    public void setUserRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            User user = new User();
            user.setId(1);
            user.setEmail("kiet" + i + 1 + "@gmail.com");

            users.add(user);
        }

        ProjectionFactory pf = new SpelAwareProxyProjectionFactory();
        List<DResponseUser> responseUsers = users.stream().map(
                entity -> pf.createProjection(DResponseUser.class, entity)
        ).collect(Collectors.toList());

        Mockito.when(userRepository.findAllBy(any(Integer.class), any(String.class),
                        any(String.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(responseUsers, pageable, responseUsers.size()));

        Mockito.when(userRepository.findByEmail(any(String.class)))
                .thenReturn(pf.createProjection(DResponseUser.class, responseUsers.get(0)));

        Mockito.when(userRepository.findByEmail(eq("kiet4@gmail.com")))
                .thenReturn(null);

        Mockito.when(userRepository.countByPhone(any(String.class)))
                .thenReturn(0);

        Mockito.when(userRepository.findById(any(Integer.class)))
                .thenReturn(Optional.of(users.get(0)));

        Mockito.doNothing().when(userRepository).updateUserRole(any(Integer.class), any(UserPermission.class));

        Mockito.when(userRepository.findUserByEmail(any(String.class)))
                .thenReturn(users.get(0));

        Mockito.when(userRepository.save(any(User.class)))
                .thenReturn(null);

    }

    @Before
    public void setUserPermissionRepository() {
        UserPermission role = new UserPermission();
        role.setRole("admin");
        role.setUserManagement(PermissionAccessType.FULL_ACCESS);
        role.setDocumentManagement(PermissionAccessType.FULL_ACCESS);

        Mockito.when(userPermissionRepository.findByRole("admin"))
                .thenReturn(role);
    }

    @Before
    public void setBCryptPasswordEncoder() {

        Mockito.when(bCryptPasswordEncoder.encode(any(String.class)))
                .thenReturn("newpassword");

        Mockito.when(bCryptPasswordEncoder.matches("oldpassword", null))
                .thenReturn(true);

    }

    @Before
    public void setDateFormatter() {
        Mockito.when(dateFormatter.formatDate(any(String.class)))
                .thenReturn(LocalDate.now());
    }

    @Test
    public void getAllUserTest() {

        Page<DResponseUser> userPage = userService.getAllUser(0, "", "", "dob", 0, 10, "DESC");

        Assertions.assertEquals(userPage.getSize(), 10);
        Assertions.assertEquals(userPage.getTotalPages(), 1);
        Assertions.assertEquals(userPage.getContent().get(0).getId(), 1);

        Mockito.verify(userRepository).findAllBy(any(Integer.class), any(String.class),
                any(String.class), any(Pageable.class));
    }

    @Test
    public void createUserTest() {

        CRequestUser requestUser = new CRequestUser();
        requestUser.setEmail("kiet4@gmail.com");
        requestUser.setRole("admin");
        requestUser.setPhone("0123456789");

        userService.createUser(requestUser);

        Mockito.verify(userPermissionRepository).findByRole(any(String.class));
        Mockito.verify(userRepository).save(any(User.class));
        Mockito.verify(userRepository, times(2)).findByEmail(any(String.class));
        Mockito.verify(userRepository).countByPhone(any(String.class));
    }

    @Test
    public void updateUserTest() {

        URequestUser requestUser = new URequestUser();
        requestUser.setId(1);
        requestUser.setPhone("0123456789");

        userService.updateUser(requestUser);

        Mockito.verify(userRepository).save(any(User.class));
        Mockito.verify(userRepository).findById(1);
        Mockito.verify(userRepository).countByPhone("0123456789");
    }

    @Test
    public void changePasswordTest() {
        URequestUserPassword userPassword = new URequestUserPassword();
        userPassword.setNewPassword("newpassword");
        userPassword.setOldPassword("oldpassword");

        userService.changePassword(1, userPassword);

        Mockito.verify(userRepository).findById(1);
        Mockito.verify(bCryptPasswordEncoder).matches("oldpassword", null);
        Mockito.verify(bCryptPasswordEncoder).encode(any(String.class));
        Mockito.verify(userRepository).findById(1);
    }

    @Test
    public void updateUserRoleTest() {

        userService.updateUserRole(1, "admin");

        Mockito.verify(userRepository).findById(1);
        Mockito.verify(userPermissionRepository).findByRole("admin");
        Mockito.verify(userRepository).updateUserRole(eq(1), any(UserPermission.class));
    }

    @Test
    public void verifyUserTest() {
        RequestLogin requestLogin = new RequestLogin();
        requestLogin.setEmail("kiet1@gmail.com");
        requestLogin.setPassword("oldpassword");

        userService.verifyUser(requestLogin);

        Mockito.verify(userRepository).findUserByEmail("kiet1@gmail.com");
        Mockito.verify(bCryptPasswordEncoder).matches("oldpassword", null);
    }

}
