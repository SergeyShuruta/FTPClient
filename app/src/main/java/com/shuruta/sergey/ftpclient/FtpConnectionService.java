package com.shuruta.sergey.ftpclient;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.shuruta.sergey.ftpclient.database.entity.Connection;

import de.greenrobot.event.EventBus;

/**
 * Created by Sergey on 12.08.2015.
 */
public class FtpConnectionService extends Service {

    private Connection connection;

    public static final String TAG = FtpConnectionService.class.getSimpleName();

    public static class GUIMessage {

        public final long conId;
        public final State state;

        public GUIMessage(long conId, State state) {
            this.conId = conId;
            this.state = state;
        }

        public enum State {
            PREPARE,
            CONNECTED,
            ERROR,
            CLOSED
        }
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

        public FtpConnectionService getService() {
            return FtpConnectionService.this;
        }
    }

    public void startConnection(Connection connection) {
        this.connection = connection;
        new ConnectToFtp().execute();
        Log.d(TAG, "startConnection()");
    }

    public void displayList() {
        Log.d(TAG, "displayList()");
    }

    private class ConnectToFtp extends AsyncTask<String, Boolean, GUIMessage.State> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            EventBus.getDefault().post(new GUIMessage(connection.getId(), GUIMessage.State.PREPARE));
        }

        @Override
        protected GUIMessage.State doInBackground(String... params) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return GUIMessage.State.CONNECTED;
        }

        @Override
        protected void onPostExecute(GUIMessage.State result) {
            super.onPostExecute(result);
            EventBus.getDefault().post(new GUIMessage(connection.getId(), result));
        }
    }
}
