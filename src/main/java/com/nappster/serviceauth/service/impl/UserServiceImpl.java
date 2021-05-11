/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nappster.serviceauth.service.impl;

import com.nappster.serviceauth.dao.UserDao;
import com.nappster.serviceauth.entities.User;
import com.nappster.serviceauth.service.UserService;
import com.nappster.serviceauth.util.UtilsService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nappster
 */
@Service
@Transactional
public class UserServiceImpl implements UserService{
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private UtilsService utilsService;
    
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Override
    public User createOrUpdate(User t) {
        return userDao.save(t);
    }

    @Override
    public void delete(Long key) {
        userDao.deleteById(key);
    }

    @Override
    public Optional<User> findById(Long key) {
        return userDao.findById(key);
    }

    @Override
    public List<User> findAll() {
        return userDao.findByIsDeletedFalse();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsernameAndIsDeletedFalse(username);
    }

    @Override
    public Optional<User> login(String username, String password) {
        
        Optional<User> optional = this.findByUsername(username);
        if(optional.isPresent()){
            User user = optional.get();
            if(passwordEncoder.matches(password, user.getPassword()))
                return optional;
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> login(String token) {
        String[] credentials = utilsService.extractCredentials(token);
        if(credentials!=null || credentials.length==2){
            String username = credentials[0];
            String password = credentials[1];
            return this.login(username, password);
        }
        return Optional.empty();
    }
    
    
    
}
