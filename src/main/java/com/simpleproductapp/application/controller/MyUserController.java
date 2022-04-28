package com.simpleproductapp.application.controller;

import com.simpleproductapp.application.dto.MyUserDto;
import com.simpleproductapp.application.entity.MyUser;
import com.simpleproductapp.application.exceptions.MyUserException;
import com.simpleproductapp.application.service.MyUserService;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/user")
public class MyUserController {

    private final MyUserService myUserService;

    public MyUserController(MyUserService myUserService) {
        this.myUserService = myUserService;
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_DRIVER')")
    public ResponseEntity<?> getUsers(@RequestParam(defaultValue = "10", required = false) Integer pageSize,
                                      @RequestParam (defaultValue = "0", required = false) Integer pageNumber,
                                      @RequestParam (required = false) String search,
                                      @RequestParam(defaultValue = "0", required = false) Integer sortBy,
                                      @RequestParam(defaultValue = "0", required = false) Integer sortDirection,
                                      HttpServletRequest request) throws Exception {
        log.debug("User: "+ request.getRemoteUser()+", endpoint: \"/user\", method: GET, time: "+ LocalDateTime.now()+", IP adress: "+request.getRemoteAddr()+", Browser info: "+ request.getHeader("User-Agent"));
        return new ResponseEntity<>(myUserService.getUsers(pageSize, pageNumber, search, sortBy, sortDirection), HttpStatus.OK);
    }

    @PostMapping()
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public ResponseEntity<?> newUsers(@RequestBody(required = false) List<MyUserDto> myUserDtos,
                         HttpServletRequest request) throws Exception {
        log.debug("User: "+ request.getRemoteUser()+", endpoint: \"/user\", method: POST, time: "+ LocalDateTime.now()+", IP adress: "+request.getRemoteAddr()+", Browser info: "+ request.getHeader("User-Agent"));
        myUserService.newUsers(myUserDtos);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping()
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public ResponseEntity<?> updateUser(@RequestBody MyUserDto myUserDto,
                           HttpServletRequest request) throws Exception {
        log.debug("User: "+ request.getRemoteUser()+", endpoint: \"/user\", method: PUT, time: "+ LocalDateTime.now()+", IP adress: "+request.getRemoteAddr()+", Browser info: "+ request.getHeader("User-Agent"));
        myUserService.updateUser(myUserDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping()
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public ResponseEntity<?> deleteUser(@RequestParam UUID userUuid,
                           HttpServletRequest request){
        log.debug("User: "+ request.getRemoteUser()+", endpoint: \"/user\", method: DELETE, time: "+ LocalDateTime.now()+", IP adress: "+request.getRemoteAddr()+", Browser info: "+ request.getHeader("User-Agent"));
        myUserService.deleteUser(userUuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/suspend")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public ResponseEntity<?> suspendUser(@RequestParam UUID userUuid,
                            HttpServletRequest request){
        log.debug("User: "+ request.getRemoteUser()+", endpoint: \"/user/suspend\", method: POST, time: "+ LocalDateTime.now()+", IP adress: "+request.getRemoteAddr()+", Browser info: "+ request.getHeader("User-Agent"));
        myUserService.suspendUser(userUuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


//    Reset Password
    @PostMapping("/sendresettoken")
    public ResponseEntity<?> sendResetToken(@RequestParam String email,
                          HttpServletRequest request){
        log.debug("User: "+ request.getRemoteUser()+", endpoint: \"/user/sendemail\", time: "+ LocalDateTime.now()+", IP adress: "+request.getRemoteAddr()+", Browser info: "+ request.getHeader("User-Agent")+", email: "+email);
        myUserService.sendResetToken(email, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/checktoken")
    public ResponseEntity<?> checkToken(@RequestParam String token,
                                        HttpServletRequest request){
        log.debug("User: "+ request.getRemoteUser()+", endpoint: \"/user/checktoken\", time: "+ LocalDateTime.now()+", IP adress: "+request.getRemoteAddr()+", Browser info: "+ request.getHeader("User-Agent"));
        myUserService.checkToken(token);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/newpassword")
    public ResponseEntity<?> newPassword(@RequestParam String token,
                            @RequestParam String password,
                                         HttpServletRequest request){
        log.debug("User: "+ request.getRemoteUser()+", endpoint: \"/user/newpassword\", time: "+ LocalDateTime.now()+", IP adress: "+request.getRemoteAddr()+", Browser info: "+ request.getHeader("User-Agent"));
        myUserService.newPassword(token, password);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


//    Pdf
    @GetMapping("/getuserspdf")
    public ResponseEntity<byte[]> getUsersPdf(HttpServletRequest request) throws JRException {
        log.debug("User: "+ request.getRemoteUser()+", endpoint: \"/user/getuserspdf\", time: "+ LocalDateTime.now()+", IP adress: "+request.getRemoteAddr()+", Browser info: "+ request.getHeader("User-Agent"));
        return myUserService.getUsersPdf();
    }

    @GetMapping("/emailuserspdf")
    public ResponseEntity<byte[]> emailUsersPdf(@RequestParam String email,
                                                HttpServletRequest request) throws JRException {
        log.debug("User: "+ request.getRemoteUser()+", endpoint: \"/user/emailuserspdf\", time: "+ LocalDateTime.now()+", IP adress: "+request.getRemoteAddr()+", Browser info: "+ request.getHeader("User-Agent"));
        myUserService.emailUsersPdf(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
