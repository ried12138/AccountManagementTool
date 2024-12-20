package com.example.accountmanagementtool.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间管理转换工具
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2024/12/16   11:48
 * @Version 1.0
 */
public class DateConverterUtil {

    // 定义日期格式
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // 将Date转换为String
    public static String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }

    // 将String转换为Date
    public static Date stringToDate(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.parse(dateString);
    }
    // 将日期字符串转换为Long（毫秒）
    public static long dateStringToLong(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Date date = sdf.parse(dateString);
        return date.getTime();
    }
}
