package com.simpleproductapp.application.service;

import com.simpleproductapp.application.dto.MyUserDto;
import com.simpleproductapp.application.entity.MyUser;
import com.simpleproductapp.application.entity.Role;
import com.simpleproductapp.application.exceptions.MyUserException;
import com.simpleproductapp.application.parameterValidation.ParameterValidationService;
import com.simpleproductapp.application.repository.MyUserRepository;
import com.simpleproductapp.application.repository.RoleRepository;
import net.bytebuddy.utility.RandomString;
import net.sf.jasperreports.engine.JRException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MyUserServiceImpl implements MyUserService{

    private final MyUserRepository myUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ParameterValidationService parameterValidationService;
    private final ResetPasswordService resetPasswordService;
    private final EmailService emailService;
    private final PdfService pdfService;

    public MyUserServiceImpl(MyUserRepository myUserRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ParameterValidationService parameterValidationService, ResetPasswordService resetPasswordService, EmailService emailService, PdfService pdfService) {
        this.myUserRepository = myUserRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.parameterValidationService = parameterValidationService;
        this.resetPasswordService = resetPasswordService;
        this.emailService = emailService;
        this.pdfService = pdfService;
    }

    @Override
    public Object getUsers(Integer pageSize, Integer pageNumber, String search, Integer sortBy, Integer sortDirection) {

        String checkColumn = parameterValidationService.checkSortByForUser(sortBy);
        Sort.Direction checkSortDirection = parameterValidationService.checkSortingDirection(sortDirection);
        String checkSearch = parameterValidationService.checkSearch(search);
        Pageable sort = PageRequest.of(pageNumber, pageSize, Sort.by(checkSortDirection, checkColumn));

        return myUserRepository.findUsers(checkSearch, sort);
    }

    @Transactional
    @Override
    public void newUsers(List<MyUserDto> myUserDtos) {
        for (int i=0;i<myUserDtos.size();i++){
            if (myUserRepository.checkIfUsernameExistsAndNotDeleted(myUserDtos.get(i).getUsername())){
                throw new MyUserException("Username (email) "+myUserDtos.get(i).getUsername()+" already exists");
            }
            else if (myUserRepository.checkIfUsernameExistsAndDeleted(myUserDtos.get(i).getUsername())){
                MyUser myUser = myUserRepository.findByUuid(myUserDtos.get(i).getUuid());

                Long id = myUser.getId();

//              First Name
                try {
                    myUser.setFirstName(myUserDtos.get(i).getFirstName());
                }catch (Exception e){
                    throw new MyUserException("Problem with First Name");
                }

//              Last Name
                try {
                    myUser.setLastName(myUserDtos.get(i).getLastName());
                }catch (Exception e){
                    throw new MyUserException("Problem with Last Name");
                }

//              Username
                try {
                    myUser.setUsername(myUserDtos.get(i).getUsername());
                }catch (Exception e){
                    throw new MyUserException("Problem with Username");
                }

//              Password
                try {
                    myUser.setPassword(passwordEncoder.encode(myUserDtos.get(i).getPassword()));
                }catch (Exception e){
                    throw new MyUserException("Problem with Password");
                }

//              Role
                try {
                    myUser.setRole(roleRepository.findById(myUserDtos.get(i).getRoleId()).get());
                }catch (Exception e){
                    throw new MyUserException("Problem with Role");
                }

                myUser.setId(id);
                myUser.setActive(myUserDtos.get(i).isActive());
                myUser.setCreated(Timestamp.valueOf(LocalDateTime.now()));
                myUser.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
                myUser.setDeleted(null);
                myUser.setUuid(UUID.randomUUID());
                myUserRepository.save(myUser);
            }else {
                MyUser myUser = new MyUser();
//              First Name
                try {
                    myUser.setFirstName(myUserDtos.get(i).getFirstName());
                }catch (Exception e){
                    throw new MyUserException("Problem with First Name");
                }

//              Last Name
                try {
                    myUser.setLastName(myUserDtos.get(i).getLastName());
                }catch (Exception e){
                    throw new MyUserException("Problem with Last Name");
                }

//              Username
                try {
                    myUser.setUsername(myUserDtos.get(i).getUsername());
                }catch (Exception e){
                    throw new MyUserException("Problem with Username");
                }

//              Password
                try {
                    myUser.setPassword(passwordEncoder.encode(myUserDtos.get(i).getPassword()));
                }catch (Exception e){
                    throw new MyUserException("Problem with Password");
                }

//              Role
                try {
                    myUser.setRole(roleRepository.findById(myUserDtos.get(i).getRoleId()).get());
                }catch (Exception e){
                    throw new MyUserException("Problem with Role");
                }
                myUser.setActive(myUserDtos.get(i).isActive());
                myUser.setCreated(Timestamp.valueOf(LocalDateTime.now()));
                myUser.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
                myUser.setId(null);
                myUser.setUuid(UUID.randomUUID());
                myUserRepository.save(myUser);
            }
        }

    }

    @Override
    public void updateUser(MyUserDto newUser) {
        MyUser myUser = myUserRepository.findByUuid(newUser.getUuid());
        if (myUser==null){
            throw new MyUserException("User Does not exist");
        }
        try{
            myUser.setUsername(newUser.getUsername());
        }catch (Exception e){
            throw new MyUserException("Problem with Username");
        }
        if (newUser.getPassword() != null && !newUser.getPassword().equals("")) {
            try {
                myUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            } catch (Exception e) {
                throw new MyUserException("Problem with Password");
            }
        }
        try{
            myUser.setActive(newUser.isActive());
        }catch (Exception e){
            throw new MyUserException("Problem with Active");
        }
        try{
            myUser.setFirstName(newUser.getFirstName());
        }catch (Exception e){
            throw new MyUserException("Problem with FirstName");
        }
        try{
            myUser.setLastName(newUser.getLastName());
        }catch (Exception e){
            throw new MyUserException("Problem with LastName");
        }try{
            myUser.setRole(roleRepository.findById(newUser.getRoleId()).get());
        }catch (Exception e){
            throw new MyUserException("Problem with Role");
        }
        myUser.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        myUserRepository.save(myUser);

    }

    @Override
    public void deleteUser(UUID userUuid) {
        MyUser myUser = myUserRepository.findByUuid(userUuid);
        if (myUser==null){
            throw new MyUserException("User Does not exist");
        }
        myUser.setDeleted(Timestamp.valueOf(LocalDateTime.now()));
        myUserRepository.save(myUser);
    }

    @Override
    public void suspendUser(UUID userUuid) {
        MyUser myUser = myUserRepository.findByUuid(userUuid);
        if (myUser==null){
            throw new MyUserException("User Does not exist");
        }
        if(myUser.isActive()){
            myUser.setActive(false);
        }else {
            myUser.setActive(true);
        }
        myUser.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        myUserRepository.save(myUser);
    }

    @Override
    public void createMyUser(String username, String firstName, String lastName, String password, Role role) {
        if (myUserRepository.findByUsername(username)==null) {
            MyUser admin = new MyUser();
            admin.setUsername(username);
            admin.setCreated(Timestamp.valueOf(LocalDateTime.now()));
            admin.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
            admin.setActive(true);
            admin.setFirstName(firstName);
            admin.setLastName(lastName);
            admin.setPassword(passwordEncoder.encode(password));
            admin.setUuid(UUID.randomUUID());
            admin.setRole(role);
            myUserRepository.save(admin);
        }
    }

    @Override
    public void sendResetToken(String email, HttpServletRequest request){
        String token = RandomString.make(45);
        resetPasswordService.updateResetPasswordToken(token, email);
        String siteURL=request.getRequestURL().toString();
        siteURL.replace(request.getServletPath(), "");
        String resetPasswordLink = "http://localhost:3000/resetpassword?token="+token;
        String bodyText = "Pozdrav,<br><br>Klikni na link za resetiranje lozinke: <br><a href=\""+resetPasswordLink+"\">Promjeni lozinku</a><br><br>Lijep pozdrav,<br><br>SimpleProductApp";
        String subject = "Reset password link";
        emailService.sendEmail(subject, bodyText, email);
    }

    @Override
    public void checkToken(String token) {
        MyUser myUser = resetPasswordService.get(token);
        if (myUser==null){
            throw new MyUserException("Invalid token");
        }
    }

    @Override
    public void newPassword(String token, String password) {
        MyUser myUser = resetPasswordService.get(token);
        if (myUser==null){
            throw new MyUserException("Invalid token");
        }
        else{
            resetPasswordService.updatePassword(myUser, password);
        }
    }

    @Override
    public ResponseEntity<byte[]> getUsersPdf() throws JRException {
        return pdfService.viewMyUserList();
    }

    @Override
    public void emailUsersPdf(String email) {
        pdfService.createUsersPdf();
        String bodyText = "Pozdrav,<br><br>U prilogu imate pdf s listom svih usera<br><br>Lijep pozdrav,<br><br>SimpleProductApp";
        String pdfName = "myUsersList.pdf";
        String subject = "Pdf with all users";
        emailService.sendEmail(subject, bodyText, pdfName, email);
    }
}
