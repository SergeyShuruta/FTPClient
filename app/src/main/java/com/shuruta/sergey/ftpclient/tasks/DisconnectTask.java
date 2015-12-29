package com.shuruta.sergey.ftpclient.tasks;

import android.content.Context;

import com.shuruta.sergey.ftpclient.event.CommunicationEvent;
import com.shuruta.sergey.ftpclient.cache.CacheManager;
import com.shuruta.sergey.ftpclient.services.FtpService;

import it.sauronsoftware.ftp4j.FTPClient;

/**
 * Created by Sergey on 28.11.2015.
 */
public class DisconnectTask extends Task {

    private final FTPClient ftpClient;

    public static final String TAG = FtpService.TAG + "." + DisconnectTask.class.getSimpleName();

    public DisconnectTask(Context context, FTPClient ftpClient) {
        super(context);
        this.ftpClient = ftpClient;
    }

    @Override
    public void run() {
        CacheManager.getInstance().clearCache();
        try {
            ftpClient.disconnect(true);
            CommunicationEvent.send(CommunicationEvent.Type.DISCONNECTION, CommunicationEvent.State.FINISH);
        } catch (Exception e) {
            CommunicationEvent.send(CommunicationEvent.Type.DISCONNECTION, CommunicationEvent.State.ERROR, e.getMessage());
            e.printStackTrace();
        }
    }
}
