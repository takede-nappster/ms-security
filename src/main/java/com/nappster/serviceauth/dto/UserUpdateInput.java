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
public class UserUpdateInput {
    
    private String firstname;
    
    private String lastname;
    
    private String email;
    
    private String newPassword;
    
}
