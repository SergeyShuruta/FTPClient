package com.shuruta.sergey.ftpclient.entity;

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
        return String.valueOf((this.totalSize / this.totalProgress) * 100);
    }

    @Override
    public void started() {

    }

    @Override
    public void transferred(int i) {
        downloadList.get(index-1).addProgress(i);
        totalProgress += i;
    }

    @Override
    public void completed() {
        downloadList.get(index-1).setCompleted();
    }

    @Override
    public void aborted() {

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
