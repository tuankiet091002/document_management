package com.java.java_proj.repositories;

import com.java.java_proj.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Query(value = "SELECT t FROM RefreshToken t WHERE t.token = :token AND t.isActive = true")
    RefreshToken findUnexpiredToken(String token);

    @Transactional
    @Modifying
    @Query(value = "UPDATE refresh_tokens SET active = false WHERE user_id = ?1", nativeQuery = true)
    void deActiveUserToken(int userId);

}
