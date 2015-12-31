package com.shuruta.sergey.ftpclient.entity;

import android.util.Log;

import com.shuruta.sergey.ftpclient.event.CommunicationEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import it.sauronsoftware.ftp4j.FTPDataTransferListener;

/**
 * Created by Sergey on 12.12.2015.
 */
public class DownloadEntity implements Iterable<DFile>, FTPDataTransferListener {

    private int index = 0;
    private List<DFile> downloadList = new ArrayList<>();
    private long totalProgress = 0;
    private long totalSize = 0;
    private String remotePath, localPath;

    public DownloadEntity(String remotePath, String localPath) {
        this.remotePath = remotePath;
        this.localPath = localPath;
    }

    public void putFile(DFile file) {
        if(file.isFile())
            totalSize += file.getSize();
        this.downloadList.add(file.clone());
    }

    @Override
    public Iterator<DFile> iterator() {
        return new DownloadEntityIterator();
    }

    public String getTotalProgress() {
        return String.valueOf(getTotalProgressInt());
    }

    public int getTotalProgressInt() {
        return (int)(100*(double)this.totalProgress/Double.valueOf(this.totalSize));
    }

    public int getSize() {
        return downloadList.size();
    }

    public int getCompletedSize() {
        return index-1;
    }

    public String getLocalPath() {
        return localPath;
    }

    public String getRemotePath() {
        return remotePath;
    }

    @Override
    public void started() {
        CommunicationEvent.sendFileDownload(CommunicationEvent.State.START, downloadList.get(index-1));
    }

    @Override
    public void transferred(int i) {
        downloadList.get(index-1).addProgress(i);
        totalProgress += i;
        CommunicationEvent.sendFileDownload(CommunicationEvent.State.PROGRESS, downloadList.get(index-1), getTotalProgress());
    }

    @Override
    public void completed() {
        downloadList.get(index-1).setCompleted();
        CommunicationEvent.sendFileDownload(CommunicationEvent.State.FINISH, downloadList.get(index - 1));
    }

    @Override
    public void aborted() {
        CommunicationEvent.sendFileDownload(CommunicationEvent.State.ABORTED, downloadList.get(index - 1));
    }

    @Override
    public void failed() {
        downloadList.get(index-1).setIsFiled();
    }

    private class DownloadEntityIterator implements Iterator<DFile> {

        @Override
        public boolean hasNext() {
            return downloadList.size() > index;
        }

        @Override
        public DFile next() {
            return downloadList.get(index++);
        }

        @Override
        public void remove() {
            downloadList.remove(index--);
        }
    }
}
