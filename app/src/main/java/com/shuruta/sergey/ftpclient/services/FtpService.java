package com.shuruta.sergey.ftpclient.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.shuruta.sergey.ftpclient.Constants;
import com.shuruta.sergey.ftpclient.CustomApplication;
import com.shuruta.sergey.ftpclient.entity.Connection;
import com.shuruta.sergey.ftpclient.interfaces.FFile;
import com.shuruta.sergey.ftpclient.tasks.ConnectionTask;
import com.shuruta.sergey.ftpclient.tasks.DisconnectTask;
import com.shuruta.sergey.ftpclient.tasks.DownloadTask;
import com.shuruta.sergey.ftpclient.tasks.FtpReadListTask;
import com.shuruta.sergey.ftpclient.tasks.LocalReadListTask;
import com.shuruta.sergey.ftpclient.tasks.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPCommunicationListener;

/**
 * Created by Sergey on 12.08.2015.
 */
public class FtpService extends Service {

    private ExecutorService executorService;
    private FTPClient ftpClient;

    public static final String TAG = FtpService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        executorService = Executors.newFixedThreadPool(10);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ConnectionBinder();
    }

    public class ConnectionBinder extends Binder {

        public FtpService getService() {
            return FtpService.this;
        }
    }

    public void startConnection(Connection connection) {
        Log.d(TAG, "startConnection()");
        this.ftpClient = new FTPClient();
        this.ftpClient.addCommunicationListener(ftpCommunicationListener);
        executorService.execute(new ConnectionTask(this, ftpClient, connection));
    }

    public void readList(int listType) {
        Log.d(TAG, "readList(" + listType + ")");
        String path = CustomApplication.getInstance().getPath(listType);
        Task task = listType == Constants.TYPE_FTP
                ? new FtpReadListTask(this, ftpClient, path)
                : new LocalReadListTask(this, path);
        executorService.execute(task);
    }

    public void disconnectAndClose() {
        Log.d(TAG, "disconnectAndClose()");
        executorService.execute(new DisconnectTask(this, ftpClient, true));
    }

    public void disconnect() {
        Log.d(TAG, "disconnect()");
        executorService.execute(new DisconnectTask(this, ftpClient, false));
    }

/*    public void preDownload(FFile file, String from, String to) {
        Log.d(TAG, "preDownload(" + file.getName() + ")");
        executorService.execute(new PreDownloadTask(FtpService.this, ftpClient, file, from, to, FtpService.this));
    }*/

    public void download(FFile file, String from, String to) {
        Log.d(TAG, "download(" + file.getName() + ", " + from + ", " + to + ")");
        executorService.execute(new DownloadTask(FtpService.this, ftpClient, file, from, to));
    }

    FTPCommunicationListener ftpCommunicationListener = new FTPCommunicationListener()
    {
        @Override
        public void sent(String command)
        {
            Log.d(TAG, command);
        }

        @Override
        public void received(String response)
        {
            Log.d(TAG, response);
        }
    };
}
