package com.java.java_proj.entities;

import com.java.java_proj.entities.enums.PermissionAccessType;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="user_permissions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String role;

    @Column(name = "document")
    @Enumerated(EnumType.ORDINAL)
    private PermissionAccessType documentManagement;

    @Column(name = "user")
    @Enumerated(EnumType.ORDINAL)
    private PermissionAccessType userManagement;


}
