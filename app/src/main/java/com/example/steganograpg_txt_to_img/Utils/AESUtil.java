package com.example.steganograpg_txt_to_img.Utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public  class AESUtil {
    private static final String IV = "fedcba9876543210"; // 16-byte initialization vector

    public static String encrypt(String input, String key) {
        try {
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            byte[] encrypted = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(byte[] encrypted, String key) {
        try {
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encrypted));
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}