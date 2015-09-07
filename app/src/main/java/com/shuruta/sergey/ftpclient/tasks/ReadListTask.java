package com.shuruta.sergey.ftpclient.tasks;

import android.content.Context;

import com.shuruta.sergey.ftpclient.CacheManager;
import com.shuruta.sergey.ftpclient.EventBusMessenger;
import com.shuruta.sergey.ftpclient.FFile;
import com.shuruta.sergey.ftpclient.FtpService;
import com.shuruta.sergey.ftpclient.database.entity.Connection;

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
        EventBus.getDefault().post(new EventBusMessenger(connection.getId(), EventBusMessenger.State.READ_FTP_LIST_START));

        FTPFile[] list = new FTPFile[0];
        try {
            list = ftpClient.list(connection.getDir());
            EventBus.getDefault().post(new EventBusMessenger(connection.getId(), EventBusMessenger.State.READ_FTP_LIST_OK));
        } catch (IOException e) {
            EventBus.getDefault().post(new EventBusMessenger(connection.getId(), EventBusMessenger.State.READ_FTP_LIST_ERROR));
            e.printStackTrace();
        } catch (FTPIllegalReplyException e) {
            EventBus.getDefault().post(new EventBusMessenger(connection.getId(), EventBusMessenger.State.READ_FTP_LIST_ERROR));
            e.printStackTrace();
        } catch (FTPException e) {
            EventBus.getDefault().post(new EventBusMessenger(connection.getId(), EventBusMessenger.State.READ_FTP_LIST_ERROR));
            e.printStackTrace();
        } catch (FTPDataTransferException e) {
            EventBus.getDefault().post(new EventBusMessenger(connection.getId(), EventBusMessenger.State.READ_FTP_LIST_ERROR));
            e.printStackTrace();
        } catch (FTPAbortedException e) {
            EventBus.getDefault().post(new EventBusMessenger(connection.getId(), EventBusMessenger.State.READ_FTP_LIST_ERROR));
            e.printStackTrace();
        } catch (FTPListParseException e) {
            EventBus.getDefault().post(new EventBusMessenger(connection.getId(), EventBusMessenger.State.READ_FTP_LIST_ERROR));
            e.printStackTrace();
        }
        List<FFile> ftpFiles = new ArrayList<FFile>();
        for(int i = 0; i < list.length; i++) {
            if(list[i].getName().equals("..")) continue;
            ftpFiles.add(new FFile(list[i]));
        }
        Collections.sort(ftpFiles, new Comparator<FFile>() {
            @Override
            public int compare(final FFile object1, final FFile object2) {
                if (object1.isDirectory() && object2.isFile())
                    return -1;
                if (object1.isFile() && object2.isDirectory())
                    return 1;
                return object1.getName().compareTo(object2.getName());
            }
        });
        CacheManager.getInstance().putFtpFiles(ftpFiles);
        EventBus.getDefault().post(new EventBusMessenger(connection.getId(), EventBusMessenger.State.READ_FTP_LIST_FINISH));
    }
}
