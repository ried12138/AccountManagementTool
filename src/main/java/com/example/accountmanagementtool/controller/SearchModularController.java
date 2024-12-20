package com.example.accountmanagementtool.controller;

import com.example.accountmanagementtool.bean.UserInfoBean;
import com.example.accountmanagementtool.config.ExcelManager;
import com.example.accountmanagementtool.enumeration.CryptoAlgorithmEnum;
import com.example.accountmanagementtool.utils.DateConverterUtil;
import com.example.accountmanagementtool.utils.EncryptionUtil;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Pane;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.*;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2024/12/13   10:33
 * @Version 1.0
 */
public class SearchModularController {
    @FXML
    private Pagination paging;
    @FXML
    private ComboBox platformSearch;
    @FXML
    private Button search;
    @FXML
    private Label platfrom;
    @FXML
    private Label platfromFixed;
    @FXML
    private Label username;
    @FXML
    private Label usernameFixed;
    @FXML
    private Label password;
    @FXML
    private Label passwordFixed;
    @FXML
    private Label email;
    @FXML
    private Label emailFixed;
    @FXML
    private Label telNum;
    @FXML
    private Label telNumFixed;
    @FXML
    private Label issue;
    @FXML
    private Label issueFixed;
    @FXML
    private Label answer;
    @FXML
    private Label answerFixed;
    @FXML
    private Label encryption;
    @FXML
    private Label dateString;
    @FXML
    private Button copyPwd;
    @FXML
    private ComboBox platformSearchOne;
    @FXML
    private ComboBox platformSearchUpdata;

    @FXML
    private Label warningPlatform;
    @FXML
    private TextField userNameNew;
    @FXML
    private Label warningUser;
    @FXML
    private TextField passwordNew;
    @FXML
    private Label warningPwd;
    @FXML
    private TextField emailNew;
    @FXML
    private TextField telNumNew;
    @FXML
    private TextField issueNew;
    @FXML
    private TextField answerNew;
    @FXML
    private ComboBox encryptionNew;
    @FXML
    private Label pwdExplain;
    @FXML
    private ComboBox userNameBox;
    @FXML
    private ComboBox encryptionUpdata;
    @FXML
    private TextField pwdUpdata;
    @FXML
    private TextField emailUpdata;
    @FXML
    private TextField telNumUpdata;
    @FXML
    private TextField issueUpdata;
    @FXML
    private TextField answerUpdata;
    @FXML
    private Label prompt;
    @FXML
    private Label promptPwd;
    @FXML
    private TextArea aboutText;
    //账号，账号信息 key  value
    public static Map<String,UserInfoBean> userNameInfo = null;
    @FXML
    public void initialize() {
        aboutText.setText("尊敬的用户：\n" +
                "\n" +
                "您好！\n" +
                "\n" +
                "感谢您使用由作者ried开发的软件。在此，我们希望明确以下几点关于软件使用的声明：\n" +
                "\n" +
                "软件所有权：本软件由作者ried独立开发，拥有全部的知识产权和所有权。\n" +
                "\n" +
                "非商业用途：本软件仅供个人学习和研究使用，严禁用于任何商业目的。任何未经授权的商业使用行为都可能侵犯作者的合法权益。\n" +
                "\n" +
                "责任限制：作者ried不对因使用本软件而产生的任何直接、间接、偶然、特殊及后续的损害负责，包括但不限于营业损失、利润损失或数据损失。\n" +
                "\n" +
                "遵守法律：使用本软件的用户必须遵守相关法律法规，不得利用软件进行任何违法活动。\n" +
                "\n" +
                "最终解释权：本声明的最终解释权归作者ried所有。\n" +
                "\n" +
                "请用户在使用软件前仔细阅读并遵守上述声明。感谢您的理解与合作。\n" +
                "\n" +
                "作者签名：ried\n" +
                "\n" +
                "邮箱：liuweiran12138@outlook.com\n" +
                "\n" +
                "日期：2024年12月18日"+
                "\n" +
                "版本：1.0.1");
        encryptionNew.getItems().addAll(CryptoAlgorithmEnum.toList());
        // 确保ComboBox选中第一个元素
        if (!encryptionNew.getItems().isEmpty()) {
            encryptionNew.getSelectionModel().selectFirst();
        }
        //监听翻页工具被点击时刷新页面label
        paging.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                pwdExplain.setVisible(false);
            }
        });
    }

    @FXML
    public void getOnSelectionChangedQuery(){
        String usernameText = username.getText();
        String platfromText = platfrom.getText();
        boolean falg = true;
        if (!usernameText.equals("")|| !platfromText.equals("")){
            Sheet sheet = ExcelManager.sheetMap.get(platfromText);
            if (sheet != null){
                List<UserInfoBean> rows = ExcelManager.getRows(sheet);
                for (UserInfoBean userinfo : rows) {
                    if (userinfo.getAccount().equals(usernameText)){
                        falg = false;
                        continue;
                    }
                }
            }
        }
        if (falg){
            platfrom.setVisible(false);
            username.setVisible(false);
            password.setVisible(false);
            email.setVisible(false);
            telNum.setVisible(false);
            issue.setVisible(false);
            answer.setVisible(false);
            encryption.setVisible(false);
            dateString.setVisible(false);
            paging.setVisible(false);
        }else{
            search.fire();
        }
    }
    @FXML
    protected void handleButtonAction(){
        String value = (String) platformSearch.getValue();
        if (value == null || value.equals("")){
            getOnSelectionChangedQuery();
            return;
        }
        Sheet sheet = ExcelManager.sheetMap.get(value);
        List<UserInfoBean> list = ExcelManager.getRows(sheet);
        if(list.size() != 0){
            paging.setPageCount(list.size());
            paging.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    // 创建页面内容
                    return createPage(pageIndex,list,value);
                }
            });
        }
    }

    private Node createPage(Integer integer,List<UserInfoBean> list,String sheetName){
        UserInfoBean data = list.get(integer);
        platfrom.setText(sheetName);
        platfrom.setVisible(true);
        username.setText(data.getAccount());
        username.setVisible(true);
        password.setText(data.getPassword());
        password.setVisible(true);
        email.setText(data.getEmail());
        email.setVisible(true);
        telNum.setText(data.getPhoneNumber());
        telNum.setVisible(true);
        issue.setText(data.getQuestion());
        issue.setVisible(true);
        answer.setText(data.getAnswer());
        answer.setVisible(true);
        encryption.setText(data.getEncryptionMethod());
        encryption.setVisible(true);
        dateString.setText(data.getUpdateTime());
        dateString.setVisible(true);
        paging.setVisible(true);
        return new Pane();
    }
    /**
     * 获取平台信息
     */
    @FXML
    protected void platformSearchList(){
        if (platformSearch.getItems() != null && !platformSearch.getItems().isEmpty()){
            platformSearch.getItems().clear();
        }
        platformSearch.getItems().addAll(ExcelManager.platformName);

    }

    @FXML
    protected void platformSearchOne(){
        if (platformSearchOne.getItems() != null && !platformSearchOne.getItems().isEmpty()){
            platformSearchOne.getItems().clear();
        }
        platformSearchOne.getItems().addAll(ExcelManager.platformName);
    }

    @FXML
    protected void platformSearchUpdata(){
        if (platformSearchUpdata.getItems() != null && !platformSearchUpdata.getItems().isEmpty()){
            platformSearchUpdata.getItems().clear();
        }
        platformSearchUpdata.getItems().addAll(ExcelManager.platformName);
    }
    /**
     * 新账号提交
     * @throws Exception
     */
    @FXML
    protected void submitSave() throws Exception {
        Object platform = platformSearchOne.getValue();
        if (platform == null){
            warningPlatform.setVisible(true);
            warningPlatform.setText("请选择平台");
            return;
        }else if (String.valueOf(platform).equals("sheet1")){
            warningPlatform.setVisible(true);
            warningPlatform.setText("sheet1不能作为平台名称保存，请更换平台名称");
            return;
        }else{
            warningPlatform.setVisible(false);
        }
        String usernameText = userNameNew.getText();
        if (usernameText ==null || usernameText.equals("")){
            warningUser.setVisible(true);
            warningUser.setText("账号不能为空");
            return;
        }else if(ExcelManager.checkUserInfoOfIn(String.valueOf(platform),usernameText)){
            warningUser.setVisible(true);
            warningUser.setText("账号已存在");
            return;
        }else{
            warningUser.setVisible(false);
        }
        String password = passwordNew.getText();
        if (password == null || password.equals("")){
            warningPwd.setVisible(true);
            warningPwd.setText("密码不能为空");
            return;
        }else{
            warningPwd.setVisible(false);
        }
        //保存数据  String platfrom, String account, String password, String email, String phoneNumber, String question, String answer, String encryptionMethod, Date updateTime
        UserInfoBean userInfoBean = new UserInfoBean(String.valueOf(platform), usernameText, password,emailNew.getText(),
                telNumNew.getText(),issueNew.getText(),answerNew.getText(),(String) encryptionNew.getValue(), DateConverterUtil.dateToString(new Date()),DateConverterUtil.dateToString(new Date()));
        if (ExcelManager.saveDate(1,userInfoBean)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示信息");
            alert.setHeaderText("保存成功");
            alert.setContentText("提交成功！已经存入账号");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("提示信息");
            alert.setHeaderText("保存失败");
            alert.setContentText("加密过程中出现了问题");
            alert.showAndWait();
        }
    }

    /**
     * 密码解密
     */
    @FXML
    public void decode() throws Exception {
        String pwd = EncryptionUtil.encryptionMethod(2, password.getText(),dateString.getText(), encryption.getText());
        //解密结果进行绑定控件后展示
        if (!pwd.equals("error")){
            password.setText(pwd);
            pwdExplain.setText("已解密");
            pwdExplain.setVisible(true);
        }
    }

    /**
     * 复制密码到剪贴板
     */
    @FXML
    public void copyPwd(){
        // 创建ClipboardContent对象
        ClipboardContent content = new ClipboardContent();
        content.putString(password.getText());
        // 获取系统剪贴板
        Clipboard clipboard = Clipboard.getSystemClipboard();
        // 将文本设置到剪贴板
        clipboard.setContent(content);
    }

    /**
     * 当获取到平台信息时查询相关账号信息
     */
    @FXML
    public void platformSearchInput(){
        getOnSelectionChangedUpdate();
        Object value = platformSearchUpdata.getValue();
        if (value != null && !value.equals("")){
            Sheet sheet = ExcelManager.sheetMap.get(value);
            if (sheet != null){
                List<UserInfoBean> list = ExcelManager.getRows(sheet);
                userNameInfo = new HashMap<>();
                for (UserInfoBean userInfoBean : list) {
                    userNameInfo.put(userInfoBean.getAccount(),userInfoBean);
                }
                userNameBox.getItems().clear();
                userNameBox.getItems().addAll(new ArrayList<>(userNameInfo.keySet()));
                userNameBox.getSelectionModel().selectFirst();
            }
        }
    }

    /**
     * 展示账户信息
     */
    @FXML
    public void userNameBoxInput(){
        if (userNameInfo != null || !userNameInfo.isEmpty()){
            UserInfoBean userInfo = userNameInfo.get(userNameBox.getValue());
            if (userInfo != null){
                encryptionUpdata.getItems().clear();
                encryptionUpdata.getItems().addAll(CryptoAlgorithmEnum.toList());
                encryptionUpdata.getSelectionModel().select(userInfo.getEncryptionMethod());
                pwdUpdata.setText(userInfo.getPassword());
                emailUpdata.setText(userInfo.getEmail());
                telNumUpdata.setText(userInfo.getPhoneNumber());
                issueUpdata.setText(userInfo.getQuestion());
                answerUpdata.setText(userInfo.getAnswer());
            }else{
                encryptionUpdata.getItems().clear();
                pwdUpdata.setText("");
                emailUpdata.setText("");
                telNumUpdata.setText("");
                issueUpdata.setText("");
                answerUpdata.setText("");
            }
        }
    }

    @FXML
    public void userNameBoxInputClick(){
        platformSearchInput();
        userNameBoxInput();
    }
    /**
     * 删除账号
     */
    @FXML
    protected void deleteUser(){
        String platform = String.valueOf(platformSearchUpdata.getValue());
        String userName = String.valueOf(userNameBox.getValue());
        // 创建确认对话框
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "确定要删除"+platform+"平台的"+userName+"账号吗？",
                ButtonType.YES, ButtonType.NO);
        alert.initStyle(StageStyle.UNIFIED);
        alert.setHeaderText(null);
        alert.setContentText("确定要删除"+platform+"平台的"+userName+"账号吗？");
        // 显示对话框并等待用户响应
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            // 用户点击了“是”，删除数据
            UserInfoBean userInfo = userNameInfo.get(userName);
            if (ExcelManager.deleteData(userInfo)){
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                infoAlert.setTitle("提示信息");
                infoAlert.setHeaderText("删除成功");
                infoAlert.setContentText("账号已删除");
                infoAlert.showAndWait();
            }
        }
        platformSearchInput();
        userNameBoxInput();
    }

    /**
     * 更新账号信息
     */
    @FXML
    public void submitUpdate() throws Exception {
        Object platform = platformSearchUpdata.getValue();
        if (platform == null){
            prompt.setText("请选择平台");
            prompt.setVisible(true);
            return;
        }
        Object userName = userNameBox.getValue();
        if (userName == null){
            prompt.setText("请选择更新的账号");
            prompt.setVisible(true);
            return;
        }
        String pwd = pwdUpdata.getText();
        if (pwd == null|| pwd.equals("")){
            promptPwd.setText("密码不能为空");
            promptPwd.setVisible(true);
            return;
        }
        Object encryption = encryptionUpdata.getValue();
        if (encryption == null){
            prompt.setText("请选择加密方式");
            prompt.setVisible(true);
            return;
        }
        prompt.setDisable(false);
        promptPwd.setDisable(false);
        String account = String.valueOf(userName);
        UserInfoBean userInfo = userNameInfo.get(account);
        if (userInfo.getPassword().equals(pwd)){
            pwd = "";
        }
        UserInfoBean userInfoBean = new UserInfoBean(String.valueOf(platform),String.valueOf(userName),pwd,
                emailUpdata.getText(),telNumUpdata.getText(),issueUpdata.getText(),answerUpdata.getText(),String.valueOf(encryption),
                DateConverterUtil.dateToString(new Date()),userInfo.getNewAddTime());
        if (ExcelManager.saveDate(2,userInfoBean)){
            prompt.setVisible(true);
            prompt.setText("更新成功");
        }

    }
    @FXML
    public void getOnSelectionChangedUpdate(){
        prompt.setVisible(false);
        promptPwd.setVisible(false);
    }
    /**
     * 退出功能
     */
    @FXML
    private void handleExitAction() {
        // 创建确认对话框
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "确定要退出程序吗？",
                ButtonType.YES, ButtonType.NO);
        alert.initStyle(StageStyle.UNIFIED);
        alert.setHeaderText(null);
        alert.setContentText("确定要退出程序吗？");

        // 显示对话框并等待用户响应
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            // 用户点击了“是”，则退出程序
            Platform.exit();
        }
    }
}
