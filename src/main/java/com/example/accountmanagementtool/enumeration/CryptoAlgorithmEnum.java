package com.example.accountmanagementtool.enumeration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2024/12/16   13:28
 * @Version 1.0
 */
public enum CryptoAlgorithmEnum {
//    MD5("MD5"),
//    SHA_1("SHA-1"),
//    SHA_256("SHA-256"),
    AES_ECB_PKCS5PADDING("AES/ECB/PKCS5Padding");

    private final String algorithmName;

    // 构造函数
    CryptoAlgorithmEnum(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    // 获取算法名称
    public String getAlgorithmName() {
        return algorithmName;
    }

    // 根据算法名称获取枚举值
    public static CryptoAlgorithmEnum fromAlgorithmName(String algorithmName) {
        for (CryptoAlgorithmEnum algorithm : values()) {
            if (algorithm.getAlgorithmName().equalsIgnoreCase(algorithmName)) {
                return algorithm;
            }
        }
        throw new IllegalArgumentException("No constant with algorithmName " + algorithmName + " found");
    }
    // 将枚举转换成List
    public static List<String> toList() {
        return Arrays.stream(CryptoAlgorithmEnum.values())
                .map(CryptoAlgorithmEnum::getAlgorithmName)
                .collect(Collectors.toList());
    }

}
