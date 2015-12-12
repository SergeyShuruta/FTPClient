package com.shuruta.sergey.ftpclient.tasks;

import android.content.Context;
import android.util.Log;

import com.shuruta.sergey.ftpclient.adapters.FTPFileAdapter;
import com.shuruta.sergey.ftpclient.entity.DFile;
import com.shuruta.sergey.ftpclient.entity.DownloadEntity;
import com.shuruta.sergey.ftpclient.interfaces.FFile;
import com.shuruta.sergey.ftpclient.utils.Utils;

import java.io.File;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPFile;

/**
 * Created by Sergey Shuruta
 * 08.12.2015 at 15:57
 */
public class PrepForDownloadTask extends Task {

    private FTPClient ftpClient;
    private FFile file;
    private String from, to;

    public PrepForDownloadTask(Context context, FTPClient ftpClient, FFile file, String from, String to) {
        super(context);
        this.ftpClient = ftpClient;
        this.file = file;
        this.from = from;
        this.to = to;
    }

    @Override
    public void run() {
        DownloadEntity downloadEntity = addToDownload(file, from, to);
        do{
            DFile file = downloadEntity.getNext();
            Log.d("TEST", "Add to download: " + file.getFrom() + file.getName() + "|" + file.getTo());
        } while (downloadEntity.next());
    }

    private DownloadEntity addToDownload(FFile file, String from, String to) {
        return addToDownload(new DFile(file, from, to), null);
    }

    private DownloadEntity addToDownload(DFile file, DownloadEntity entity) {
        if(null == entity) {
            entity = new DownloadEntity();
        }
        if(file.isDir()) {
            try {
                String newFrom = file.getFrom().concat(file.getName()).concat(File.separator);
                String newTo = file.getTo().concat(file.getName()).concat(File.separator);
                FTPFile[] filesArray = ftpClient.list(newFrom);
                for(int i = 0; i < filesArray.length; i++) {
                    if(filesArray[i].getName().equals("..")
                            || filesArray[i].getName().equals(".")) continue;
                    addToDownload(new DFile(FTPFileAdapter.create(filesArray[i]), newFrom, newTo), entity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(file.isFile()){
            entity.putFile(file);
        }
        return entity;
    }
}
