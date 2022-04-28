package com.simpleproductapp.application.security.userDetailsService;

import com.simpleproductapp.application.entity.MyUser;
import com.simpleproductapp.application.exceptions.MyUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("myUDS")
public class MyUserDetailsService implements UserDetailsService {

    private final MyUserDao myUserDao;

    @Autowired
    public MyUserDetailsService(MyUserDao myUserDao) {
        this.myUserDao = myUserDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<MyUser> user = myUserDao.findByUsername(username);
        user.orElseThrow(() -> new MyUserException("No username " +username+ " in the database."));

        if(user.get().isActive() && user.get().getDeleted()==null){
            return new PrincipleUser(user.get());
        }
        else if(!user.get().isActive() && user.get().getDeleted()==null){
            throw new MyUserException("User is suspended");
        }
        else if(user.get().getDeleted()!=null){
            throw new MyUserException("User is deleted");
        }
        else{
            throw new MyUserException("User does not exist");
        }
    }
}
