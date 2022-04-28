package com.simpleproductapp.application.service;

import com.simpleproductapp.application.entity.Role;
import com.simpleproductapp.application.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void createRole(String role, Long id) {
        if (roleRepository.findByRole(role)==null) {
            Role admin = new Role();
            admin.setCreated(Timestamp.valueOf(LocalDateTime.now()));
            admin.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
            admin.setUuid(UUID.randomUUID());
            admin.setId(id);
            admin.setRole(role);
            roleRepository.save(admin);
        }
    }

    @Override
    public Role findByRole(String role) {
        return roleRepository.findByRole(role);
    }
}
