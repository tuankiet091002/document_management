package com.java.java_proj.repositories;

import com.java.java_proj.dto.response.fordetail.DResponseUser;
import com.java.java_proj.entities.User;
import com.java.java_proj.entities.UserPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    DResponseUser findByEmail(String email);

    Integer countByPhone(String phone);

    @Query(value = "SELECT u FROM User u WHERE " +
            "(:id = 0 OR u.id = :id) AND " +
            "u.name LIKE %:name% AND " +
            "u.email LIKE %:email% ")
    Page<DResponseUser> findAllBy(Integer id, String name, String email, Pageable paging);

    @Modifying
    @Query(value = "UPDATE User u SET u.role = ?2 WHERE u.id = ?1")
    @Transactional
    public void updateUserRole(Integer id, UserPermission role);

    @Query(value = "SELECT u FROM User u WHERE u.email = ?1")
    public User findUserByEmail(String email);

}
