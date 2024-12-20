package com.example.accountmanagementtool.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.SecureRandom;

/**
 * 文件管理类
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2024/12/18   11:25
 * @Version 1.0
 */
public class FileManageUtil {

    private static final String DIRECTORY_PATH = "src/main/resources/dataFile/";
    //文件名
    private static final String FILEPATH =  DIRECTORY_PATH + "userDataPwdExample.xlsx";


    /**
     * 文件加密
     */
    public static void fileEncryptor(){
        try {
            // 生成密钥
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128, SecureRandom.getInstanceStrong());
            SecretKey secretKey = keyGen.generateKey();

            // 加密文件
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            FileInputStream fis = new FileInputStream(new File(FILEPATH));
            FileOutputStream fos = new FileOutputStream(new File(FILEPATH));
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    fos.write(output);
                }
            }
            byte[] output = cipher.doFinal();
            if (output != null) {
                fos.write(output);
            }
            fis.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件解密
     */
    public static void fileDecryptor(){
        // 生成密钥 不能使用
        // 密钥，需要与加密时使用的密钥相同
        byte[] keyBytes = {123}; // 这里需要填入密钥字节数组
        try {
            // 使用相同的密钥
            Key key = new SecretKeySpec(keyBytes, "AES");

            // 解密文件
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            FileInputStream fis = new FileInputStream(new File(FILEPATH));
            FileOutputStream fos = new FileOutputStream(new File(FILEPATH));
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    fos.write(output);
                }
            }
            byte[] output = cipher.doFinal();
            if (output != null) {
                fos.write(output);
            }
            fis.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
