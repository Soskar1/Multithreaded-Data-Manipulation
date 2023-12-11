package com.mdm.mdm;

public class FileProgressInfo {
    private final String fileName;
    private double progress;

    public FileProgressInfo(String fileName) {
        this.fileName = fileName;
        progress = 0;
    }

    public String getFileName() {
        return fileName;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }
}
