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
    private boolean isCloseSession;

    public static final String TAG = FtpService.TAG + "." + DisconnectTask.class.getSimpleName();

    public DisconnectTask(Context context, FTPClient ftpClient, boolean isCloseSession) {
        super(context);
        this.ftpClient = ftpClient;
        this.isCloseSession = isCloseSession;
    }

    @Override
    public void run() {
        CommunicationEvent.sendDisconnection(CommunicationEvent.State.START, isCloseSession);
        CacheManager.getInstance().clearCache();
        try {
            ftpClient.disconnect(true);
            CommunicationEvent.sendDisconnection(CommunicationEvent.State.FINISH, isCloseSession);
        } catch (Exception e) {
            CommunicationEvent.sendDisconnection(CommunicationEvent.State.ERROR, e.getMessage(), isCloseSession);
            e.printStackTrace();
        }
    }
}
