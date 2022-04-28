package com.simpleproductapp.application.service;

import com.simpleproductapp.application.entity.MyUser;

public interface ResetPasswordService {
    
    void updateResetPasswordToken(String token, String email);

    MyUser get(String token);

    void updatePassword(MyUser myUser, String password);
}
