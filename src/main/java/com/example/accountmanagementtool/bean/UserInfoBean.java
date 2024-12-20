package com.example.accountmanagementtool.bean;

import com.example.accountmanagementtool.utils.DateConverterUtil;
import lombok.Data;
import java.util.Date;

@Data
public class UserInfoBean {

    //所属平台
    private String platfrom;
    // 账号
    private String account;
    // 密码
    private String password;
    // 邮箱
    private String email;
    // 手机号
    private String phoneNumber;
    // 安全问题
    private String question;
    // 安全问题答案
    private String answer;
    // 加密方式
    private String encryptionMethod;
    // 更新时间
    private String updateTime;
    //新增时间
    private String newAddTime;


    public UserInfoBean() {
    }

    public UserInfoBean(String platfrom,String account, String password,String encryptionMethod,Date updateTime) {
        this.account = account;
        this.password = password;
        this.updateTime = DateConverterUtil.dateToString(updateTime);
    }

    public UserInfoBean(String platfrom, String account, String password, String email, String phoneNumber, String question, String answer, String encryptionMethod, String updateTime,String newAddTime) {
        this.platfrom = platfrom;
        this.account = account;
        this.password = password;
        if (email != null){
            this.email = email;
        }
        if (phoneNumber != null){
            this.phoneNumber = phoneNumber;
        }
        if (question != null){
            this.question = question;
        }
        if (answer != null){
            this.answer = answer;
        }
        this.encryptionMethod = encryptionMethod;
        this.updateTime = updateTime;
        this.newAddTime = newAddTime;
    }

    public void setPlatfrom(String platfrom) {
        this.platfrom = platfrom;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setEncryptionMethod(String encryptionMethod) {
        this.encryptionMethod = encryptionMethod;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setNewAddTime(String newAddTime) {
        this.newAddTime = newAddTime;
    }

    public String getPlatfrom() {
        return platfrom;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getEncryptionMethod() {
        return encryptionMethod;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getNewAddTime() {
        return newAddTime;
    }
}
