package com.shuruta.sergey.ftpclient.tasks;

import android.content.Context;

import com.shuruta.sergey.ftpclient.adapters.FTPFileAdapter;
import com.shuruta.sergey.ftpclient.cache.CacheManager;
import com.shuruta.sergey.ftpclient.entity.DFile;
import com.shuruta.sergey.ftpclient.entity.DownloadEntity;
import com.shuruta.sergey.ftpclient.event.CommunicationEvent;
import com.shuruta.sergey.ftpclient.interfaces.FFile;
import com.shuruta.sergey.ftpclient.services.FtpService;

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
    private FtpService ftpService;

    public PreDownloadTask(Context context, FTPClient ftpClient, FFile file, String from, String to, FtpService ftpService) {
        super(context);
        this.ftpClient = ftpClient;
        this.file = file;
        this.from = from;
        this.to = to;
        this.ftpService = ftpService;
    }

    @Override
    public void run() {
        CommunicationEvent.sendPreDownload(CommunicationEvent.State.START);
        try {
            CacheManager.getInstance().addToDownload(addToDownload(file, from, to));
        } catch (Exception e) {
            CommunicationEvent.sendPreDownload(CommunicationEvent.State.ERROR, e.getMessage());
            e.printStackTrace();
        }
        CommunicationEvent.sendPreDownload(CommunicationEvent.State.FINISH);
        if(CacheManager.getInstance().isHasDownload()) {
            ftpService.download();
        }
    }

    private DownloadEntity addToDownload(FFile file, String from, String to) {
        return addToDownload(new DFile(file, from, to), null);
    }

    private DownloadEntity addToDownload(DFile file, DownloadEntity entity) {
        if(null == entity) {
            entity = new DownloadEntity(file.getFrom(), file.getTo());
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
