package com.shuruta.sergey.ftpclient.entity;

import com.shuruta.sergey.ftpclient.event.CommunicationEvent;
import com.shuruta.sergey.ftpclient.interfaces.FFile;

import it.sauronsoftware.ftp4j.FTPDataTransferListener;

/**
 * Created by Sergey on 12.12.2015.
 */
public class DFile {

    private final FFile file;
    private final String from;
    private final String to;
    private final long size;
    private long progress = 0;

    public DFile(FFile file, String from, String to) {
        this.file = file;
        this.from = from;
        this.to = to;
        this.size = file.getSize();
    }

    public void addProgress(long progres) {
        this.progress += progres;
    }

    public String getProgress() {
        return String.valueOf(getProgressInt());
    }

    public int getProgressInt() {
        return (int)(100*(double)this.progress/Double.valueOf(this.size));
    }

    public long getSize() {
        return size;
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
