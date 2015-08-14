package com.shuruta.sergey.ftpclient;

import android.graphics.drawable.Drawable;

import java.io.File;

import it.sauronsoftware.ftp4j.FTPFile;

/**
 * Created by Sergey Shuruta
 * 14.08.2015 at 13:56
 */
public class FFile {

    private final FTPFile ftpFile;
    private final File file;
    private FileType fileType;

    public static final int TYPE_FILE = 0;
    public static final int TYPE_DIRECTORY = 1;
    public static final int TYPE_LINK = 2;

    public enum FileType {
        DIRECTORY,
        UNKNOWN_FILE,
        IMAGE_FILE,
    }

    public FFile(FTPFile ftpFile) {
        this.ftpFile = ftpFile;
        this.file = null;
        calculateType();
    }

    public FFile(File file) {
        this.file = file;
        this.ftpFile = null;
        calculateType();
    }

    public String getName() {
        String name = null == ftpFile ? file.getName() : ftpFile.getName();
        return name.equals(".") ? CustomApplication.getInstance().getResources().getString(R.string.back) : name;
    }

    public long getSize() {
        return null == ftpFile ? file.length() : ftpFile.getSize();
    }

    public String getSizeString() {
        return Utils.readableFileSize(getSize());
    }

    public boolean isDirectory() {
        return null == ftpFile ? file.isDirectory() : (ftpFile.getType() == FTPFile.TYPE_DIRECTORY);
    }

    public boolean isFile() {
        return null == ftpFile ? file.isFile() : (ftpFile.getType() == FTPFile.TYPE_FILE);
    }

    public boolean isBack() {
        String name = null == ftpFile ? file.getName() : ftpFile.getName();
        return name.equals(".");
    }

    private void calculateType() {
        if(isDirectory()) {
            this.fileType = FileType.DIRECTORY;
            return;
        }
        String name = getName();
        switch (name.substring(name.lastIndexOf(".") + 1).toLowerCase()) {
            case "php":
            case "txt":
            case "png":
            case "jpg":
            case "jpeg":
            case "gif":
                this.fileType = FileType.IMAGE_FILE;
                break;
            default:
                this.fileType = FileType.UNKNOWN_FILE;
        }
    }

    public Drawable getIcon() {
        int res;
        switch (fileType) {
            case DIRECTORY: res = R.drawable.ic_action_collection; break;
            case IMAGE_FILE: res = R.drawable.ic_action_picture; break;
            default: res = R.drawable.ic_action_unknown;
        }
        return CustomApplication.getInstance().getApplicationContext().getResources().getDrawable(res);
    }

}
