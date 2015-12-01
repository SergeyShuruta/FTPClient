/*
package com.shuruta.sergey.ftpclient.ui.fragments;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.shuruta.sergey.ftpclient.Constants;
import com.shuruta.sergey.ftpclient.cache.CacheManager;
import com.shuruta.sergey.ftpclient.services.FtpService;
import com.shuruta.sergey.ftpclient.interfaces.FFile;

import java.util.List;

*/
/**
 * Created by Sergey Shuruta
 * 08/15/15 at 22:11
 *//*

public class FtpFragment extends FilesFragment {

    private FtpService mFtpConnectionService;
    private boolean bound;

    public static final String TAG = FtpFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initList(Constants.TYPE_FTP);
    }

*/
/*    @Override
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
    }*//*


*/
/*    @Override
    public List<FFile> getFiles() {

        return CacheManager.getInstance().getFiles();
    }*//*


*/
/*    @Override
    public void readList(String patch) {

        mFtpConnectionService.readList(patch);
    }*//*


*/
/*    @Override
    public void disconnect() {
        super.disconnect();
        mFtpConnectionService.disconnect();
    }*//*


    @Override
    public void onFileClick(FFile ftpFile) {

    }

*/
/*    private ServiceConnection mServiceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d(TAG, "onServiceConnected()");
            mFtpConnectionService = ((FtpService.ConnectionBinder) binder).getService();
            bound = true;
        }

        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected()");
            bound = false;
        }
    };*//*

}
*/
