/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nappster.serviceauth.entities;

import com.nappster.serviceauth.entities.enumarations.Role;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

/**
 *
 * @author nappster
 */
@Data
@Entity
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column
    private String firstname;
    
    @Column
    private String lastname;
    
    @Column(unique = true)
    private String username;
    
    @Column(unique = true)
    private String email;

    @Column
    private String password;
    
    @Column
    private Date lastConnexionDate;
    
    @Enumerated(EnumType.STRING)
    @Column
    private Role role;
    
    @Column
    private Boolean isDeleted = Boolean.FALSE;
    
}
