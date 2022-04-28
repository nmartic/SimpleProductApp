package com.simpleproductapp.application.service;

import com.simpleproductapp.application.entity.MyUser;
import com.simpleproductapp.application.exceptions.MyUserException;
import com.simpleproductapp.application.repository.MyUserRepository;
import com.simpleproductapp.application.security.config.PasswordConfig;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class ResetPasswordServiceImpl implements ResetPasswordService{


    private final MyUserRepository resetPasswordRepository;
    private final PasswordConfig passwordConfig;

    public ResetPasswordServiceImpl(MyUserRepository resetPasswordRepository, PasswordConfig passwordConfig) {
        this.resetPasswordRepository = resetPasswordRepository;
        this.passwordConfig = passwordConfig;
    }

    @Override
    public void updateResetPasswordToken(String token, String email) {
        Long userId = resetPasswordRepository.findUserIdByEmail(email);

        if (userId!=null){
            MyUser myUser =resetPasswordRepository.findById(userId).get();
            myUser.setResetPasswordToken(token);
            resetPasswordRepository.save(myUser);
        }else{
            throw new MyUserException("Could not find any customer with email: "+email);
        }
    }

    @Override
    public MyUser get(String token) {
        return resetPasswordRepository.findByResetPasswordToken(token);
    }

    @Override
    public void updatePassword(MyUser myUser, String password) {
        String encodedPassword = passwordConfig.passwordEncoder().encode(password);

        myUser.setPassword(encodedPassword);
        myUser.setResetPasswordToken(null);
        myUser.setUpdated(Timestamp.valueOf(LocalDateTime.now()));

        resetPasswordRepository.save(myUser);
    }
}
