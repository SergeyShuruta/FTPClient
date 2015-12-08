package com.shuruta.sergey.ftpclient.ui.activitys;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import com.shuruta.sergey.ftpclient.Constants;
import com.shuruta.sergey.ftpclient.EventBusMessenger;
import com.shuruta.sergey.ftpclient.R;
import com.shuruta.sergey.ftpclient.ui.fragments.FilesFragment;

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
                EventBusMessenger.sendMessage(Constants.TYPE_FTP, EventBusMessenger.Event.CLOSE);
            case R.id.action_refresh:
                // Do animation start
                //LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //ImageView iv = (ImageView)inflater.inflate(R.layout.refresh_action_view, null);
                //Animation rotation = AnimationUtils.loadAnimation(this, R.anim.refresh);
                //rotation.setRepeatCount(Animation.INFINITE);
                //iv.startAnimation(rotation);
                //item.getActionView().findViewById(R.id.action_refresh).startAnimation(rotation);
                //refreshItem = item;
                //refresh();
                EventBusMessenger.sendMessage(Constants.TYPE_FTP,EventBusMessenger.Event.REFRESH);
                EventBusMessenger.sendMessage(Constants.TYPE_LOCAL, EventBusMessenger.Event.REFRESH);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        EventBusMessenger.sendMessage(Constants.TYPE_FTP, EventBusMessenger.Event.BACK);
        EventBusMessenger.sendMessage(Constants.TYPE_LOCAL, EventBusMessenger.Event.BACK);
    }
}
