package com.shuruta.sergey.ftpclient.entity;

import com.shuruta.sergey.ftpclient.interfaces.FFile;

/**
 * Created by Sergey on 12.12.2015.
 */
public class DFile {

    private final FFile file;
    private final String from;
    private final String to;
    private final long size;
    private long progress;


    public DFile(FFile file, String from, String to) {
        this.file = file;
        this.from = from;
        this.to = to;
        this.size = file.getSize();
    }

    public void setProgress(long progres) {
        this.progress = progres;
    }

    public String getProgress() {
        return String.valueOf((this.size / this.progress) * 100);
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    protected DFile clone() {
        return new DFile(file, from, to);
    }

    public String getName() {
        return file.getName();
    }

    public boolean isDir() {
        return file.isDir();
    }

    public boolean isFile() {
        return file.isFile();
    }
}
