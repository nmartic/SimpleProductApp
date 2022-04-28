package com.simpleproductapp.application.entity;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.UUID;

@Hidden
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String role;
    private UUID uuid;
    private Timestamp created;
    private Timestamp updated;
    private Timestamp deleted;
}