package com.shuruta.sergey.ftpclient.tasks;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.shuruta.sergey.ftpclient.cache.CacheManager;
import com.shuruta.sergey.ftpclient.entity.DFile;
import com.shuruta.sergey.ftpclient.entity.DownloadEntity;
import com.shuruta.sergey.ftpclient.event.CommunicationEvent;

import java.io.File;
import java.io.IOException;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

/**
 * Created by Sergey Shuruta
 * 08.12.2015 at 15:57
 */
public class DownloadTask extends Task {

    private FTPClient ftpClient;

    private static String TAG = DownloadTask.class.getSimpleName();

    public DownloadTask(Context context, FTPClient ftpClient) {
        super(context);
        this.ftpClient = ftpClient;
    }

    @Override
    public void run() {
        CommunicationEvent.sendDownload(CommunicationEvent.State.START);
        do {
            DownloadEntity downloadEntity = CacheManager.getInstance().getNextDownload();
            if(null == downloadEntity) break;
            for(DFile dFile : downloadEntity) {
                try {
                    downloadFile(dFile, downloadEntity);
                    //CommunicationEvent.sendDownload(CommunicationEvent.State.FINISH);
                } catch (Exception e) {
                    CommunicationEvent.sendDownload(CommunicationEvent.State.ERROR, e.getMessage());
                    e.printStackTrace();
                }
            }
        } while(CacheManager.getInstance().isHasDownload());
        CommunicationEvent.sendDownload(CommunicationEvent.State.FINISH);
    }

    private boolean downloadFile(DFile dFile, FTPDataTransferListener transferListener) throws
            IOException,
            FTPAbortedException,
            FTPDataTransferException,
            FTPException,
            FTPIllegalReplyException {
        File lFile = new File(dFile.getTo() + dFile.getName());
        if(dFile.isDir())
            try {
                Log.d(TAG, "Try create dir " + lFile.getPath());
                return lFile.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        if(dFile.isFile()) {
            ftpClient.download(dFile.getFrom() + dFile.getName(), lFile, transferListener);
            Log.d(TAG, "Download " + dFile.getFrom() + dFile.getName() + " => " + lFile.getPath());
            return true;
        }
        return false;
    }
}
