package com.shuruta.sergey.ftpclient.tasks;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.shuruta.sergey.ftpclient.cache.CacheManager;
import com.shuruta.sergey.ftpclient.entity.DFile;
import com.shuruta.sergey.ftpclient.entity.DownloadEntity;

import java.io.File;
import java.io.IOException;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
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
        Bundle bundle = new Bundle();
        //EventBusMessenger.sendFtpMessage(EventBusMessenger.Event.START_DOWNLOAD);
        do {
            DownloadEntity downloadEntity = CacheManager.getInstance().getNextDownload();
            if(null == downloadEntity) break;
            for(DFile dFile : downloadEntity) {
                try {
                    downloadFile(dFile);
                } catch (Exception e) {
                    //bundle.putString(EventBusMessenger.MSG, e.getMessage());
                    e.printStackTrace();
                }
            }
        } while(CacheManager.getInstance().isHasDownload());
/*        if(bundle.containsKey(EventBusMessenger.MSG))
            EventBusMessenger.sendFtpMessage(EventBusMessenger.Event.ERROR_DOWNLOAD, bundle);
        EventBusMessenger.sendFtpMessage(EventBusMessenger.Event.FINISH_DOWNLOAD);*/
    }

    private boolean downloadFile(DFile dFile) {
        File lFile = new File(dFile.getTo() + dFile.getName());
        if(dFile.isDir())
            try {
                Log.d(TAG, "Try create dir " + lFile.getPath());
                return lFile.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        if(dFile.isFile())
            try {
                ftpClient.download(dFile.getFrom() + dFile.getName(), lFile, dFile);
                Log.d(TAG, "Download " + dFile.getFrom() + dFile.getName() + " => " + lFile.getPath());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FTPIllegalReplyException e) {
                e.printStackTrace();
            } catch (FTPException e) {
                e.printStackTrace();
            } catch (FTPDataTransferException e) {
                e.printStackTrace();
            } catch (FTPAbortedException e) {
                e.printStackTrace();
            }
        return false;
    }
}
