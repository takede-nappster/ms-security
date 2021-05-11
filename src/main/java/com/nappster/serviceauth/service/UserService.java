/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nappster.serviceauth.service;

import com.nappster.serviceauth.entities.User;
import java.util.Optional;

/**
 *
 * @author nappster
 */
public interface UserService extends IGenericService<User, Long>{

    public Optional<User> findByUsername(String username);

    public Optional<User> login(String username, String password);
    
    public Optional<User> login(String token);
    
}
