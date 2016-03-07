package com.shuruta.sergey.ftpclient.entity;

import com.shuruta.sergey.ftpclient.interfaces.FFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey on 07.03.2016.
 */
public class DownloadEntity {

    private final FFile startFile;
    private final String from;
    private final String to;

    private List<DFile> downloadedList = new ArrayList<>();
    private List<DFile> skippedList = new ArrayList<>();
    private boolean isOverrideNext = false;
    private DFile currentFile;

    public DownloadEntity(FFile startFile, String from, String to) {
        this.startFile = startFile;
        this.from = from;
        this.to = to;
    }

    public FFile getStartFile() {
        return startFile;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public void setCurrentFile(DFile currentFile) {
        this.currentFile = currentFile;
    }

    public boolean isDownloaded(DFile dFile) {
        return downloadedList.contains(dFile) || skippedList.contains(dFile);
    }

    public void setDownloaded() {
        downloadedList.add(currentFile);
    }

    public void setSkip() {
        skippedList.add(currentFile);
    }

    public void setOverride() {
        isOverrideNext = true;
    }

    public boolean isOverride() {
        if (isOverrideNext) {
            isOverrideNext = false;
            return true;
        }
        return false;
    }

    public DFile getCurrentFile() {
        return currentFile;
    }
}
