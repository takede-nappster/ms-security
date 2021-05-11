package com.nappster.serviceauth.security;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESImpl {

    private static final String ALGO = "AES";

    /**
     * Encrypt a string with AES algorithm.
     *
     * @param data is a string
     * @param keyVal encryption key
     * @return the encrypted string
     * @throws java.lang.Exception
     */
    public static String encrypt(String data, String keyVal) throws Exception {
        Key key = generateKey(keyVal);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encVal);
    }

    /**
     * Decrypt a string with AES algorithm.
     *
     * @param encryptedData is a string
     * @param keyVal encryption key
     * @return the decrypted string
     * @throws java.lang.Exception
     */
    public static String decrypt(String encryptedData, String keyVal) throws Exception {
        Key key = generateKey(keyVal);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        return new String(decValue);
    }

    /**
     * Generate a new encryption key.
     */
    private static Key generateKey(String keyVal) throws Exception {
        return new SecretKeySpec(keyVal.getBytes(), ALGO);
    }

}
