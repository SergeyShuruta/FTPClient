package com.shuruta.sergey.ftpclient.ui.activitys;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.shuruta.sergey.ftpclient.EventBusMessenger;
import com.shuruta.sergey.ftpclient.services.FtpService;
import com.shuruta.sergey.ftpclient.R;
import com.shuruta.sergey.ftpclient.ui.fragments.FtpFilesFragment;
import com.shuruta.sergey.ftpclient.ui.fragments.FilesFragment;
import com.shuruta.sergey.ftpclient.ui.fragments.LocalFilesFragment;

import de.greenrobot.event.EventBus;

/**
 * Author: Sergey Shuruta
 * Date: 08/15/15
 * Time: 22:11
 */
public class FilesActivity extends BaseActivity {

    private ListType mSelectedList = ListType.FTP;

    private FilesFragment mFtpFilesFragment, mLocalFilesFragment;

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

/*        findViewById(R.id.ftpFrameLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectList(ListType.FTP);
            }
        });


        findViewById(R.id.localFrameLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectList(ListType.LOCAL);
            }
        });*/

        if(null != findViewById(R.id.localFrameLayout)) {
            mLocalFilesFragment = new LocalFilesFragment();
            fragmentTransaction.replace(R.id.localFrameLayout, mLocalFilesFragment);
            mSelectedList = ListType.LOCAL;
        }


        fragmentTransaction.commit();
    }

    private void selectList(ListType listType) {
        mSelectedList = listType;
        mFtpFilesFragment.setEnabled(mSelectedList.equals(ListType.FTP));
        mLocalFilesFragment.setEnabled(mSelectedList.equals(ListType.FTP));
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
    public void onBackPressed() {
        super.onBackPressed();
        // TODO onBack()
    }

    public void onEventMainThread(EventBusMessenger event) {
        Log.d(TAG, "onEvent: " + event.state);
        //MenuItem menuItem = menu.findItem(R.id.action_refresh);
        switch (event.state) {
            case READ_FTP_LIST_START:
                //if(isFtpListReading) break;
/*                LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                ImageView iv = (ImageView)inflater.inflate(R.layout.refresh, null);
                Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate_refresh);
                rotation.setRepeatCount(Animation.INFINITE);
                iv.startAnimation(rotation);*/
                break;
            case READ_FTP_LIST_FINISH:

                break;
        }
    }

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
    };*/

/*    @Override
    public FtpService getFtpConnectionService() {
        return mFtpConnectionService;
    }*/

/*
    @Override
    public boolean isFtpListReading() {
        return isFtpListReading;
    }
*/

    private FilesFragment getCurrentFragment() {
        if(mSelectedList.equals(ListType.FTP)) {
            return mFtpFilesFragment;
        } else {
            return mLocalFilesFragment;
        }
    }
}
