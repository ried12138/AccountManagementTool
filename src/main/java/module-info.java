module com.example.accountmanagementtool {
    requires javafx.controls;
    requires javafx.fxml;
//    requires javafx.web;
//    requires org.controlsfx.controls;
//    requires com.dlsc.formsfx;
//    requires validatorfx;
//    requires org.kordamp.ikonli.javafx;
//    requires org.kordamp.bootstrapfx.core;
//    requires eu.hansolo.tilesfx;
    requires lombok;
    requires org.apache.poi.ooxml;

    opens com.example.accountmanagementtool to javafx.fxml;
    exports com.example.accountmanagementtool;
    exports com.example.accountmanagementtool.controller;
    exports com.example.accountmanagementtool.bean;
    exports com.example.accountmanagementtool.utils;
    opens com.example.accountmanagementtool.controller to javafx.fxml;
    exports com.example.accountmanagementtool.config;
    exports com.example.accountmanagementtool.enumeration;
    opens com.example.accountmanagementtool.enumeration to javafx.fxml;
    opens com.example.accountmanagementtool.utils to javafx.fxml;
}