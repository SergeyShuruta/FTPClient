package com.shuruta.sergey.ftpclient.ui.activitys;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.shuruta.sergey.ftpclient.EventBusMessenger;
import com.shuruta.sergey.ftpclient.services.FtpService;
import com.shuruta.sergey.ftpclient.R;
import com.shuruta.sergey.ftpclient.ui.fragments.FtpFilesFragment;
import com.shuruta.sergey.ftpclient.ui.fragments.FFilesFragment;
import com.shuruta.sergey.ftpclient.ui.fragments.LocalFilesFragment;

import de.greenrobot.event.EventBus;

/**
 * Author: Sergey Shuruta
 * Date: 08/15/15
 * Time: 22:11
 */
public class FilesActivity extends BaseActivity implements FtpFilesFragment.FtpFragmentListener {

    private FtpService mFtpConnectionService;
    private Menu menu;
    private boolean isFtpListReading, bound;
    private ListType mSelectedList = ListType.FTP;

    private FFilesFragment mFtpFilesFragment, mLocalFilesFragment;

    public enum ListType {
        FTP,
        LOCAL
    }

    public static final String TAG = FilesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);
        setupToolBar(R.drawable.ic_launcher, R.string.app_name, getString(R.string.list_of_connections), false);

        mFtpFilesFragment = new FtpFilesFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.ftpFrameLayout, mFtpFilesFragment);

        if(null != getSupportFragmentManager().findFragmentById(R.id.localFrameLayout)) {
            mLocalFilesFragment = new LocalFilesFragment();
            fragmentTransaction.replace(R.id.localFrameLayout, mLocalFilesFragment);
            mSelectedList = ListType.LOCAL;
        }


        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_files, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                getCurrentFragment().reload();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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
        bindService(new Intent(FilesActivity.this, FtpService.class), mServiceConnection, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!bound) return;
        unbindService(mServiceConnection);
        bound = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // TODO onBack()
    }

    public void onEventMainThread(EventBusMessenger event) {
        Log.d(TAG, "onEvent: " + event.state);
        //MenuItem menuItem = menu.findItem(R.id.action_refresh);
        switch (event.state) {
            case READ_FTP_LIST_START:
                if(isFtpListReading) break;
                LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                ImageView iv = (ImageView)inflater.inflate(R.layout.refresh, null);
                Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate_refresh);
                rotation.setRepeatCount(Animation.INFINITE);
                iv.startAnimation(rotation);
                //menuItem.setActionView(iv);
                isFtpListReading = true;
                break;
            case READ_FTP_LIST_FINISH:
                /*if(null != menuItem.getActionView()) {
                    menuItem.getActionView().clearAnimation();
                    menuItem.setActionView(null);
                }*/
                isFtpListReading = false;
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
    public FtpService getFtpConnectionService() {
        return mFtpConnectionService;
    }

    @Override
    public boolean isFtpListReading() {
        return isFtpListReading;
    }

    private FFilesFragment getCurrentFragment() {
        if(mSelectedList.equals(ListType.FTP)) {
            return mFtpFilesFragment;
        } else {
            return mLocalFilesFragment;
        }
    }
}
