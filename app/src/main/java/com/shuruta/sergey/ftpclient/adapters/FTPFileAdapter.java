package com.shuruta.sergey.ftpclient.adapters;

import com.shuruta.sergey.ftpclient.utils.Utils;
import com.shuruta.sergey.ftpclient.interfaces.FFile;
import it.sauronsoftware.ftp4j.FTPFile;

/**
 * Created by Sergey Shuruta
 * 02.10.2015 at 15:33
 */
public class FTPFileAdapter extends FileAdapter {

    private FTPFile mFile;

    public static FFile create(FTPFile file) {
        return new FTPFileAdapter(file);
    }

    protected FTPFileAdapter(FTPFile file) {
        mFile = file;
    }

    @Override
    public String getName() {
        return mFile.getName();
    }

    @Override
    public long getSize() {
        return mFile.getSize();
    }

    @Override
    public String getFormatSize() {
        return Utils.readableFileSize(getSize());
    }

    @Override
    public boolean isDir() {
        return mFile.getType() == FTPFile.TYPE_DIRECTORY;
    }

    @Override
    public boolean isFile() {
        return mFile.getType() == FTPFile.TYPE_FILE;
    }

    @Override
    public boolean isBackButton() {
        return mFile.getName().equals(".");
    }

}
