/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nappster.serviceauth.resource;

import com.nappster.serviceauth.dto.UserInput;
import com.nappster.serviceauth.dto.UserUpdateInput;
import com.nappster.serviceauth.entities.User;
import com.nappster.serviceauth.service.UserService;
import com.nappster.serviceauth.util.UtilsService;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nappster
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UtilsService utilsService;
    
    @Autowired
    private UserService userService;
    
    @Value("${sessionTimeout}")
    private String sessionTimeout;
    
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    Predicate<String> emptyString = s-> s==null || s.isEmpty();
    
    @PostMapping("/create")
    public ResponseEntity<User> create(@RequestBody UserInput input, @RequestHeader("Authorization") String token){
        
        if(input == null || emptyString.test(input.getEmail()) ||  emptyString.test(input.getLastname()) ){
            Map<String, Object> output = new HashMap<>();
            output.put("status", 400);
            output.put("message", "Invalid inputs");
            return new ResponseEntity(output, HttpStatus.OK);        
        }
        
        String[] credentials = utilsService.extractCredentials(token);
        
        if(credentials==null || credentials.length!=2){
            Map<String, Object> output = new HashMap<>();
            output.put("status", 400);
            output.put("message", "Invalid header");
            return new ResponseEntity(output, HttpStatus.BAD_REQUEST);
        }
        
        String username = credentials[0];
        String password = credentials[1];
        
        Optional<User> optional = userService.findByUsername(username);
        if(optional.isPresent()){
            Map<String, Object> output = new HashMap<>();
            output.put("status", 400);
            output.put("message", "User alredy exist");
            return new ResponseEntity(output, HttpStatus.BAD_REQUEST);
        }
        
        User user = input.getUser();
        
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        
        user = userService.createOrUpdate(user);
        
        return new ResponseEntity(user, HttpStatus.OK);
    }
    
    @PostMapping("/update")
    public ResponseEntity<User> update(@RequestBody UserUpdateInput input, @RequestHeader("Authorization") String token){
        
        if(input == null){
            Map<String, Object> output = new HashMap<>();
            output.put("status", 400);
            output.put("message", "Invalid inputs");
            return new ResponseEntity(output, HttpStatus.UNAUTHORIZED);        
        }
        
        String[] credentials = utilsService.extractCredentials(token);
        
        if(credentials==null || credentials.length!=2){
            Map<String, Object> output = new HashMap<>();
            output.put("status", 401);
            output.put("message", "Invalid header");
            return new ResponseEntity(output, HttpStatus.UNAUTHORIZED);
        }
        
        String username = credentials[0];
        String password = credentials[1];
        
        Optional<User> optional = userService.login(username, password);
        if(!optional.isPresent()){
            Map<String, Object> output = new HashMap<>();
            output.put("status", 404);
            output.put("message", "UserNotFound");
            return new ResponseEntity(output, HttpStatus.NOT_FOUND);
        }
        
        User user = optional.get();
        
        if(!emptyString.test(input.getEmail()))
            user.setEmail(input.getEmail());
        
        if(!emptyString.test(input.getFirstname()))
            user.setFirstname(username);
        
        if(!emptyString.test(input.getLastname()))
            user.setLastname(input.getLastname());
        
        if(!emptyString.test(input.getNewPassword()))
            user.setPassword(passwordEncoder.encode(input.getNewPassword()));
        
        user = userService.createOrUpdate(user);
        
        return new ResponseEntity(user, HttpStatus.OK);
    }
    
    @GetMapping("/login")
    public ResponseEntity<User> login(@RequestHeader("Authorization") String token){
        
        String[] credentials = utilsService.extractCredentials(token);
        
        if(credentials==null || credentials.length!=2){
            Map<String, Object> output = new HashMap<>();
            output.put("status", 401);
            output.put("message", "Invalid header");
            return new ResponseEntity(output, HttpStatus.UNAUTHORIZED);
        }
        
        String username = credentials[0];
        String password = credentials[1];
        
        Optional<User> optional = userService.findByUsername(username);
        if(!optional.isPresent()){
            Map<String, Object> output = new HashMap<>();
            output.put("status", 404);
            output.put("message", "User not found");
            return new ResponseEntity(output, HttpStatus.NOT_FOUND);
        }
        
        User user = optional.get();
        
        if(!passwordEncoder.matches(password, user.getPassword())){
            Map<String, Object> output = new HashMap<>();
            output.put("status", 401);
            output.put("message", "Bad credentials");
            return new ResponseEntity(output, HttpStatus.UNAUTHORIZED);
        }
        
        if(user.getLastConnexionDate() != null){
            Calendar cal = Calendar.getInstance();
            cal.setTime(user.getLastConnexionDate());
            Integer timeout = Integer.valueOf(sessionTimeout);
            cal.add(Calendar.MILLISECOND, timeout);
            Date exp = cal.getTime();
            if(exp.after(new Date())){
                Map<String, Object> output = new HashMap<>();
                output.put("status", 401);
                output.put("message", "User aready conected");
                return new ResponseEntity(output, HttpStatus.UNAUTHORIZED);
            }
        }
        
        user.setLastConnexionDate(new Date());
        user = userService.createOrUpdate(user);
        return new ResponseEntity(user, HttpStatus.OK);
    }
    
    @PostMapping("/delete/{username}")
    public ResponseEntity<User> delete(@PathVariable("username") String username){
        Optional<User> optional = userService.findByUsername(username);
        if(!optional.isPresent()){
            Map<String, Object> output = new HashMap<>();
            output.put("status", 404);
            output.put("message", "User not found");
            return new ResponseEntity(output, HttpStatus.OK);
        }
        User user = optional.get();
        user.setIsDeleted(Boolean.TRUE);
        user = userService.createOrUpdate(user);
        return new ResponseEntity(user, HttpStatus.OK);
    }
    
    @GetMapping("/find/{username}")
    public ResponseEntity<User> findByUsername(@PathVariable("username") String username){
       Optional<User> optional = userService.findByUsername(username);
        if(!optional.isPresent()){
            Map<String, Object> output = new HashMap<>();
            output.put("status", 404);
            output.put("message", "User not found");
            return new ResponseEntity(output, HttpStatus.OK);
        }
        User user = optional.get();
        return new ResponseEntity(user, HttpStatus.OK);
    }
    
    @GetMapping("/findAll")
    public ResponseEntity<List<User>> findAll(){
        List<User> list = userService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }
    
    @GetMapping("/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") String token){
        Optional<User> optional = userService.login(token);
        if(!optional.isPresent()){
            Map<String, Object> output = new HashMap<>();
            output.put("status", 401);
            output.put("message", "Invalid header");
            return new ResponseEntity(output, HttpStatus.UNAUTHORIZED);
        }
        User user = optional.get();
        user.setLastConnexionDate(new Date());
        user = userService.createOrUpdate(user);
        return new ResponseEntity(HttpStatus.OK);
    }
    
}
