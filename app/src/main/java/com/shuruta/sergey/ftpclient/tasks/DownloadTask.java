/*
package com.shuruta.sergey.ftpclient.tasks;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.FTPClient.Request;
import com.example.FTPClient.applications.FTPApplication;
import com.example.FTPClient.entity.FFile;
import com.example.FTPClient.entity.HeapFiles;
import com.example.FTPClient.events.MessageEvent;
import com.example.FTPClient.managers.FTPManager;
import com.example.FTPClient.services.Task;
import com.shuruta.sergey.ftpclient.EventBusMessenger;
import com.shuruta.sergey.ftpclient.entity.Connection;
import com.shuruta.sergey.ftpclient.entity.HeapFiles;
import com.shuruta.sergey.ftpclient.interfaces.FFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPFile;

*/
/**
 * Author: Sergey Shuruta
 * Date: 11/25/13
 * Time: 5:37 PM
 *//*

public class DownloadTask extends Task {

    private FTPClient ftpClient;
    private HeapFiles heapFiles;
    private EventBus mBus;
    private boolean isError = false;
    
    public static final String TAG = DownloadTask.class.getSimpleName();

    public DownloadTask(Context context, FTPClient ftpClient, HeapFiles heapFiles) {
        super(context);
        this.ftpClient = ftpClient;
        this.heapFiles = heapFiles;
        mBus = EventBus.getDefault();
    }

    @Override
    public void run() {
        EventBusMessenger.sendFtpMessage(EventBusMessenger.Event.START);
        Bundle bundle = new Bundle();
        Bundle bundle = new Bundle();
        try {
            downloadFiles(heapFiles.getFiles());
        }

        if(heapFiles == null) return;
        mBus.post(new MessageEvent(Request.REQUEST_DOWNLOAD_POOL_START));

        
        for(FFile file : heapFiles.getFiles()) {
            file.setLocalDir(heapFiles.getDownloadTo());
        }
        downloadFiles(heapFiles.getFiles());
        if(!isError)
            mFTPManager.removeDownloadPool();
        //mBus.post(new MessageEvent(Request.REQUEST_DOWNLOAD_POOL_FINISH));
    }

    private void downloadFiles(List<FFile> files) {
        for(FFile file : files) {
            if(isError) return;
            Log.d(TAG, "Select: " + file.getName());
            if(file.isDir()) {
                String dir = file.getName();
                Log.d(TAG, "Generate: " + file.getLocalDir() + File.separator + dir);
                File localDir = new File(file.getLocalDir() + File.separator + dir);
                localDir.mkdir();
                try {
                    String readdir = File.separator + file.getFtpDir() + dir + File.separator;
                    Log.d(TAG, "readdir: " + readdir);
                    FTPFile[] list = mSession.list(File.separator + readdir);
                    Log.d(TAG, "list: " + list.length);
                    List<FFile> dirFiles = new ArrayList<FFile>();
                    for(FTPFile f : list) {
                        if(f.getName().equals(".") || f.getName().equals("..")) continue;
                        //Log.d(TAG, "File: " + f.getName());
                        FFile newNode = new FFile(f, readdir);
                        newNode.setFtpDir(file.getFtpDir() + File.separator + dir + File.separator);
                        newNode.setLocalDir(file.getLocalDir() + File.separator + dir);
                        dirFiles.add(newNode);
                    }
                    if(dirFiles.size() > 0)
                        downloadFiles(dirFiles);
                } catch (Exception e) {
                    Log.d(TAG, "Exception: " + e.getMessage() + "(" + file.getName() + ")");
                    //mBus.post(new MessageEvent(Request.REQUEST_DOWNLOAD_POOL_ERROR));
                    isError = true;
                    return;
                }
            } else if(file.isFile()) {
                if(mFTPManager.isDownloadedFile(file)) continue;
                try {
                    heapFiles.setDownloadingFile(file);
                    File localFile = new File(file.getLocalDir() + File.separator + file.getName());
                    Log.d("TEST", "Path to file: " + localFile.getPath());
                    //Log.d(TAG, "Is replace " + file.getName() + ": " + heapFiles.isReplaceFile(file.getLocalDir() + File.separator + file.getName()));
                    if(!localFile.exists()*/
/* || heapFiles.isReplaceAll() || heapFiles.isReplaceFile(file.getLocalDir() + File.separator + file.getName())*//*
) {
                        // Start download: /argest.com.ua/www/wp-content/themes/twentythirteen/images/search-icon-2x.png
                        // To: /storage/emulated/0/Download/twentythirteen/images/search-icon-2x.png

                        Log.d(TAG, "Start download: " + File.separator + file.getFtpDir() + File.separator + file.getName());
                        Log.d(TAG, "To: " + file.getLocalDir() + "/" + file.getName());
                        mSession.download(File.separator + file.getFtpDir() + File.separator + file.getName(), new File(file.getLocalDir() + File.separator + file.getName()), heapFiles.getListener());
                        mFTPManager.addToDownloadedFileList(file);
                    } else {
                        Log.d(TAG, file.getName() + " is present");
                        //mBus.post(new MessageEvent(Request.REQUEST_DOWNLOAD_FILE_PRESENT));
                        isError = true;
                        return;
                    }
                } catch(Exception e) {
                    Log.d(TAG, "Download " + file.getName() + " is error with exception: " + e.getMessage());
                    //mBus.post(new MessageEvent(Request.REQUEST_DOWNLOAD_POOL_ERROR));
                    isError = true;
                    return;
                }
            }
        }
    }
}
*/
