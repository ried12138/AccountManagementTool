package com.example.accountmanagementtool.init;

import com.example.accountmanagementtool.config.ExcelManager;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Map;

/**
 * 初始化数据库
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2024/12/12   17:22
 * @Version 1.0
 */
public class DataLoader {
    private static volatile DataLoader instance;
    private static Map<String, Sheet> sheetMap;

    private DataLoader() {
        sheetMap = loadInitialData();
    }

    public static DataLoader getInstance() {
        if (instance == null) {
            synchronized (DataLoader.class) {
                if (instance == null) {
                    instance = new DataLoader();
                }
            }
        }
        return instance;
    }

    private Map<String, Sheet> loadInitialData() {
        // 实现数据加载逻辑
        return ExcelManager.initExcelFile();
    }
}