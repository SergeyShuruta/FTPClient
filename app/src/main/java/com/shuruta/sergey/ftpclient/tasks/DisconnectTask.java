package com.shuruta.sergey.ftpclient.tasks;

import android.content.Context;

import com.shuruta.sergey.ftpclient.EventBusMessenger;
import com.shuruta.sergey.ftpclient.adapters.FTPFileAdapter;
import com.shuruta.sergey.ftpclient.cache.CacheManager;
import com.shuruta.sergey.ftpclient.interfaces.FFile;
import com.shuruta.sergey.ftpclient.services.FtpService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

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

        try {
            ftpClient.disconnect(true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FTPIllegalReplyException e) {
            EventBusMessenger.sendFtpMessage(EventBusMessenger.Event.DISCONNECT_ERROR);
            e.printStackTrace();
        } catch (FTPException e) {
            EventBusMessenger.sendFtpMessage(EventBusMessenger.Event.DISCONNECT_ERROR);
            e.printStackTrace();
        }
        EventBusMessenger.sendFtpMessage(EventBusMessenger.Event.DISCONNECTED);
    }
}
