package com.example.accountmanagementtool.config;

import com.example.accountmanagementtool.bean.UserInfoBean;
import com.example.accountmanagementtool.utils.EncryptionUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * 校验/创建本地数据库excel
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2024/12/12   16:08
 * @Version 1.0
 */
public class ExcelManager {

    private static final String DIRECTORY_PATH = "src/main/resources/dataFile/";
    //文件名
    private static final String FILEPATH =  DIRECTORY_PATH + "userDataPwdExample.xlsx";
    //工作簿名
    private static final String SHEETNAME = "sheet1";
    //表头信息
    private static final String[] HEADERS = {"账号","密码","邮箱","手机号","问题","答案","加密方式","更新时间","新增时间"};
    //加密方式名称
    private static final String[]  ENCRYPTIONMETHOD = {""};
    //excel All数据
    public static Map<String, Sheet> sheetMap = null;
    //获取所有工作簿的名称
    public static Set<String> platformName = new TreeSet<>();


    /**
     * 初始化excel文件
     */
    public static Map<String, Sheet> initExcelFile() {
        // 确保dataFile文件夹存在
        ensureDirectoryExists();
        Workbook workbook = readExcelToMap();
        sheetMap = new HashMap<>();
        // 遍历所有的工作表
         for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            sheetMap.put(sheet.getSheetName(), sheet);
        }
        platformName.addAll(sheetMap.keySet());
        return sheetMap;
    }

    public static Workbook readExcelToMap(){
        Workbook workbook = null;
        try {
            // 检查文件是否存在
            if (Files.exists(Paths.get(FILEPATH))) {
                System.out.println("File already exists. Reading the file.");
                workbook = readExcelToMap(FILEPATH);
                if (workbook.getSheet(SHEETNAME) != null && workbook.getNumberOfSheets() > 0) {
                    int sheetIndex = workbook.getSheetIndex(workbook.getSheet(SHEETNAME));
                    workbook.removeSheetAt(sheetIndex);
                }
            } else {
                System.out.println("File does not exist. Creating a new file.");
                // 初始化excel文件
                createNewExcelFile(SHEETNAME, HEADERS, FILEPATH);
                workbook = readExcelToMap(FILEPATH);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    FileOutputStream fos = new FileOutputStream(FILEPATH);
                    workbook.write(fos);
                    workbook.close();
                } catch (IOException e) {
                    System.err.println("Error closing the workbook: " + e.getMessage());
                }
            }
        }
        return workbook;
    }
    /**
     * 校验本地是否已经存在excel
     */
    private static void ensureDirectoryExists() {
        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private static void createNewExcelFile(String sheetName, String[] headers, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            createHeader(workbook,sheetName,headers);
            // 将工作簿写入文件
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建表头
     * @param workbook
     * @param sheetName
     * @param headers
     */
    private static Sheet createHeader(Workbook workbook, String sheetName, String[] headers){
        Sheet sheet = workbook.createSheet(sheetName);
        // 创建一个单元格样式，用于设置为字符串格式
        CellStyle stringCellStyle = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        stringCellStyle.setDataFormat(format.getFormat("@")); // 设置为文本格式
        // 创建表头
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(stringCellStyle); // 应用字符串格式样式
        }
        return sheet;
    }
    /**
     * 读取excel文件
     * @param filePath
     */
    public static Workbook readExcelToMap(String filePath) {
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(new FileInputStream(new File(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }

    public static List<UserInfoBean> getRows(Sheet rows){
        List<UserInfoBean> rowList = new ArrayList<>();
        for (Row row : rows) {
            Cell cell = row.getCell(1);
            if (cell.getStringCellValue().equals("密码")){
                continue;
            }
            //UserInfoBean(String platfrom, String account, String password, String email, String phoneNumber, String question, String answer, String encryptionMethod, Date updateTime)
            UserInfoBean userInfoBean = new UserInfoBean(rows.getSheetName(),(String) getCellValue(row.getCell(0)), (String) getCellValue(row.getCell(1)),
                    (String) getCellValue(row.getCell(2)), (String) getCellValue(row.getCell(3)),
                    (String) getCellValue(row.getCell(4)), (String) getCellValue(row.getCell(5)),
                    (String) getCellValue(row.getCell(6)), (String) getCellValue(row.getCell(7)),(String) getCellValue(row.getCell(8)));
            rowList.add(userInfoBean);
        }
        return rowList;
    }

    /**
     * 每个单元格的数据格式处理
     * @param cell
     * @return
     */
    private static Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                // 如果单元格是公式，获取公式计算后的值
                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                    return String.valueOf(cell.getNumericCellValue());
                } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                    return cell.getStringCellValue();
                }
                // 其他情况，返回公式本身
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    /**
     * 更新账号，和添加账号
     * @param type  1=添加 2=更新
     * @param userInfoBean
     * @return
     * @throws Exception
     */
    public static boolean saveDate(Integer type,UserInfoBean userInfoBean) throws Exception {
        Workbook workbook = null;
        FileInputStream inputStream = null;
        if (Files.exists(Paths.get(FILEPATH))) {
            try {
            inputStream = new FileInputStream(FILEPATH);
            workbook = new XSSFWorkbook(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Sheet sheet = workbook.getSheet(userInfoBean.getPlatfrom());
        if (sheet == null) {
            // 如果Sheet不存在，可以选择创建一个新的Sheet或处理其他逻辑
            sheet = createHeader(workbook, userInfoBean.getPlatfrom(), HEADERS);
        }
        Row row = null;
        if (type == 1){
            // 创建行对象
            row = sheet.createRow(sheet.getLastRowNum()+1); // 创建第一行，如果需要在特定位置插入，可以指定行号
            //对密码进行加密
            String pwd = EncryptionUtil.encryptionMethod(1,userInfoBean.getPassword(), userInfoBean.getNewAddTime(), userInfoBean.getEncryptionMethod());
            if (pwd.equals("error")){
                return false;
            }
            row.createCell(0).setCellValue(userInfoBean.getAccount().toString());
            row.createCell(1).setCellValue(pwd.toString());
            // 创建单元格对象，并设置值
            row.createCell(2).setCellValue(userInfoBean.getEmail().toString());
            row.createCell(3).setCellValue(userInfoBean.getPhoneNumber().toString());
            row.createCell(4).setCellValue(userInfoBean.getQuestion().toString());
            row.createCell(5).setCellValue(userInfoBean.getAnswer().toString());
            row.createCell(6).setCellValue(userInfoBean.getEncryptionMethod().toString());
            row.createCell(7).setCellValue(userInfoBean.getUpdateTime().toString());
            row.createCell(8).setCellValue(userInfoBean.getNewAddTime().toString());
        }else if(type == 2){
            // 从最后一行开始向上遍历，这样在删除行时不会影响未检查的行的索引
            for (int rowNum = sheet.getLastRowNum(); rowNum >= 0; rowNum--) {
                row = sheet.getRow(rowNum);
                if (row != null) {
                    // 获取第一列的单元格
                    Cell firstCell = row.getCell(0);
                    if (firstCell != null) {
                        // 检查单元格是否包含特定值
                        if (firstCell.getCellType() == CellType.STRING && firstCell.getStringCellValue().equals(userInfoBean.getAccount())) {
                            // 如果找到匹配的值则修改这一样数据
                            break;
                        }
                    }
                }
            }
            if (!userInfoBean.getPassword().equals("")){
                String pwd = EncryptionUtil.encryptionMethod(1,userInfoBean.getPassword(), userInfoBean.getNewAddTime(), userInfoBean.getEncryptionMethod());
                row.getCell(1).setCellValue(pwd.toString());
            }
            row.getCell(0).setCellValue(userInfoBean.getAccount().toString());
            // 创建单元格对象，并设置值
            row.getCell(2).setCellValue(userInfoBean.getEmail().toString());
            row.getCell(3).setCellValue(userInfoBean.getPhoneNumber().toString());
            row.getCell(4).setCellValue(userInfoBean.getQuestion().toString());
            row.getCell(5).setCellValue(userInfoBean.getAnswer().toString());
            row.getCell(6).setCellValue(userInfoBean.getEncryptionMethod().toString());
            row.getCell(7).setCellValue(userInfoBean.getUpdateTime().toString());
            row.getCell(8).setCellValue(userInfoBean.getNewAddTime().toString());
        }

        // 将更改写入文件
        try (FileOutputStream fileOut = new FileOutputStream(FILEPATH)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        initExcelFile();
        return true;
    }

    /**
     * 删除数据
     * @param userInfoBean
     * @return
     * @throws Exception
     */
    public static boolean deleteData(UserInfoBean userInfoBean){
        boolean falg = false;
        try {
            Workbook workbook = null;
            FileInputStream inputStream = null;
            if (Files.exists(Paths.get(FILEPATH))) {
                inputStream = new FileInputStream(FILEPATH);
                workbook = new XSSFWorkbook(inputStream);
            }
            Sheet sheet = workbook.getSheet(userInfoBean.getPlatfrom());
            // 从最后一行开始向上遍历，这样在删除行时不会影响未检查的行的索引
            for (int rowNum = sheet.getLastRowNum(); rowNum >= 0; rowNum--) {
                Row row = sheet.getRow(rowNum);
                if (row != null) {
                    // 获取第一列的单元格
                    Cell firstCell = row.getCell(0);
                    if (firstCell != null) {
                        // 检查单元格是否包含特定值
                        if (firstCell.getCellType() == CellType.STRING && firstCell.getStringCellValue().equals(userInfoBean.getAccount())) {
                            // 如果找到匹配的值，则删除这一行
                            sheet.removeRow(row);
                            falg = true;
                            break;
                        }
                    }
                }
            }
            // 将修改后的工作簿写回文件
            FileOutputStream out = new FileOutputStream(FILEPATH);
            workbook.write(out);
            out.close();
            inputStream.close();
            // 关闭工作簿
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            initExcelFile();
        }
        return falg;
    }

    /**
     * 校验账号是否存在
     * @param platform
     * @param userName
     * @return
     */
    public static boolean checkUserInfoOfIn(String platform,String userName){
        Sheet sheet = sheetMap.get(platform);
        boolean falg = false;
        if (sheet != null){
            List<UserInfoBean> userInfo = getRows(sheet);
            for (UserInfoBean userInfoBean : userInfo) {
                if (userInfoBean.getAccount().equals(userName)){
                    falg = true;
                    break;
                }
            }
        }
        return falg;
    }
}