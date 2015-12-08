package com.shuruta.sergey.ftpclient.tasks;

import android.content.Context;
import android.util.Log;

import com.shuruta.sergey.ftpclient.Constants;
import com.shuruta.sergey.ftpclient.EventBusMessenger;
import com.shuruta.sergey.ftpclient.adapters.FTPFileAdapter;
import com.shuruta.sergey.ftpclient.adapters.LocalFileAdapter;
import com.shuruta.sergey.ftpclient.cache.CacheManager;
import com.shuruta.sergey.ftpclient.interfaces.FFile;
import com.shuruta.sergey.ftpclient.services.FtpService;
import com.shuruta.sergey.ftpclient.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPListParseException;

/**
 * Author: Sergey Shuruta
 * Date: 08/15/15
 * Time: 22:11
 */
public class LocalReadListTask extends Task {

    private final String path;

    public static final String TAG = FtpService.TAG + "." + LocalReadListTask.class.getSimpleName();

    public LocalReadListTask(Context context, String path) {
        super(context);
        this.path = path;
    }

    @Override
    public void run() {
        EventBusMessenger.sendLocalMessage(EventBusMessenger.Event.START);
        Log.d("TEST", "Path: " + path);
        File dir = new File(path);
        File filesArray[] = dir.listFiles();
        List<FFile> files = new ArrayList<>();
        if(null != filesArray) {
            for (int i = 0; i < filesArray.length; i++) {
                Log.d("TEST", "File: " + filesArray[i].getName());
                if (filesArray[i].getName().equals("..")) continue;
                files.add(LocalFileAdapter.create(filesArray[i]));
            }
            Utils.sortFiles(files);
        }
        CacheManager.getInstance().putFiles(files, Constants.TYPE_LOCAL);
        EventBusMessenger.sendLocalMessage(EventBusMessenger.Event.OK);
        EventBusMessenger.sendLocalMessage(EventBusMessenger.Event.FINISH);
    }
}
