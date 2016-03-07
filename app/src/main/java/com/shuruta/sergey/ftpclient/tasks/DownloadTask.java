package com.shuruta.sergey.ftpclient.tasks;

import android.content.Context;
import android.util.Log;

import com.shuruta.sergey.ftpclient.CustomApplication;
import com.shuruta.sergey.ftpclient.R;
import com.shuruta.sergey.ftpclient.adapters.FTPFileAdapter;
import com.shuruta.sergey.ftpclient.cache.CacheManager;
import com.shuruta.sergey.ftpclient.entity.DFile;
import com.shuruta.sergey.ftpclient.entity.DownloadEntity;
import com.shuruta.sergey.ftpclient.event.CommunicationEvent;
import com.shuruta.sergey.ftpclient.interfaces.FFile;

import java.io.File;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPFile;

/**
 * Created by Sergey Shuruta
 * 08.12.2015 at 15:57
 */
public class DownloadTask extends Task implements FTPDataTransferListener {

    private Context context;
    private FTPClient ftpClient;
    private FFile downloadFile;
    private DownloadEntity downloadEntity;
    private String from, to;
    private boolean isBreak = false;

    private static String TAG = DownloadTask.class.getSimpleName();

    public DownloadTask(Context context, FTPClient ftpClient, FFile ftpFile, String from, String to) {
        super(context);
        this.context = context;
        this.ftpClient = ftpClient;
        this.downloadFile = ftpFile;
        this.from = from;
        this.to = to;
        downloadEntity = CacheManager.getInstance().newDownloadEntity(ftpFile, from, to);
    }

    @Override
    public void run() {
        CommunicationEvent.sendDownload(CommunicationEvent.State.START);
        downloadFile(new DFile(downloadFile, from, to));
        if(!isBreak) {
            CacheManager.getInstance().clearDownload();
            CommunicationEvent.sendDownload(CommunicationEvent.State.FINISH);
        }
    }

    private void downloadFile(DFile dFile) {
        if(isBreak || downloadEntity.isDownloaded(dFile)) return;
        String newFromFilePatch = dFile.getFrom() + dFile.getName();
        String newToFilePatch = dFile.getTo() + dFile.getName();
        File newToFile = new File(newToFilePatch);
        if (dFile.isDir()) {
            Log.d(TAG, "Try read dir " + newFromFilePatch);
            newToFile.mkdir();
            try {
                for (FTPFile ftpFile : ftpClient.list(dFile.getFrom() + dFile.getName())) {
                    if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) continue;
                    downloadFile(new DFile(FTPFileAdapter.create(ftpFile), newFromFilePatch + File.separator, newToFilePatch + File.separator));
                }
            } catch (Exception e) {
                CommunicationEvent.sendDownload(CommunicationEvent.State.ERROR, e.getMessage());
                e.printStackTrace();
                isBreak = true;
            }
        } else {
            downloadEntity.setCurrentFile(dFile);
            if (newToFile.exists()) {
                CommunicationEvent.sendFileDownload(CommunicationEvent.State.PRESENT, dFile);
                isBreak = true;
            } else {
                Log.d(TAG, "Try download file " + newFromFilePatch);
                try {
                    ftpClient.download(newFromFilePatch, newToFile, DownloadTask.this);
                } catch (Exception e) {
                    CommunicationEvent.sendDownload(CommunicationEvent.State.ERROR, e.getMessage());
                    e.printStackTrace();
                    isBreak = true;
                }
            }
            if(!isBreak)
                downloadEntity.setDownloaded();
        }
    }

/*    private boolean downloadFile(FFile fFile, String from, String to) {
        if(!CustomApplication.getInstance().isStoppedDownload()) return true;

        if(fFile.isDir()) {
            String newFrom = from + fFile.getName() + File.separator;
            String newTo = to + fFile.getName() + File.separator;
            File newDir = new File(newTo);
            if(newDir.exists() || newDir.mkdir()) {
                FTPFile[] filesArray = new FTPFile[]{};
                Log.d(TAG, "Try read dir " + newFrom);
                try {
                    filesArray = ftpClient.list(newFrom);
                } catch (Exception e) {
                    CommunicationEvent.sendDownload(CommunicationEvent.State.ERROR, e.getMessage());
                    isBreak = true;
                    e.printStackTrace();
                }
                if(filesArray.length > 0)
                    for (FTPFile newFtpFile : filesArray) {
                        if(newFtpFile.getName().equals(".") || newFtpFile.getName().equals("..")) continue;
                        String ftpFilePath = newFrom + newFtpFile.getName() + File.separator;
                        if(!CacheManager.getInstance().isDownloaded(ftpFilePath)) {
                            File newFile = new File(to + fFile.getName());
                            dFile = new DFile(fFile, from, to);
                            if(newFile.exists() && state.equals(State.NEW)) {
                                CommunicationEvent.sendFileDownload(CommunicationEvent.State.PRESENT, dFile);
                            } else {
                                if(newFile.exists() && state.equals(State.SKIP_ALL)) return true;
                                if(newFile.exists() && state.equals(State.SKIP)) {
                                    this.state = State.NEW;
                                    CacheManager.getInstance().setDownloaded(ftpFilePath);
                                    return true;
                                }
                                if(newFile.exists() && state.equals(State.OVERWRITE)) {
                                    this.state = State.NEW;
                                }
                                Log.d(TAG, "Try download " + newFile.getName() + " => " + newFile.getPath());
                                try {
                                    ftpClient.download(ftpFilePath, newFile, DownloadTask.this);
                                    CacheManager.getInstance().setDownloaded(ftpFilePath);
                                } catch (Exception e) {
                                    CommunicationEvent.sendDownload(CommunicationEvent.State.ERROR, e.getMessage());
                                    isBreak = true;
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
            } else {
                CommunicationEvent.sendDownload(CommunicationEvent.State.ERROR, context.getString(R.string.failed_to_write_file));
                isBreak = true;
            }
        } else {

        }
        return !isBreak;
    }*/

    @Override
    public void started() {
        CommunicationEvent.sendFileDownload(CommunicationEvent.State.START, downloadEntity.getCurrentFile());
    }

    @Override
    public void transferred(int i) {
        downloadEntity.getCurrentFile().addProgress(i);
        CommunicationEvent.sendFileDownload(CommunicationEvent.State.PROGRESS, downloadEntity.getCurrentFile());
    }

    @Override
    public void completed() {

    }

    @Override
    public void aborted() {
        CommunicationEvent.sendFileDownload(CommunicationEvent.State.ERROR, downloadEntity.getCurrentFile());
    }

    @Override
    public void failed() {
        CommunicationEvent.sendFileDownload(CommunicationEvent.State.ERROR, downloadEntity.getCurrentFile());
    }
}
