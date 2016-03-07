package com.shuruta.sergey.ftpclient.cache;

import com.shuruta.sergey.ftpclient.Constants;
import com.shuruta.sergey.ftpclient.entity.DFile;
import com.shuruta.sergey.ftpclient.entity.DownloadEntity;
import com.shuruta.sergey.ftpclient.interfaces.FFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Sergey Shuruta
 * Date: 08/15/15
 * Time: 22:11
 */
public class CacheManager {

    private List<FFile> ftpFiles = new ArrayList<>();
    private List<FFile> localFiles = new ArrayList<>();
    private DownloadEntity downloadEntity;
    public static final String TAG = CacheManager.class.getSimpleName();

    private static CacheManager instance;

    public static CacheManager getInstance() {
        if(null == instance)
            synchronized (CacheManager.class) {
                if(null == instance)
                    instance = new CacheManager();
            }
        return instance;
    }

    public List<FFile> getFiles(int listType) {
        return new ArrayList<FFile>(listType == Constants.TYPE_FTP ? this.ftpFiles : this.localFiles);
    }

    public void putFiles(List<FFile> ftpFiles, int listType) {
        if(listType == Constants.TYPE_FTP) {
            this.ftpFiles.clear();
            this.ftpFiles.addAll(ftpFiles);
        } else if(listType == Constants.TYPE_LOCAL) {
            this.localFiles.clear();
            this.localFiles.addAll(ftpFiles);
        }
    }

    public void clearCache() {
        this.ftpFiles.clear();
        this.localFiles.clear();
    }

    public DownloadEntity getDownloadEntity() {
        return downloadEntity;
    }

    public DownloadEntity newDownloadEntity(FFile startFile, String from, String to) {
        if(null == downloadEntity)
            downloadEntity = new DownloadEntity(startFile, from, to);
        return downloadEntity;
    }

    public void clearDownload() {
        downloadEntity = null;
    }
}
