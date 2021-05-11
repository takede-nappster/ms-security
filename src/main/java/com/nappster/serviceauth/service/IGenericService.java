/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nappster.serviceauth.service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author nappster
 */
public interface IGenericService<T, K> {
    
    T createOrUpdate(T t);
    
    void delete(K key);
    
    Optional<T> findById(K key);
    
    List<T> findAll();
    
}
