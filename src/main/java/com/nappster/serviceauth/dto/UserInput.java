/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nappster.serviceauth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nappster.serviceauth.entities.User;
import javax.persistence.Column;
import lombok.Data;

/**
 *
 * @author nappster
 */
@Data
public class UserInput {
    
    private String firstname;
    
    private String lastname;
    
    private String email;
    
    @JsonIgnore
    public User getUser(){
        User user = new User();
        user.setEmail(email);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        return user;
    }
    
}
