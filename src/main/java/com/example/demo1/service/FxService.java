package com.example.demo1.service;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public interface FxService {
    void copyFile(String sourcePath, String destination, Label statusLabel, ProgressBar progressBar);

    void cancelCopy();
}
