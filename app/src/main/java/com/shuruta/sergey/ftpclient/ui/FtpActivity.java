package com.shuruta.sergey.ftpclient.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import com.shuruta.sergey.ftpclient.EventBusMessenger;
import com.shuruta.sergey.ftpclient.FFile;
import com.shuruta.sergey.ftpclient.FtpService;
import com.shuruta.sergey.ftpclient.R;
import de.greenrobot.event.EventBus;

/**
 * Author: Sergey Shuruta
 * Date: 08/15/15
 * Time: 22:11
 */
public class FtpActivity extends FragmentActivity implements FtpFragmentList.FtpFragmentListener {

    private boolean bound = false;
    private FtpService mFtpConnectionService;
    public static final String TAG = FtpActivity.class.getSimpleName();
    private Menu manu;
    private boolean isListReading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftp);
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.frameLayout, new FtpFragmentList());
        fTrans.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ftp, menu);
        this.manu = menu;
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
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

    public void onEventMainThread(EventBusMessenger event) {
        Log.d(TAG, "onEvent: " + event.state);
        MenuItem menuItem = manu.findItem(R.id.action_refresh);
        switch (event.state) {
            case READ_LIST_START:
                if(isListReading) break;
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ImageView iv = (ImageView)inflater.inflate(R.layout.refresh, null);
                Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate_refresh);
                rotation.setRepeatCount(Animation.INFINITE);
                iv.startAnimation(rotation);
                menuItem.setActionView(iv);
                isListReading = true;
                break;
            case READ_LIST_OK:

                break;
            case READ_LIST_ERROR:
                Toast.makeText(FtpActivity.this, R.string.connection_error, Toast.LENGTH_SHORT).show();
                break;
            case READ_LIST_FINISH:
                if(null == menuItem.getActionView())break;
                menuItem.getActionView().clearAnimation();
                menuItem.setActionView(null);
                isListReading= false;
                break;
        }
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
        if(isListReading) return;
        mFtpConnectionService.back();
    }

    @Override
    public void onDirClick(FFile ftpFile) {
        if(isListReading) return;
        mFtpConnectionService.readList(ftpFile.getName());
    }

    @Override
    public void onFileClick(FFile ftpFile) {
        if(isListReading) return;
    }
}
