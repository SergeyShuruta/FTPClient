package com.shuruta.sergey.ftpclient;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.shuruta.sergey.ftpclient.database.entity.Connection;
import com.shuruta.sergey.ftpclient.tasks.ConnectionTask;
import com.shuruta.sergey.ftpclient.tasks.ReadListTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPCommunicationListener;

/**
 * Created by Sergey on 12.08.2015.
 */
public class FtpService extends Service {

    private Connection connection;
    ExecutorService executorService;
    private FTPClient ftpClient;

    public static final String TAG = FtpService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        this.ftpClient = new FTPClient();
        this.ftpClient.setType(FTPClient.TYPE_BINARY);
        this.ftpClient.addCommunicationListener(ftpCommunicationListener);
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

    public void onEventMainThread(EventBusMessenger event) {
        Log.d(TAG, "onEvent: " + event.state);
        switch (event.state) {
            case CONNECTION_OK:
                executorService.execute(new ReadListTask(this, ftpClient, connection));
                break;
        }
    }

    public class ConnectionBinder extends Binder {

        public FtpService getService() {
            return FtpService.this;
        }
    }

    public void startConnection(Connection connection) {
        Log.d(TAG, "startConnection()");
        this.connection = connection;
        executorService.execute(new ConnectionTask(this, ftpClient, connection));
    }

    public void readList() {
        readList(new String());
    }

    public void back() {
        Log.d(TAG, "back()");
        connection.backDir();
        executorService.execute(new ReadListTask(this, ftpClient, connection));
    }

    public void readList(String fileName) {
        Log.d(TAG, "readList(" + fileName + ")");
        connection.addDir(fileName);
        executorService.execute(new ReadListTask(this, ftpClient, connection));
    }

/*    public void disconnect() {
        Log.d(TAG, "disconnect()");
        mFTPManager.disconnect();
    }*/

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
