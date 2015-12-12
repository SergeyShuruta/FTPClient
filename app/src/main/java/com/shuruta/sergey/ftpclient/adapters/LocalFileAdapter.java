package com.shuruta.sergey.ftpclient.adapters;

import com.shuruta.sergey.ftpclient.interfaces.FFile;
import com.shuruta.sergey.ftpclient.utils.Utils;

import java.io.File;

import it.sauronsoftware.ftp4j.FTPFile;

/**
 * Created by Sergey Shuruta
 * 02.10.2015 at 15:33
 */
public class LocalFileAdapter extends FileAdapter {

    private File mFile;

    public static FFile create(String path) {
        return create(new File(path));
    }

    public static FFile create() {
        return new LocalFileAdapter(new File("."));
    }

    public static FFile create(File file) {
        return new LocalFileAdapter(file);
    }

    protected LocalFileAdapter(File file) {
        mFile = file;
    }

    @Override
    public String getName() {
        return mFile.getName();
    }

    @Override
    public long getSize() {
        return mFile.length();
    }

    @Override
    public String getFormatSize() {
        return Utils.readableFileSize(getSize());
    }

    @Override
    public boolean isDir() {
        return mFile.isDirectory();
    }

    @Override
    public boolean isFile() {
        return mFile.isFile();
    }

    @Override
    public boolean isBackButton() {
        return mFile.getName().equals(".");
    }

    public FFile copy() {
        try {
            return (FFile)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
