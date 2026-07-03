package com.example.birthday.calendarservice.security;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class TokenCipherTest{
    @Test void encryptsAndDecryptsWithoutStoringPlaintext(){
        var cipher=new TokenCipher("test-key");String encrypted=cipher.encrypt("secret-token");
        assertNotEquals("secret-token",encrypted);assertEquals("secret-token",cipher.decrypt(encrypted));
    }
}
