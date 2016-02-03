package com.shuruta.sergey.ftpclient.tasks;

import android.content.Context;
import android.util.Log;

import com.shuruta.sergey.ftpclient.CustomApplication;
import com.shuruta.sergey.ftpclient.R;
import com.shuruta.sergey.ftpclient.adapters.FTPFileAdapter;
import com.shuruta.sergey.ftpclient.cache.CacheManager;
import com.shuruta.sergey.ftpclient.entity.DFile;
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
    private DFile dFile;
    private String from, to;
    private State state;
    private boolean isBreak = false;

    private static String TAG = DownloadTask.class.getSimpleName();

    public enum State {
        NEW,
        OVERWRITE,
        OVERWRITE_ALL,
        SKIP,
        SKIP_ALL,
    }

    public DownloadTask(Context context, FTPClient ftpClient, FFile ftpFile, String from, String to, State state) {
        super(context);
        this.context = context;
        this.ftpClient = ftpClient;
        this.downloadFile = ftpFile;
        this.from = from;
        this.to = to;
        this.state = state;
    }

    @Override
    public void run() {
        CommunicationEvent.sendDownload(CommunicationEvent.State.START);
        downloadFile(new DFile(downloadFile, from, to));
        if(!isBreak) {
            CacheManager.getInstance().clearDownloaded();
            CustomApplication.getInstance().breakDownload();
        }
        CommunicationEvent.sendDownload(CommunicationEvent.State.FINISH);
    }

    private void downloadFile(DFile dFile) {
        if (!CustomApplication.getInstance().isStoppedDownload() || isBreak) return;
        String newFromFilePatch = dFile.getFrom() + dFile.getName();
        String newToFilePatch = dFile.getTo() + dFile.getName();
        File newToFile = new File(newToFilePatch);
        if (dFile.isDir()) {
            if (!newToFile.exists()) newToFile.mkdir();
            Log.d(TAG, "Try read dir " + newFromFilePatch);
            try {
                for (FTPFile ftpFile : ftpClient.list(dFile.getFrom() + dFile.getName())) {
                    if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) continue;
                    downloadFile(new DFile(FTPFileAdapter.create(ftpFile), newFromFilePatch + File.separator, newToFilePatch + File.separator));
                }
            } catch (Exception e) {
                CommunicationEvent.sendDownload(CommunicationEvent.State.ERROR, e.getMessage());
                isBreak = true;
                e.printStackTrace();
            }
        } else if (!CacheManager.getInstance().isDownloaded(newFromFilePatch)) {
            Log.d(TAG, "Label 1");
            if (newToFile.exists() && state.equals(State.NEW)) {
                CommunicationEvent.sendFileDownload(CommunicationEvent.State.PRESENT, dFile);
                isBreak = true;
            } else if (!(newToFile.exists() && state.equals(State.SKIP_ALL))) {
                Log.d(TAG, "Label 2");
                if (newToFile.exists() && state.equals(State.SKIP)) {
                    this.state = State.NEW;
                    CacheManager.getInstance().setDownloaded(newToFilePatch);
                    Log.d(TAG, "Label 3");
                } else {
                    if (newToFile.exists() && state.equals(State.OVERWRITE)) {
                        this.state = State.NEW;
                    }
                    Log.d(TAG, "Try download file " + newFromFilePatch);
                    try {
                        ftpClient.download(newFromFilePatch, newToFile, DownloadTask.this);
                        CacheManager.getInstance().setDownloaded(newToFilePatch);
                    } catch (Exception e) {
                        CommunicationEvent.sendDownload(CommunicationEvent.State.ERROR, e.getMessage());
                        isBreak = true;
                        e.printStackTrace();
                    }
                }
            }
        } else {
            Log.d(TAG, "Label 4");
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
        CommunicationEvent.sendFileDownload(CommunicationEvent.State.START, dFile);
    }

    @Override
    public void transferred(int i) {
        dFile.addProgress(i);
        CommunicationEvent.sendFileDownload(CommunicationEvent.State.PROGRESS, dFile);
    }

    @Override
    public void completed() {

    }

    @Override
    public void aborted() {
        CommunicationEvent.sendFileDownload(CommunicationEvent.State.ERROR, dFile);
    }

    @Override
    public void failed() {
        CommunicationEvent.sendFileDownload(CommunicationEvent.State.ERROR, dFile);
    }
}
