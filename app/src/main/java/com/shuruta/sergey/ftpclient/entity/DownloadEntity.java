package com.shuruta.sergey.ftpclient.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Sergey on 12.12.2015.
 */
public class DownloadEntity implements Iterable<DFile> {

    private int index;
    private List<DFile> downloadList = new ArrayList<>();

    public void putFile(DFile file) {
        this.downloadList.add(file.clone());
    }

    @Override
    public Iterator<DFile> iterator() {
        return null;
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

/*    public boolean next() {
        return downloadList.size() > index;
    }

    public DFile getNext() {
        return downloadList.get(index++);
    }*/
}
