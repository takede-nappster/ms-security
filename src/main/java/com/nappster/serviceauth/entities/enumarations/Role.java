/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nappster.serviceauth.entities.enumarations;

/**
 *
 * @author Nappster-SPRINT-PAY
 */
public enum Role {
    
    USER("USER"), ADMIN("ADMIN");
    
    private String name;
    
    private Role(String name){
        this.name = name;
    }
}
