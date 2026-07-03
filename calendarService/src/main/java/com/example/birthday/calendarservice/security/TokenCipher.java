package com.example.birthday.calendarservice.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.*;

@Component
public class TokenCipher {
    private final SecretKey key;
    private final SecureRandom random = new SecureRandom();
    public TokenCipher(@Value("${calendar.encryption-key}") String value) {
        try {
            byte[] digest=MessageDigest.getInstance("SHA-256")
                    .digest(value.getBytes(StandardCharsets.UTF_8));
            key=new SecretKeySpec(digest,"AES");
        } catch (GeneralSecurityException ex) { throw new IllegalStateException(ex); }
    }
    public String encrypt(String plain) {
        try {
            byte[] iv=new byte[12]; random.nextBytes(iv);
            Cipher cipher=Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE,key,new GCMParameterSpec(128,iv));
            byte[] encrypted=cipher.doFinal(plain.getBytes(StandardCharsets.UTF_8));
            byte[] result=new byte[iv.length+encrypted.length];
            System.arraycopy(iv,0,result,0,iv.length);
            System.arraycopy(encrypted,0,result,iv.length,encrypted.length);
            return Base64.getEncoder().encodeToString(result);
        } catch (GeneralSecurityException ex) { throw new IllegalStateException("Token encryption failed",ex); }
    }
    public String decrypt(String encoded) {
        try {
            byte[] value=Base64.getDecoder().decode(encoded);
            Cipher cipher=Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE,key,new GCMParameterSpec(128,value,0,12));
            return new String(cipher.doFinal(value,12,value.length-12),StandardCharsets.UTF_8);
        } catch (GeneralSecurityException ex) { throw new IllegalStateException("Token decryption failed",ex); }
    }
}
