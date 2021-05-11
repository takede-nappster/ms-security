//package com.nappster.serviceauth.util;
//
//import java.util.ArrayList;
//import org.springframework.beans.factory.annotation.Value;
//
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AuthUserDetailsService implements UserDetailsService {
//
//    @Value("${spring.auth-username}")
//    private String authUsername;
//
//    @Value("${spring.auth-password}")
//    private String authPassword;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        if (authUsername.equals(username)) {
//            return new User(authUsername, authPassword,
//                    new ArrayList<>());
//        } else {
//            return null;
//        }
//    }
//}
