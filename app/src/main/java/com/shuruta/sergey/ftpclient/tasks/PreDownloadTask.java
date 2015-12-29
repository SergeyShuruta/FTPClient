package com.shuruta.sergey.ftpclient.tasks;

import android.content.Context;
import android.os.Bundle;

import com.shuruta.sergey.ftpclient.adapters.FTPFileAdapter;
import com.shuruta.sergey.ftpclient.cache.CacheManager;
import com.shuruta.sergey.ftpclient.entity.DFile;
import com.shuruta.sergey.ftpclient.entity.DownloadEntity;
import com.shuruta.sergey.ftpclient.interfaces.FFile;

import java.io.File;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPFile;

/**
 * Created by Sergey Shuruta
 * 08.12.2015 at 15:57
 */
public class PreDownloadTask extends Task {

    private FTPClient ftpClient;
    private FFile file;
    private String from, to;

    public PreDownloadTask(Context context, FTPClient ftpClient, FFile file, String from, String to) {
        super(context);
        this.ftpClient = ftpClient;
        this.file = file;
        this.from = from;
        this.to = to;
    }

    @Override
    public void run() {
        Bundle bundle = new Bundle();
        //EventBusMessenger.sendFtpMessage(EventBusMessenger.Event.START_PREDOWNLOAD);
        try {
            CacheManager.getInstance().addToDownload(addToDownload(file, from, to));
        } catch (Exception e) {
            //bundle.putString(EventBusMessenger.MSG, e.getMessage());
            e.printStackTrace();
        }
/*
        if(bundle.containsKey(EventBusMessenger.MSG))
            EventBusMessenger.sendFtpMessage(EventBusMessenger.Event.ERROR_PREDOWNLOAD, bundle);
        EventBusMessenger.sendFtpMessage(EventBusMessenger.Event.FINISH_PREDOWNLOAD);
*/
    }

    private DownloadEntity addToDownload(FFile file, String from, String to) {
        return addToDownload(new DFile(file, from, to), null);
    }

    private DownloadEntity addToDownload(DFile file, DownloadEntity entity) {
        if(null == entity) {
            entity = new DownloadEntity();
        }
        entity.putFile(file);
        if(file.isDir()) {
            try {
                String newFrom = file.getFrom().concat(file.getName()).concat(File.separator);
                String newTo = file.getTo().concat(file.getName()).concat(File.separator);
                FTPFile[] filesArray = ftpClient.list(newFrom);
                for(int i = 0; i < filesArray.length; i++) {
                    if(filesArray[i].getName().equals("..")
                            || filesArray[i].getName().equals(".")) continue;
                    addToDownload(new DFile(FTPFileAdapter.create(filesArray[i]), newFrom, newTo), entity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return entity;
    }
}
