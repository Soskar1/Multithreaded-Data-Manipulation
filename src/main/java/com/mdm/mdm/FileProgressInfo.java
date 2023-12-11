package com.mdm.mdm;

public class FileProgressInfo {
    private final String fileName;
    private final FileReader thread;

    public FileProgressInfo(String fileName, FileReader thread) {
        this.fileName = fileName;
        this.thread = thread;
    }

    public String getFileName() {
        return fileName;
    }

    public FileReader getThread() {
        return thread;
    }
}
