package com.example.demo1.service;


import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileService_via_CommonMultithreading implements FxService{
    private Thread copyFileThread;


    public void copyFile(String source, String destination, Label statusLabel, ProgressBar progressBar) {
        progressBar.setProgress(0);
        copyFileThread = new Thread(new CopyThread(source, destination, statusLabel, progressBar));
        copyFileThread.setDaemon(true);
        copyFileThread.start();
    }


    public void cancelCopy() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new CopyInterrupter());
    }


    private class CopyThread implements Runnable {
        String sourcePath;
        String destinationPath;
        Label statusLabel;
        ProgressBar progressBar;


        public CopyThread(String sourcePath, String destinationPath, Label statusLabel, ProgressBar progressBar) {
            this.sourcePath = sourcePath;
            this.destinationPath = destinationPath;
            this.statusLabel = statusLabel;
            this.progressBar = progressBar;
        }

        @Override
        public void run() {
            File file = new File(sourcePath);
            if (file.exists()) {
                try (InputStream sourceStream = new FileInputStream(sourcePath); OutputStream destinationStream = new FileOutputStream(destinationPath)) {
                    long fileSize = file.length();
                    double progress = 0;
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    Platform.runLater(() -> {
                        statusLabel.setText("Copy started");
                    });

                    while ((bytesRead = sourceStream.read(buffer)) != -1 && !Thread.currentThread().isInterrupted()) {
                        destinationStream.write(buffer, 0, bytesRead);
                        progress += buffer.length;
                        progressBar.setProgress(progress / fileSize);
                    }

                    if (!Thread.currentThread().isInterrupted()) {
                        Platform.runLater(() -> {
                            statusLabel.setText("Copy completed");
                        });
                    } else {
                        destinationStream.close();
                        Platform.runLater(() -> {
                            statusLabel.setText("Copy interrupted");
                        });
                        cleanAfterCopy(destinationPath);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        private void cleanAfterCopy(String destination) {
            Path filePath = Paths.get(destination);
            try {
                Files.delete(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private class CopyInterrupter implements Runnable {
        @Override
        public void run() {
            copyFileThread.interrupt();
        }
    }
}


