/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nappster.serviceauth.dao;

import com.nappster.serviceauth.entities.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author nappster
 */
public interface UserDao extends JpaRepository<User, Long> {

    public Optional<User> findByUsername(String username);

    public Optional<User> findByUsernameAndIsDeletedFalse(String username);

    public List<User> findByIsDeletedFalse();
    
}
