package com.example.demo1.service;


import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileService implements FxService{
    private Task<Void> fileCopyTask;

    public void copyFile(String sourcePath, String destination, Label statusLabel, ProgressBar progressBar) {
        File sourceFile = new File(sourcePath);
        fileCopyTask = new Task<Void>() {
            @Override
            protected Void call() {
                long fileSize = sourceFile.length();
                long progress = 0;

                try (InputStream inputStream = new FileInputStream(sourceFile);
                     OutputStream outputStream = new FileOutputStream(destination)) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        if (isCancelled()) {
                            outputStream.close();
                            cleanUncompletedCopy(destination);
                            break;
                        }
                        outputStream.write(buffer, 0, bytesRead);
                        progress += bytesRead;
                        updateProgress(progress, fileSize);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        fileCopyTask.setOnRunning(event -> {
            statusLabel.setText("Copy in progress...");
        });

        fileCopyTask.setOnSucceeded(event -> {
            statusLabel.setText("Done.");
        });

        fileCopyTask.setOnFailed(event -> {
            statusLabel.setText("Error in the copying process.");
        });

        fileCopyTask.setOnCancelled(event -> {
            statusLabel.setText("Coping was interrupted.");
        });

        progressBar.progressProperty().bind(fileCopyTask.progressProperty());

        new Thread(fileCopyTask).start();
    }


    private void cleanUncompletedCopy(String destination) {
        Path filePath = Paths.get(destination);
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void cancelCopy() {
        if (fileCopyTask != null) {
            fileCopyTask.cancel();
        }
    }
}


