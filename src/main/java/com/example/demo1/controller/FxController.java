package com.example.demo1.controller;

import com.example.demo1.service.FileService;
import com.example.demo1.service.FxService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class FxController {

    FxService service = new FileService();
//  FxService service = new FileService_via_CommonMultithreading();

    @FXML
    private Label label;

    @FXML
    private ProgressBar progressBar;


    @FXML
    protected void onStartCopyClick() {
        String source = "D:\\Михаил\\Videos\\Остров сокровищ (1988)\\Ostrov sokrovisch (1988). Part2.avi";
        String destination = "E:\\GIT\\FX-Altabel-maven_new\\demo1\\Ostrov sokrovisch (1988). Part2.avi";
        service.copyFile(source, destination, label, progressBar);
    }

    @FXML
    protected void onCancelCopyClick() {
        service.cancelCopy();
    }
}