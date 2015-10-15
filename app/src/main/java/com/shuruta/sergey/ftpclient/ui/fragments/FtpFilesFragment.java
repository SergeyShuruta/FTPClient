package com.shuruta.sergey.ftpclient.ui.fragments;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.shuruta.sergey.ftpclient.cache.CacheManager;
import com.shuruta.sergey.ftpclient.EventBusMessenger;
import com.shuruta.sergey.ftpclient.services.FtpService;
import com.shuruta.sergey.ftpclient.interfaces.FFile;

import de.greenrobot.event.EventBus;

/**
 * Created by Sergey Shuruta
 * 08/15/15 at 22:11
 */
public class FtpFilesFragment extends FilesFragment {

    private FtpService mFtpConnectionService;
    private boolean bound;

    public static final String TAG = FtpFilesFragment.class.getSimpleName();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        displayList(CacheManager.getInstance().getFtpFiles());
    }

    @Override
    public void onStart() {
        super.onStart();
        mContext.bindService(new Intent(mContext, FtpService.class), mServiceConnection, 0);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!bound) return;
        mContext.unbindService(mServiceConnection);
        bound = false;
    }

    public void onEventMainThread(EventBusMessenger event) {
        Log.d(TAG, "onEvent: " + event.state);
        switch (event.state) {
            case REFRESH:
                mFtpConnectionService.readList();
                break;
            case BACK:
                onBack();
                break;
            case READ_FTP_LIST_START:
                startReadList();
                break;
            case READ_FTP_LIST_OK:
                displayList(CacheManager.getInstance().getFtpFiles());
                break;
            case READ_FTP_LIST_ERROR:
                errorReadList();
            case READ_FTP_LIST_FINISH:
                finishReadList();
                break;
        }
    }

    @Override
    public void onBack() {
        mFtpConnectionService.back();
    }

    @Override
    public void onDirClick(FFile ftpFile) {
        mFtpConnectionService.readList(ftpFile.getName());
    }

    @Override
    public void onFileClick(FFile ftpFile) {
    }

    @Override
    public int getListType() {
        return FilesFragment.TYPE_LIST_FTP;
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
}
