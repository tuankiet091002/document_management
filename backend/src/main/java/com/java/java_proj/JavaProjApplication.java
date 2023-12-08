package com.java.java_proj;

import com.java.java_proj.dto.response.forlist.LResponseUserPermission;
import com.java.java_proj.entities.Email;
import com.java.java_proj.entities.UserPermission;
import com.java.java_proj.entities.enums.PermissionAccessType;
import com.java.java_proj.repositories.EmailRepository;
import com.java.java_proj.repositories.UserPermissionRepository;
import com.java.java_proj.services.FirebaseFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class JavaProjApplication implements CommandLineRunner {

    @Autowired
    FirebaseFileService fileService;
    @Autowired
    private UserPermissionRepository userPermissionRepository;
    @Autowired
    private EmailRepository emailRepository;

    public static void main(String[] args)  {
        SpringApplication.run(JavaProjApplication.class, args);
    }

    @Override
    public void run(String... args) {
        fileService.init();
        this.autoUserPermission();
        this.autoEmailContent();
    }

    private void autoEmailContent() {
        List<Email> email = emailRepository.findAll();

        // check if email's exist
        if (email.isEmpty()) {
            Email emailAdd = Email.builder().subject("[FAMS] - Tài khoản được tạo thành công")
                    .body("<p>Hi, %s, </p>" +
                            "<p>Tài khoản đăng nhập vào hệ thống FAMS của bạn đã được tạo thành công. </p>" +
                            "<p>Vui lòng truy cập hệ thống theo thông tin sau: </p>" +
                            "<ul>" +
                            "<li>Username: %s</li>" +
                            "<li>Password: %s</li></ul>" +
                            "<p><b>Lưu ý</b>: Vui lòng thay đổi mật khẩu sau khi đăng nhập.</p>")
                    .build();

            emailRepository.save(emailAdd);
            System.out.println("Email content added!");
        }


    }

    private void autoUserPermission() {
        List<LResponseUserPermission> userPermissionList = userPermissionRepository.findAllBy();

        // check if user permission's exist
        if (userPermissionList.isEmpty()) {
            UserPermission admin = new UserPermission();
            admin.setRole("admin");
            admin.setUserManagement(PermissionAccessType.FULL_ACCESS);
            admin.setDocumentManagement(PermissionAccessType.FULL_ACCESS);
            // user
            UserPermission user = new UserPermission();
            user.setRole("user");
            user.setUserManagement(PermissionAccessType.VIEW);
            user.setDocumentManagement(PermissionAccessType.VIEW);
            // adding to db
            userPermissionRepository.save(admin);
            userPermissionRepository.save(user);

            System.out.println("Adding missing user permissions!");
        }
    }
}
