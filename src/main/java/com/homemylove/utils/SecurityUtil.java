package com.homemylove.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SecurityUtil {

    // 生成随机的salt值
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        StringBuilder saltHex = new StringBuilder();
        for (byte b : salt) {
            saltHex.append(String.format("%02x", b));
        }
        return saltHex.toString();
    }

    // 对密码进行SHA-256加密
    public static String encryptPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String saltedPassword = password + salt;

            byte[] hashBytes = digest.digest(saltedPassword.getBytes());

            StringBuilder hashHex = new StringBuilder();
            for (byte b : hashBytes) {
                hashHex.append(String.format("%02x", b));
            }

            return hashHex.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}

