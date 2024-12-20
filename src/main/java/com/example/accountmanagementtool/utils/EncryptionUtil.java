package com.example.accountmanagementtool.utils;

import com.example.accountmanagementtool.bean.UserInfoBean;
import com.example.accountmanagementtool.enumeration.CryptoAlgorithmEnum;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 加密工具
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2024/12/16   11:00
 * @Version 1.0
 */
public class EncryptionUtil {


    /**
     * 加解密类型匹配
     * @param type               1=加密 2=解密
     * @param password           密码
     * @param random             戳
     * @param encryptionMethod   加密方式
     * @return
     * @throws Exception
     */
    public static String encryptionMethod(Integer type,String password,String random,String encryptionMethod) throws Exception {
        CryptoAlgorithmEnum cryptoAlgorithmEnum = CryptoAlgorithmEnum.fromAlgorithmName(encryptionMethod);
        String msg = "error";
        switch (cryptoAlgorithmEnum){
            case AES_ECB_PKCS5PADDING -> {
                if (type == 1){
                    msg = encryptAES(password, random);
                }else if(type == 2){
                    msg = decryptAES(password,random);
                }
                break;
            }
            default ->{
                break;

            }
        }
        return msg;
    }

    /**
     * 对字符串进行MD5加密
     * @param pwd 输入的字符串
     * @return 加密后的字符串
     */
    public static String md5(String pwd) {
        try {
            // 获取MD5实例
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 将字符串转换为字节数组
            byte[] messageDigest = md.digest(pwd.getBytes());
            // 转换为十六进制
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            // 返回加密后的字符串
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * AES/ECB/PKCS5Padding 加密方法
     * @param password
     * @return
     * @throws Exception
     */
    public static String encryptAES(String password,String random) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(random));
        byte[] contentByte = password.getBytes("utf-8");
        byte[] encryptByte = cipher.doFinal(contentByte);
        return Base64.getEncoder().encodeToString(encryptByte);
    }

    private static Key getSecretKey(String random) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(random.getBytes());
        keyGenerator.init(128, secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();
        return new SecretKeySpec(secretKey.getEncoded(), "AES");
    }
    /**
     * AES/ECB/PKCS5Padding 解密方法
     * @param encryptedText     密文
     * @param random            加密随机值
     * @return
     * @throws Exception
     */
    public static String decryptAES(String encryptedText,String random) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(random));
        byte[] decode = Base64.getDecoder().decode(encryptedText);
        byte[] decryptByte = cipher.doFinal(decode);
        return new String(decryptByte, "utf-8");
    }
}
