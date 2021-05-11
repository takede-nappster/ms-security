/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nappster.serviceauth.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 *
 * @author nappster
 */
public interface GenericDAOFilter<T> extends JpaSpecificationExecutor<T>{
    
    public default List<T> filter(){
        //return this.findAll(s, sort);
        return null;
    }
    
}
