package com.java.java_proj.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "refresh_tokens")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(name = "expired_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "active", nullable = false, columnDefinition = "boolean default true")
    private boolean isActive = true;
}
