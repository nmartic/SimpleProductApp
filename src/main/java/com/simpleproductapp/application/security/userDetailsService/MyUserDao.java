package com.simpleproductapp.application.security.userDetailsService;

import com.simpleproductapp.application.entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyUserDao extends JpaRepository<MyUser, Long> {

    Optional<MyUser> findByUsername(String username);
}
