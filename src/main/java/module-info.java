module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.demo1 to javafx.fxml;
    exports com.example.demo1;
    exports com.example.demo1.service;
    opens com.example.demo1.service to javafx.fxml;
    exports com.example.demo1.controller;
    opens com.example.demo1.controller to javafx.fxml;
}