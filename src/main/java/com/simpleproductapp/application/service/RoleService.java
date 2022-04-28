package com.simpleproductapp.application.service;


import com.simpleproductapp.application.entity.Role;

public interface RoleService {
    void createRole(String role, Long id);

    Role findByRole(String role);
}
