package com.shuruta.sergey.ftpclient.tasks;

import android.content.Context;

import com.shuruta.sergey.ftpclient.Constants;
import com.shuruta.sergey.ftpclient.adapters.FTPFileAdapter;
import com.shuruta.sergey.ftpclient.cache.CacheManager;
import com.shuruta.sergey.ftpclient.event.CommunicationEvent;
import com.shuruta.sergey.ftpclient.services.FtpService;
import com.shuruta.sergey.ftpclient.interfaces.FFile;
import com.shuruta.sergey.ftpclient.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPFile;

/**
 * Author: Sergey Shuruta
 * Date: 08/15/15
 * Time: 22:11
 */
public class FtpReadListTask extends Task {

    private final FTPClient ftpClient;
    private final String path;

    public static final String TAG = FtpService.TAG + "." + FtpReadListTask.class.getSimpleName();

    public FtpReadListTask(Context context, FTPClient ftpClient, String path) {
        super(context);
        this.ftpClient = ftpClient;
        this.path = path;
    }

    @Override
    public void run() {
        CommunicationEvent.send(CommunicationEvent.State.START, Constants.TYPE_FTP);
        try {
            FTPFile[] filesArray = ftpClient.list(path);
            List<FFile> files = new ArrayList<>();
            for(int i = 0; i < filesArray.length; i++) {
                if(filesArray[i].getName().equals("..")) continue;
                files.add(FTPFileAdapter.create(filesArray[i]));
            }
            Utils.sortFiles(files);
            CacheManager.getInstance().putFiles(files, Constants.TYPE_FTP);
            CommunicationEvent.send(CommunicationEvent.State.FINISH, Constants.TYPE_FTP);
        } catch (Exception e) {
            CommunicationEvent.send(CommunicationEvent.State.ERROR, Constants.TYPE_FTP, e.getMessage());
            e.printStackTrace();
        }
    }
}
