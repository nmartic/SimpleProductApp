package com.simpleproductapp.application.repository;


import com.simpleproductapp.application.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(value = "select r.* from role r where r.role like :role", nativeQuery = true)
    Role findByRole(@RequestParam("role") String role);
}
