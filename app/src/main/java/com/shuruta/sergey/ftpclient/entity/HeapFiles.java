/*
package com.shuruta.sergey.ftpclient.entity;

import android.util.Log;

import com.example.FTPClient.Request;
import com.example.FTPClient.applications.FTPApplication;
import com.example.FTPClient.events.MessageEvent;
import com.shuruta.sergey.ftpclient.CustomApplication;
import com.shuruta.sergey.ftpclient.interfaces.FFile;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

*/
/**
 * Author: Sergey Shuruta
 * Date: 11/26/13
 * Time: 2:50 PM
 *//*

public class HeapFiles {

    private List<FFile> mFiles = new ArrayList<FFile>(); // Список файлов
    private long mProgressBytes; // Текущий прогресс
    private FFile mErrorFile;
    private FFile mProcessingFile;
    private String mFileName;

    private boolean mReplaceAll;
    private String mDownloadTo;

    private final static String TAG = HeapFiles.class.getSimpleName();

    public HeapFiles(List<FFile> files) {
        this(new ArrayList<FFile>(files), CustomApplication.getInstance().getCurrentConnection().getLocalDir());
    }

    public HeapFiles(List<FFile> files, String downloadTo) {
        mFiles.clear();
        mFiles.addAll(files);
        mProgressBytes = 0;
        mReplaceAll = false;
        mDownloadTo = downloadTo;
    }

    public void setDownloadTo(String downloadTo) {
        mDownloadTo = downloadTo;
    }

    public String getDownloadTo() {
        return mDownloadTo;
    }

    public List<FFile> getFiles() {
        return mFiles;
    }

    public void addProgress(long progressBytes) {
        mProgressBytes += progressBytes;
    }

    public int getProgress() {
        return (int)(100*(double)mProgressBytes/Double.valueOf(mProcessingFile.getSize()));
    }

    public void clearProgress() {
        mProgressBytes = 0;
    }

    public void setDownloadFailed(FFile downloadFailed) {
        mErrorFile = downloadFailed;
    }

    public FFile getDownloadFailed() {
        return mErrorFile;
    }

    public void setDownloadingFile(FFile downloadingFile) {
        mProcessingFile = downloadingFile;
    }

    public FFile getDownloadingFile() {
        return mProcessingFile;
    }

    public void setReplaceFile(String fileName) {
        mFileName = fileName;
    }

    public boolean isReplaceFile(String fileName) {
        return (mFileName != null && mFileName.compareTo(fileName) == 0);
    }

    public void setReplaceAll() {
        mReplaceAll = true;
    }

    public boolean isReplaceAll() {
        return mReplaceAll;
    }

    public FTPDataTransferListener getListener() {
        return new FTPDataTransferListener() {
            @Override
            public void started() {
                Log.d(TAG, "Start download: " + mProcessingFile.getName());
                clearProgress();
                EventBus.getDefault().post(new MessageEvent(Request.REQUEST_DOWNLOAD_FILE_START));
            }

            @Override
            public void transferred(int length) {
                addProgress(length);
                MessageEvent event = new MessageEvent(Request.REQUEST_DOWNLOAD_FILE_PROGRESS);
                EventBus.getDefault().post(event);
            }

            @Override
            public void completed() {
                Log.d(TAG, "Completed: " + mProcessingFile.getName());
                clearProgress();
                MessageEvent event = new MessageEvent(Request.REQUEST_DOWNLOAD_FILE_COMPLETED);
                EventBus.getDefault().post(event);
            }

            @Override
            public void aborted() {
                */
/*Log.d(TAG, "Aborted: " + mProcessingFile.getName());
                setStatus(FTPFileNode.FILE_STATUS_ABORTED);
                MessageEvent event = new MessageEvent(Request.REQUEST_DOWNLOAD_FILE_ABORTED);
                event.setValue(getId());
                bus.post(event);*//*

            }

            @Override
            public void failed() {
                */
/*Log.d(TAG, "Failed: " + getName());
                //setDownloadFailed(FTPFileNode.this);
                setStatus(FTPFileNode.FILE_STATUS_FAILED);
                MessageEvent event = new MessageEvent(Request.REQUEST_DOWNLOAD_FILE_FAILED);
                event.setValue(getId());
                bus.post(event);*//*


            }
        };
    }
}*/
