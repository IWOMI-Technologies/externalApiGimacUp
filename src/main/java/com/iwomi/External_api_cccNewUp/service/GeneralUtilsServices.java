/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iwomi.External_api_cccNewUp.service;

import com.iwomi.External_api_cccNewUp.model.Pwd;
import com.iwomi.External_api_cccNewUp.repository.PwdRepository;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import static org.aspectj.asm.internal.ProgramElement.trim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author user
 */
@Service
public class GeneralUtilsServices {
    
    @Autowired
    PwdRepository pwdRepository;
    
    public String decrypt_data(String encData)
        throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        
        Pwd pwd = pwdRepository.findByAcscd("0216", "0");
        byte[] decoder = Base64.getDecoder().decode(trim(pwd.getPass().toString()));
        String v = new String(decoder);
        final String key = trim(v);
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        System.out.println("Base64 decoded: "
                + java.util.Base64.getDecoder().decode(encData.getBytes()).length);
        byte[] original = cipher
                .doFinal(java.util.Base64.getDecoder().decode(encData.getBytes()));
        return new String(original).trim();
    }

    public String encrypt_data(String data)
            throws Exception {
        Pwd pwd = pwdRepository.findByAcscd("0216", "0");
        byte[] decoder = Base64.getDecoder().decode(trim(pwd.getPass().toString()));
        String v = new String(decoder);
        final String key = trim(v);
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        System.out.println("Base64 encoded: "
                + java.util.Base64.getEncoder().encode(data.getBytes()).length);

        byte[] original = java.util.Base64.getEncoder().encode(cipher.doFinal(data.getBytes()));
        return new String(original);
    }
    
}

