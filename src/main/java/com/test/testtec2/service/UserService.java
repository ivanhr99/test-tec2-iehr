package com.test.testtec2.service;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserService implements UserDetailsService {

    private String username = "Ivanhr";
    private String passwprd = "Pa$$w0rd" ;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if(this.username.equals(username)) {
            return new User(this.username, this.passwprd , new ArrayList<>());
        }else{
            return null;
        }
        
    }

    
    
}
