package com.simpleproductapp.application.service;



import com.simpleproductapp.application.dto.MyUserDto;
import com.simpleproductapp.application.entity.Role;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

public interface MyUserService {
    Object getUsers(Integer pageSize, Integer pageNumber, String search, Integer sortBy, Integer sortDirection);

    void newUsers(List<MyUserDto> myUserDtos);

    void updateUser(MyUserDto myUserDto);

    void deleteUser(UUID userUuid);

    void suspendUser(UUID userUuid);

    void createMyUser(String username, String firstName, String lastName, String password, Role role);

    void checkToken(String token);

    void newPassword(String token, String password);

    void sendResetToken(String email, HttpServletRequest request);

    ResponseEntity<byte[]> getUsersPdf() throws JRException;

    void emailUsersPdf(String email);
}
