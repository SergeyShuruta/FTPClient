package com.shuruta.sergey.ftpclient.tasks;

import android.content.Context;
import com.shuruta.sergey.ftpclient.interfaces.FFile;
import it.sauronsoftware.ftp4j.FTPClient;

/**
 * Created by Sergey Shuruta
 * 08.12.2015 at 15:57
 */
public class PrepForDownloadTask extends Task {

    private FTPClient ftpClient;
    private FFile file;

    public PrepForDownloadTask(Context context, FTPClient ftpClient, FFile file) {
        super(context);
        this.ftpClient = ftpClient;
        this.file = file;
    }

    @Override
    public void run() {

    }
}
