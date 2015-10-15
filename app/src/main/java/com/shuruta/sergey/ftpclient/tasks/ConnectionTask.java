package com.shuruta.sergey.ftpclient.tasks;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.shuruta.sergey.ftpclient.EventBusMessenger;
import com.shuruta.sergey.ftpclient.services.FtpService;
import com.shuruta.sergey.ftpclient.entity.Connection;

import java.io.File;
import java.io.IOException;

import de.greenrobot.event.EventBus;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

/**
 * Author: Sergey Shuruta
 * Date: 08/15/15
 * Time: 22:11
 */
public class ConnectionTask extends Task {

    private FTPClient ftpClient;
    private Connection connection;

    public static final String TAG = FtpService.TAG + "." + ConnectionTask.class.getSimpleName();

    public ConnectionTask(Context context, FTPClient ftpClient, Connection connection) {
        super(context);
        this.ftpClient = ftpClient;
        this.connection = connection;
    }

    @Override
    public void run() {
        Bundle bundle = new Bundle();
        bundle.putLong("connection_id", connection.getId());
        EventBus.getDefault().post(new EventBusMessenger(bundle, EventBusMessenger.State.CONNECTION_START));

        try {
            ftpClient.connect(connection.getHost(), connection.getPort());
            ftpClient.login(connection.getLogin(), connection.getPassw());
            ftpClient.setType(FTPClient.TYPE_BINARY);
            ftpClient.changeDirectory(File.separator);
        } catch (IOException e) {
            e.printStackTrace();
            EventBus.getDefault().post(new EventBusMessenger(bundle, EventBusMessenger.State.CONNECTION_ERROR));
        } catch (FTPIllegalReplyException e) {
            e.printStackTrace();
            EventBus.getDefault().post(new EventBusMessenger(bundle, EventBusMessenger.State.CONNECTION_ERROR));
        } catch (FTPException e) {
            Log.d(TAG, "Already connected: " + e.getCode());
            e.printStackTrace();
            EventBus.getDefault().post(new EventBusMessenger(bundle, EventBusMessenger.State.CONNECTION_ERROR));
        }

        if(ftpClient.isConnected()) {
            if(ftpClient.isAuthenticated()) {
                EventBus.getDefault().post(new EventBusMessenger(bundle, EventBusMessenger.State.CONNECTION_OK));
            } else {
                EventBus.getDefault().post(new EventBusMessenger(bundle, EventBusMessenger.State.CONNECTION_ERROR_AUTHORIZATION));
            }
        } else {
            EventBus.getDefault().post(new EventBusMessenger(bundle, EventBusMessenger.State.CONNECTION_ERROR));
        }
        EventBus.getDefault().post(new EventBusMessenger(bundle, EventBusMessenger.State.CONNECTION_FINISH));
    }
}
