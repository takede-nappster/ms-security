package com.nappster.serviceauth.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import org.springframework.web.client.RestTemplate;

/**
 * @author Paulinlenasaein
 *
 */
@Service
public class UtilsService {
    
    private RestTemplate rest;
    
    private HttpHeaders headers;
    
    private BiFunction<String,String[],Boolean> testStringExistIn = (String s , String[] list )-> Arrays.asList(list).stream().anyMatch(e -> e.trim().equalsIgnoreCase(s.trim()));

    
    public Long generateCodeLong() {
        double code = Math.floor(Math.random() * 100000);
        return (new Double(code)).longValue();
    }
    
    public String generateDigitsStringCode(int length){
        String chars = "1234567890";
        String pass = "";
        for (int x = 0; x < length; x++) {
            int i = (int) Math.floor(Math.random() * chars.length());
            pass += chars.charAt(i);
        }
        return pass;
    }

    public String generateCodeString(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String pass = "";
        for (int x = 0; x < length; x++) {
            int i = (int) Math.floor(Math.random() * 62);
            pass += chars.charAt(i);
        }
        return pass;
    }

    public Date convertStringToDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if (!date.isEmpty()) {
            try {
                return formatter.parse(date);
            } catch (ParseException ex) {
                Logger.getLogger(UtilsService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }

    public Date convertStringToDateTime(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!date.isEmpty()) {
            try {
                return formatter.parse(date);
            } catch (ParseException ex) {
                Logger.getLogger(UtilsService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }

    public Boolean convertStringToBoolean(String bool) {
        if (!bool.isEmpty()) {
            return Boolean.parseBoolean(bool);
        }

        return null;
    }

    public String[] extractCredentials(String authString) {

        String[] credentials = null;
        // Header is in the format "Basic 5tyc0uiDat4"
        // We need to extract data before decoding it back to original string
        if (!authString.isEmpty()) {
            String[] authParts = authString.split("\\s+");
            String authInfo = authParts[1];
            // Decode the data back to original string
            byte[] decode = Base64.getDecoder().decode(authInfo.getBytes());
            String decodeString = new String(decode);

            credentials = decodeString.split(":");
        }

        return credentials;
    }

    public HttpHeaders doHttpBasic(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        headers.set("Authorization", authHeader);
        return headers;
    }
    
    /**
     * 
     * @return HttpHeaders
     */
    public HttpHeaders doHttpToken(String token){
        HttpHeaders headers = new HttpHeaders();
        String authHeader = "Bearer " + token;
        headers.set("Authorization", authHeader);
        return headers;
    }

    
    public Map<String, String> keyPairGenerator() {
        
        try {
            //Creating KeyPair generator object
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");
            
            //Initializing the KeyPairGenerator
            keyPairGen.initialize(512);
            
            //Generate the pair of keys
            KeyPair pair = keyPairGen.generateKeyPair();
            
            //Getting the private key from the key pair
            PrivateKey privateKey = pair.getPrivate();
            
            //Getting the public key from the key pair
            PublicKey publicKey = pair.getPublic();
            
            Map<String, String> result = new HashMap();
            result.put("private", org.apache.tomcat.util.codec.binary.Base64.encodeBase64String(privateKey.getEncoded()));
            result.put("public", org.apache.tomcat.util.codec.binary.Base64.encodeBase64String(publicKey.getEncoded()));
            
            return result;
            
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UtilsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public List<Map<String, Object>> mapListExcluding(Object input, String [] exclude){
        List<Object> list = (List<Object>)input;
        List<Map<String, Object>> result = new ArrayList();
        list.forEach(obeject -> result.add(mapExcluding(obeject, exclude)));
        return result;
    }
    
    public List<Map<String, Object>> mapListOnly(List<Object> list, String [] inculding){
        List<Map<String, Object>> result = new ArrayList();
        list.forEach(obeject -> result.add(mapOnly(obeject, inculding)));
        return result;
    }   
    
    public Map<String, Object> mapExcluding(Object data, Class type, String [] exclude){
        Map<String, Object> result = new HashMap();
        Field[] classFields = type.getDeclaredFields();
        List<Field> fields = new ArrayList();
        fields.addAll(Arrays.asList(classFields));
        Class internalTyp = type.getSuperclass();
        
        while (!internalTyp.getName().equals(Object.class.getName())) {            
            Field[] superFields = internalTyp.getDeclaredFields();
            fields.addAll(Arrays.asList(classFields));
            fields.addAll(Arrays.asList(superFields));   
            internalTyp = internalTyp.getSuperclass();
        }
       
        for (Field field : fields) {
            if(!testStringExistIn.apply(field.getName(), exclude)){
                try {
                    field.setAccessible(true);
                    PropertyDescriptor pd = new PropertyDescriptor(field.getName(), type);
                    String methodName = pd.getReadMethod().getName();
                    result.put(field.getName(), data.getClass().getMethod(methodName).invoke(data));
                } catch (Exception ex) {
                    Logger.getLogger(UtilsService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return result;
    }
    
    public Map<String, Object> mapExcluding(Object data, String [] exclude){
        Map<String, Object> result = new HashMap();
        Class type = data.getClass();
        result = mapExcluding(data, type, exclude);
        return result;
    }
    
    public Map<String, Object> map(Object data, Class type){
        String[] exclude = {};
        return mapExcluding(data, type, exclude);
    }
    
    public Map<String, Object> map(Object data){
        Class type = data.getClass();
        String[] exclude = {};
        return mapExcluding(data, type, exclude);
    }
    
    public Map<String, Object> mapOnly(Object data, String [] inculding){
        Map<String, Object> result = new HashMap();
        Class type = data.getClass();
        result = mapOnly(data, type,inculding);
        return result; 
    }    
    
    public Map<String, Object> mapOnly(Object data, Class type, String [] inculding){
        System.out.println("=========== class name" + type.getSuperclass());
        Map<String, Object> result = new HashMap();
        Field[] classFields = type.getDeclaredFields();
        List<Field> fields = new ArrayList();
        fields.addAll(Arrays.asList(classFields));
        Class internalTyp = type.getSuperclass();
        
        while (!internalTyp.getName().equals(Object.class.getName())) {            
            Field[] superFields = internalTyp.getDeclaredFields();
            fields.addAll(Arrays.asList(classFields));
            fields.addAll(Arrays.asList(superFields));   
            internalTyp = internalTyp.getSuperclass();
        }
       
        for (Field field : fields) {
            System.out.println("=============== Name + " + field.getName());
            if(testStringExistIn.apply(field.getName(), inculding)){
                try {
                    field.setAccessible(true);
                    PropertyDescriptor pd = new PropertyDescriptor(field.getName(), type);
                    String methodName = pd.getReadMethod().getName();
                    result.put(field.getName(), data.getClass().getMethod(methodName).invoke(data));
                } catch (Exception ex) {
                    Logger.getLogger(UtilsService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return result;
    }
    
}
