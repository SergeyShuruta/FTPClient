package com.shuruta.sergey.ftpclient.ui;

import android.app.Activity;
import android.util.Log;

import com.shuruta.sergey.ftpclient.CacheManager;
import com.shuruta.sergey.ftpclient.EventBusMessenger;
import com.shuruta.sergey.ftpclient.FFile;
import com.shuruta.sergey.ftpclient.FtpService;
import com.shuruta.sergey.ftpclient.ui.parents.FilesFragment;

/**
 * Created by Sergey Shuruta
 * 08/15/15 at 22:11
 */
public class FFilesFragment extends FilesFragment {

    private FtpFragmentListener mActivityListener;
    public static final String TAG = FFilesFragment.class.getSimpleName();

    public interface FtpFragmentListener {
        FtpService getFtpConnectionService();
        boolean isFtpListReading();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mActivityListener = (FtpFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FilesFragmentListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        displayList(CacheManager.getInstance().getFtpFiles());
    }

    public void onEventMainThread(EventBusMessenger event) {
        //if(null == mFileRecyclerView) return;
        Log.d(TAG, "onEvent: " + event.state);
        switch (event.state) {
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
        if(mActivityListener.isFtpListReading()) return;
        mActivityListener.getFtpConnectionService().back();
    }

    @Override
    public void onDirClick(FFile ftpFile) {
        if(mActivityListener.isFtpListReading()) return;
        mActivityListener.getFtpConnectionService().readList(ftpFile.getName());
    }

    @Override
    public void onFileClick(FFile ftpFile) {
        if(mActivityListener.isFtpListReading()) return;
    }
}
