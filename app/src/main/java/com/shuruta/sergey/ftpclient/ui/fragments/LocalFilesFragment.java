package com.shuruta.sergey.ftpclient.ui.fragments;

import android.app.Activity;
import android.util.Log;

import com.shuruta.sergey.ftpclient.EventBusMessenger;
import com.shuruta.sergey.ftpclient.interfaces.FFile;

import java.util.ArrayList;

/**
 * Created by Sergey Shuruta
 * 08/15/15 at 22:11
 */
public class LocalFilesFragment extends FilesFragment {

    //private LocalFragmentListener mActivityListener;
    public static final String TAG = LocalFilesFragment.class.getSimpleName();

/*    public interface LocalFragmentListener {
        boolean isLocalListReading();
    }*/

/*    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mActivityListener = (LocalFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement LocalFragmentListener");
        }
    }*/

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onEventMainThread(EventBusMessenger event) {
        //if(null == mFileRecyclerView) return;
        Log.d(TAG, "onEvent: " + event.state);
        switch (event.state) {
            case READ_FTP_LIST_START:
                startReadList();
                break;
            case READ_FTP_LIST_OK:
                displayList(new ArrayList<FFile>());
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
        /*if(mActivityListener.isLocalListReading()) return;*/
    }

    @Override
    public void onDirClick(FFile ftpFile) {
        /*if(mActivityListener.isLocalListReading()) return;*/
    }

    @Override
    public void onFileClick(FFile ftpFile) {
        /*if(mActivityListener.isLocalListReading()) return;*/
    }

    @Override
    public void reload() {

    }
}
