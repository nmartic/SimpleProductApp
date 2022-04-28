package com.simpleproductapp.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyUserDto {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean active;
    private UUID uuid;
    private Long roleId;
}
