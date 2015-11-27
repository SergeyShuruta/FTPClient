package com.shuruta.sergey.ftpclient.ui.activitys;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.shuruta.sergey.ftpclient.Constants;
import com.shuruta.sergey.ftpclient.EventBusMessenger;
import com.shuruta.sergey.ftpclient.R;
import com.shuruta.sergey.ftpclient.ui.fragments.FtpFragment;
import com.shuruta.sergey.ftpclient.ui.fragments.FilesFragment;
import com.shuruta.sergey.ftpclient.ui.fragments.LocalFragment;

/**
 * Author: Sergey Shuruta
 * Date: 08/15/15
 * Time: 22:11
 */
public class FilesActivity extends BaseActivity {

    private FilesFragment mFtpFilesFragment, mLocalFilesFragment;

    public static final String TAG = FilesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);
        setupToolBar(R.string.app_name, getString(R.string.list_of_connections), ToolBarButton.CLOSE);

        mFtpFilesFragment = new FtpFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.ftpFrameLayout, mFtpFilesFragment);
        if(null != findViewById(R.id.localFrameLayout)) {
            mLocalFilesFragment = new LocalFragment();
            fragmentTransaction.replace(R.id.localFrameLayout, mLocalFilesFragment);
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
            case android.R.id.home:
                EventBusMessenger.sendMessage(Constants.TYPE_FTP, EventBusMessenger.Event.CLOSE);
                return true;
            case R.id.action_refresh:
                EventBusMessenger.sendMessage(Constants.TYPE_FTP,EventBusMessenger.Event.REFRESH);
                EventBusMessenger.sendMessage(Constants.TYPE_LOCAL, EventBusMessenger.Event.REFRESH);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        EventBus.getDefault().unregister(this);
//    }

    @Override
    public void onBackPressed() {
        EventBusMessenger.sendMessage(Constants.TYPE_FTP, EventBusMessenger.Event.BACK);
        EventBusMessenger.sendMessage(Constants.TYPE_LOCAL, EventBusMessenger.Event.BACK);
    }
}
