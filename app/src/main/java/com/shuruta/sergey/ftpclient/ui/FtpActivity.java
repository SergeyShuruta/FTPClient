package com.shuruta.sergey.ftpclient.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import com.shuruta.sergey.ftpclient.FtpService;
import com.shuruta.sergey.ftpclient.R;

import it.sauronsoftware.ftp4j.FTPFile;

/**
 * Author: Sergey Shuruta
 * Date: 08/15/15
 * Time: 22:11
 */
public class FtpActivity extends FragmentActivity implements FtpFragmentList.FtpFragmentListener {

    private boolean bound = false;
    private FtpService mFtpConnectionService;
    public static final String TAG = FtpActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftp);
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.frameLayout, new FtpFragmentList());
        fTrans.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(FtpActivity.this, FtpService.class), mServiceConnection, 0);
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
            mFtpConnectionService = ((FtpService.ConnectionBinder) binder).getService();
            bound = true;
        }

        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected()");
            bound = false;
        }
    };

    @Override
    public void onBack() {
        mFtpConnectionService.back();
    }

    @Override
    public void onDirClick(FTPFile ftpFile) {
        mFtpConnectionService.readList(ftpFile.getName());
    }

    @Override
    public void onFileClick(FTPFile ftpFile) {

    }
}
