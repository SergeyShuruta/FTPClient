package com.shuruta.sergey.ftpclient.tasks;

import android.content.Context;
import android.util.Log;

import com.shuruta.sergey.ftpclient.CacheManager;
import com.shuruta.sergey.ftpclient.EventBusMessenger;
import com.shuruta.sergey.ftpclient.FtpService;
import com.shuruta.sergey.ftpclient.database.entity.Connection;

import java.io.File;
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
        EventBus.getDefault().post(new EventBusMessenger(connection.getId(), EventBusMessenger.State.READ_LIST_START));

        FTPFile[] list = new FTPFile[0];
        try {
            list = ftpClient.list(connection.getDir());
            EventBus.getDefault().post(new EventBusMessenger(connection.getId(), EventBusMessenger.State.READ_LIST_OK));
        } catch (IOException e) {
            EventBus.getDefault().post(new EventBusMessenger(connection.getId(), EventBusMessenger.State.READ_LIST_ERROR));
            e.printStackTrace();
        } catch (FTPIllegalReplyException e) {
            EventBus.getDefault().post(new EventBusMessenger(connection.getId(), EventBusMessenger.State.READ_LIST_ERROR));
            e.printStackTrace();
        } catch (FTPException e) {
            EventBus.getDefault().post(new EventBusMessenger(connection.getId(), EventBusMessenger.State.READ_LIST_ERROR));
            e.printStackTrace();
        } catch (FTPDataTransferException e) {
            EventBus.getDefault().post(new EventBusMessenger(connection.getId(), EventBusMessenger.State.READ_LIST_ERROR));
            e.printStackTrace();
        } catch (FTPAbortedException e) {
            EventBus.getDefault().post(new EventBusMessenger(connection.getId(), EventBusMessenger.State.READ_LIST_ERROR));
            e.printStackTrace();
        } catch (FTPListParseException e) {
            EventBus.getDefault().post(new EventBusMessenger(connection.getId(), EventBusMessenger.State.READ_LIST_ERROR));
            e.printStackTrace();
        }
        List<FTPFile> ftpFiles = new ArrayList<FTPFile>();
        for(int i = 0; i < list.length; i++) {
            if(/*list[i].getName().equals(".") || */list[i].getName().equals("..")) continue;
            ftpFiles.add(list[i]);
        }
        Collections.sort(ftpFiles, new Comparator<FTPFile>() {
            @Override
            public int compare(final FTPFile object1, final FTPFile object2) {
                if (object1.getType() == FTPFile.TYPE_DIRECTORY && object2.getType() == FTPFile.TYPE_FILE)
                    return -1;
                if (object1.getType() == FTPFile.TYPE_FILE && object2.getType() == FTPFile.TYPE_DIRECTORY)
                    return 1;
                return object1.getName().compareTo(object2.getName());
            }
        });
        CacheManager.getInstance().putFtpFiles(ftpFiles);
        EventBus.getDefault().post(new EventBusMessenger(connection.getId(), EventBusMessenger.State.READ_LIST_FINISH));
    }
}
