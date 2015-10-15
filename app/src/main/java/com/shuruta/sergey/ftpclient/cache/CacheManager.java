package com.shuruta.sergey.ftpclient.cache;

import android.util.Log;

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

    public List<FFile> getFtpFiles() {
        return new ArrayList<FFile>(this.ftpFiles);
    }

    public List<FFile> getLocalFiles() {
        return new ArrayList<FFile>(localFiles);
    }


    public void putFtpFiles(List<FFile> ftpFiles) {
        this.ftpFiles.clear();
        this.ftpFiles.addAll(ftpFiles);
        Log.d("TEST", "Put ftp files: " + this.ftpFiles.size());
    }

    public void putLocalFiles(List<FFile> localFiles) {
        Log.d(TAG, "Put local files: " + localFiles.size());
        this.localFiles.clear();
        this.localFiles.addAll(localFiles);
    }
}
