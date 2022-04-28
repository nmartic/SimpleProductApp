package com.simpleproductapp.application.repository;

import com.simpleproductapp.application.dto.ReturnUserDto;
import com.simpleproductapp.application.entity.MyUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

public interface MyUserRepository extends JpaRepository<MyUser, Long> {

    @Query(value = "select mu.* from my_user mu where mu.username = :username", nativeQuery = true)
    MyUser findByUsername(@RequestParam("username") String username);

    @Query(value = "select mu.* from my_user mu where mu.uuid = :uuid", nativeQuery = true)
    MyUser findByUuid(@RequestParam("uuid") UUID uuid);

    @Query(value = "select case when exists (select true from my_user where username= :username and deleted is  not null) then 'true' else 'false' end", nativeQuery = true)
    boolean checkIfUsernameExistsAndDeleted(@RequestParam("username") String username);

    @Query(value = "select case when exists (select true from my_user where username= :username and deleted is null) then 'true' else 'false' end", nativeQuery = true)
    boolean checkIfUsernameExistsAndNotDeleted(@RequestParam("username") String username);

    @Query(value = "select mu.first_name as firstName, mu.last_name as lastName, mu.username, cast(mu.uuid as varchar), mu.active from my_user mu " +
            "where (lower(mu.first_name) like :search or lower(mu.last_name) like :search or lower(mu.username) like :search or lower(cast(mu.uuid as varchar)) like :search) and mu.deleted is null", nativeQuery = true)
    List<ReturnUserDto> findUsers(@RequestParam("search") String search, Pageable sort);


//    Reset Password
    @Query(value = "select mu.* from my_user mu where mu.reset_password_token = :reset_password_token", nativeQuery = true)
    MyUser findByResetPasswordToken(@RequestParam("reset_password_token") String reset_password_token);

    @Query(value = "select mu.id from my_user mu where mu.username = :email and mu.deleted is null", nativeQuery = true)
    Long findUserIdByEmail(@RequestParam("email") String email);
}
