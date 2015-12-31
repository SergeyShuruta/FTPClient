package com.shuruta.sergey.ftpclient.tasks;

import android.content.Context;
import com.shuruta.sergey.ftpclient.Constants;
import com.shuruta.sergey.ftpclient.event.CommunicationEvent;
import com.shuruta.sergey.ftpclient.adapters.LocalFileAdapter;
import com.shuruta.sergey.ftpclient.cache.CacheManager;
import com.shuruta.sergey.ftpclient.interfaces.FFile;
import com.shuruta.sergey.ftpclient.services.FtpService;
import com.shuruta.sergey.ftpclient.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
        CommunicationEvent.sendReadList(CommunicationEvent.State.START, Constants.TYPE_LOCAL);
        File dir = new File(path);
        try {
            File filesArray[] = dir.listFiles();
            List<FFile> files = new ArrayList<>();
            if(null != filesArray) {
                files.add(LocalFileAdapter.create());
                for (int i = 0; i < filesArray.length; i++) {
                    if (filesArray[i].getName().equals("..")) continue;
                    files.add(LocalFileAdapter.create(filesArray[i]));
                }
                Utils.sortFiles(files);
            }
            CacheManager.getInstance().putFiles(files, Constants.TYPE_LOCAL);
            CommunicationEvent.sendReadList(CommunicationEvent.State.FINISH, Constants.TYPE_LOCAL);
        } catch (Exception e) {
            CommunicationEvent.sendReadList(CommunicationEvent.State.ERROR, Constants.TYPE_LOCAL, e.getMessage());
            e.printStackTrace();
        }
    }
}
