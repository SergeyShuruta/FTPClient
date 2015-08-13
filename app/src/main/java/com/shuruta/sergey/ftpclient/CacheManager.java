package com.shuruta.sergey.ftpclient;

import java.util.ArrayList;
import java.util.List;

import it.sauronsoftware.ftp4j.FTPFile;

/**
 * Author: Sergey Shuruta
 * Date: 08/15/15
 * Time: 22:11
 */
public class CacheManager {

    private List<FTPFile> ftpFiles = new ArrayList<>();

    private static CacheManager instance;

    public static CacheManager getInstance() {
        if(null == instance)
            synchronized (CacheManager.class) {
                if(null == instance)
                    instance = new CacheManager();
            }
        return instance;
    }

    public List<FTPFile> getFtpFiles() {
        return new ArrayList<FTPFile>(ftpFiles);
    }

    public void putFtpFiles(List<FTPFile> ftpFiles) {
        this.ftpFiles.clear();
        this.ftpFiles.addAll(ftpFiles);
    }
}
