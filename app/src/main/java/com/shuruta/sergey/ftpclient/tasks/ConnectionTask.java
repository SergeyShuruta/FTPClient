package com.shuruta.sergey.ftpclient.tasks;

import android.content.Context;

import com.shuruta.sergey.ftpclient.R;
import com.shuruta.sergey.ftpclient.event.CommunicationEvent;
import com.shuruta.sergey.ftpclient.services.FtpService;
import com.shuruta.sergey.ftpclient.entity.Connection;

import java.io.File;

import it.sauronsoftware.ftp4j.FTPClient;

/**
 * Author: Sergey Shuruta
 * Date: 08/15/15
 * Time: 22:11
 */
public class ConnectionTask extends Task {

    private Context context;
    private FTPClient ftpClient;
    private Connection connection;

    public static final String TAG = FtpService.TAG + "." + ConnectionTask.class.getSimpleName();

    public ConnectionTask(Context context, FTPClient ftpClient, Connection connection) {
        super(context);
        this.context = context;
        this.ftpClient = ftpClient;
        this.connection = connection;
    }

    @Override
    public void run() {
        CommunicationEvent.send(CommunicationEvent.State.START, connection);
        try {
            ftpClient.connect(connection.getHost(), connection.getPort());
            ftpClient.setPassive(true);
            ftpClient.setType(FTPClient.TYPE_BINARY);
            if(connection.isAuth()) {
                ftpClient.login(connection.getLogin(), connection.getPassw());
            }
            ftpClient.changeDirectory(File.separator);
            ftpClient.setCompressionEnabled(ftpClient.isCompressionSupported());
        } catch (Exception e) {
            CommunicationEvent.send(CommunicationEvent.State.ERROR, connection, e.getMessage());
            e.printStackTrace();
        }
        if(ftpClient.isConnected()) {
            if(ftpClient.isAuthenticated()) {
                CommunicationEvent.send(CommunicationEvent.State.FINISH, connection);
            } else {
                CommunicationEvent.send(CommunicationEvent.State.ERROR, connection, context.getString(R.string.connection_auth_error));
            }
        } else {
            CommunicationEvent.send(CommunicationEvent.State.ERROR, connection, context.getString(R.string.connection_error));
        }
    }
}
