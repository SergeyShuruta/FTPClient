package com.shuruta.sergey.ftpclient.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey on 12.12.2015.
 */
public class DownloadEntity {

    private int index;
    private List<DFile> downloadList = new ArrayList<>();

    public void putFile(DFile file) {
        this.downloadList.add(file.clone());
    }

    public boolean next() {
        return downloadList.size() > index;
    }

    public DFile getNext() {
        return downloadList.get(index++);
    }
}
