package com.teamip.heyhello.global.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.yml")
class JasyptConfigTest{

    @Value("${jasypt.encryptor.password}")
    private String key;

    @Test
    public void jasypt_encrypt_test() {

    }

    private String encoding(String value) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        encryptor.setPassword(key);
        encryptor.setIvGenerator(new RandomIvGenerator());
        return encryptor.encrypt(value);
    }
}