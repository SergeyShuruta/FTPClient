package com.shuruta.sergey.ftpclient.ui.activitys;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.shuruta.sergey.ftpclient.Constants;
import com.shuruta.sergey.ftpclient.CustomApplication;
import com.shuruta.sergey.ftpclient.entity.Connection;
import com.shuruta.sergey.ftpclient.event.CommunicationEvent;
import com.shuruta.sergey.ftpclient.R;
import com.shuruta.sergey.ftpclient.ui.fragments.FilesFragment;

import de.greenrobot.event.EventBus;

/**
 * Author: Sergey Shuruta
 * Date: 08/15/15
 * Time: 22:11
 */
public class FilesActivity extends BaseActivity implements CommunicationEvent.DisconnectionEventListener, CommunicationEvent.ConnectionEventListener {

    private FilesFragment mFtpFilesFragment, mLocalFilesFragment;

    public static final String TAG = FilesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);
        setupToolBar(R.string.app_name, CustomApplication.getInstance().getCurrentConnection().getName(), ToolBarButton.CLOSE);
        Bundle argsFtp = new Bundle();
        argsFtp.putInt(FilesFragment.LIST_TYPE, Constants.TYPE_FTP);
        argsFtp.putBoolean(FilesFragment.IS_SELECTED, true);
        mFtpFilesFragment = new FilesFragment();
        mFtpFilesFragment.setArguments(argsFtp);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.ftpFrameLayout, mFtpFilesFragment);
        if(null != findViewById(R.id.localFrameLayout)) {
            Bundle argsLocal = new Bundle();
            argsLocal.putInt(FilesFragment.LIST_TYPE, Constants.TYPE_LOCAL);
            mLocalFilesFragment = new FilesFragment();
            mLocalFilesFragment.setArguments(argsLocal);
            fragmentTransaction.replace(R.id.localFrameLayout, mLocalFilesFragment);
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_file_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(null != mFtpFilesFragment)
                    mFtpFilesFragment.onDisconnect();
            case R.id.action_refresh:
                if(null != mFtpFilesFragment)
                    mFtpFilesFragment.onListRefresh();
                if(null != mLocalFilesFragment)
                    mLocalFilesFragment.onListRefresh();
        }
        return super.onOptionsItemSelected(item);
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
        if(null != mFtpFilesFragment)
            mFtpFilesFragment.onListBack();
        if(null != mLocalFilesFragment)
            mLocalFilesFragment.onListBack();
    }

    @Override
    public void onEventMainThread(CommunicationEvent event) {
        event.setListener((CommunicationEvent.DisconnectionEventListener)FilesActivity.this);
        event.setListener((CommunicationEvent.ConnectionEventListener)FilesActivity.this);
    }

    @Override
    public void onConnectionStart(Connection connection) {

    }

    @Override
    public void onConnectionError(Connection connection, String message) {
        Toast.makeText(FilesActivity.this, R.string.connection_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFinish(Connection connection) {
        if(null != mFtpFilesFragment)
            mFtpFilesFragment.readList();
        if(null != mLocalFilesFragment)
            mLocalFilesFragment.readList();
    }

    @Override
    public void onDisconnectionStart() {

    }

    @Override
    public void onDisconnectionError(String message) {
        Toast.makeText(FilesActivity.this, getString(R.string.error_disconnect), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisconnectionFinish() {
        finish();
    }
}
