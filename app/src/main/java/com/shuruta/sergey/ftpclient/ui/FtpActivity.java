package com.shuruta.sergey.ftpclient.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.shuruta.sergey.ftpclient.FtpConnectionService;
import com.shuruta.sergey.ftpclient.R;

/**
 * Created by Sergey on 12.08.2015.
 */
public class FtpActivity extends FragmentActivity {

    private boolean bound = false;
    private FtpConnectionService mFtpConnectionService;
    public static final String TAG = FtpActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftp);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(FtpActivity.this, FtpConnectionService.class), mServiceConnection, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!bound) return;
        unbindService(mServiceConnection);
        bound = false;
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d(TAG, "onServiceConnected()");
            mFtpConnectionService = ((FtpConnectionService.ConnectionBinder) binder).getService();
            mFtpConnectionService.displayList();
            bound = true;
        }

        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected()");
            bound = false;
        }
    };

}
