package com.shuruta.sergey.ftpclient.tasks;

import android.content.Context;
import android.os.Bundle;

import com.shuruta.sergey.ftpclient.Constants;
import com.shuruta.sergey.ftpclient.adapters.FTPFileAdapter;
import com.shuruta.sergey.ftpclient.cache.CacheManager;
import com.shuruta.sergey.ftpclient.EventBusMessenger;
import com.shuruta.sergey.ftpclient.services.FtpService;
import com.shuruta.sergey.ftpclient.entity.Connection;
import com.shuruta.sergey.ftpclient.interfaces.FFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.greenrobot.event.EventBus;
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
public class ReadListTask extends Task {

    private FTPClient ftpClient;
    private Connection connection;

    public static final String TAG = FtpService.TAG + "." + ReadListTask.class.getSimpleName();

    public ReadListTask(Context context, FTPClient ftpClient, Connection connection) {
        super(context);
        this.ftpClient = ftpClient;
        this.connection = connection;
    }

    @Override
    public void run() {
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.PARAM_CONNECTION_ID, connection.getId());
        EventBusMessenger.sendFtpMessage(EventBusMessenger.Event.START, bundle);
        try {
            prepareAndPutToCache(ftpClient.list(connection.getDir()));
            EventBusMessenger.sendFtpMessage(EventBusMessenger.Event.OK, bundle);
        } catch (IOException e) {
            EventBusMessenger.sendFtpMessage(EventBusMessenger.Event.ERROR, bundle);
            e.printStackTrace();
        } catch (FTPIllegalReplyException e) {
            EventBusMessenger.sendFtpMessage(EventBusMessenger.Event.ERROR, bundle);
            e.printStackTrace();
        } catch (FTPException e) {
            EventBusMessenger.sendFtpMessage(EventBusMessenger.Event.ERROR, bundle);
            e.printStackTrace();
        } catch (FTPDataTransferException e) {
            EventBusMessenger.sendFtpMessage(EventBusMessenger.Event.ERROR, bundle);
            e.printStackTrace();
        } catch (FTPAbortedException e) {
            EventBusMessenger.sendFtpMessage(EventBusMessenger.Event.ERROR, bundle);
            e.printStackTrace();
        } catch (FTPListParseException e) {
            EventBusMessenger.sendFtpMessage(EventBusMessenger.Event.ERROR, bundle);
            e.printStackTrace();
        }
        EventBusMessenger.sendFtpMessage(EventBusMessenger.Event.FINISH, bundle);
    }

    private void prepareAndPutToCache(FTPFile[] list) {
        List<FFile> ftpFiles = new ArrayList<>();
        for(int i = 0; i < list.length; i++) {
            if(list[i].getName().equals("..")) continue;
            ftpFiles.add(FTPFileAdapter.create(list[i]));
        }
        Collections.sort(ftpFiles, new Comparator<FFile>() {
            @Override
            public int compare(final FFile object1, final FFile object2) {
                if (object1.isDir() && object2.isFile())
                    return -1;
                if (object1.isFile() && object2.isDir())
                    return 1;
                return object1.getName().compareTo(object2.getName());
            }
        });
        CacheManager.getInstance().putFtpFiles(ftpFiles);
    }
}
