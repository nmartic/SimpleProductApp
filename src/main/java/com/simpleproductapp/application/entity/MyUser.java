package com.simpleproductapp.application.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean active;
    private UUID uuid;
    private String resetPasswordToken;
    private Timestamp created;
    private Timestamp updated;
    private Timestamp deleted;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}
