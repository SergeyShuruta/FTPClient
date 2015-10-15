package com.shuruta.sergey.ftpclient.ui.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.shuruta.sergey.ftpclient.CustomApplication;
import com.shuruta.sergey.ftpclient.EventBusMessenger;
import com.shuruta.sergey.ftpclient.adapters.LocalFileAdapter;
import com.shuruta.sergey.ftpclient.cache.CacheManager;
import com.shuruta.sergey.ftpclient.interfaces.FFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Sergey Shuruta
 * 08/15/15 at 22:11
 */
public class LocalFilesFragment extends FilesFragment {

    public static final String TAG = LocalFilesFragment.class.getSimpleName();
    private static final String START_DIR = Environment.getExternalStorageDirectory().toString();

    @Override
    public void onResume() {
        super.onResume();
        displayList(CacheManager.getInstance().getLocalFiles());
    }

    public void onEventMainThread(EventBusMessenger event) {
        Log.d(TAG, "onEvent: " + event.state);
        switch (event.state) {
            case REFRESH:
                if(!isSelected() || !isCanDo()) return;
                new ReadLocalFilesAsyncTask().execute(START_DIR);
                break;
            case SELECT_FTP:
                setSelected(false);
                break;
            case SELECT_LOCAL:
                setSelected(true);
                break;
            case BACK:
                if(isSelected() && isCanDo()) {
                    onBack();
                }
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

    private class ReadLocalFilesAsyncTask extends AsyncTask<String, Boolean, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startReadList();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = false;
            List<FFile> files = new ArrayList<>();
            try {
                String path = params[0];
                File f = new File(path);
                File fileArray[] = f.listFiles();
                for (int i=0; i < fileArray.length; i++) {
                    files.add(LocalFileAdapter.create(fileArray[i]));
                }
                Collections.sort(files, new Comparator<FFile>() {
                    @Override
                    public int compare(final FFile object1, final FFile object2) {
                        if (object1.isDir() && object2.isFile())
                            return -1;
                        if (object1.isFile() && object2.isDir())
                            return 1;
                        return object1.getName().compareTo(object2.getName());
                    }
                });
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            CacheManager.getInstance().putLocalFiles(files);
            return result;


        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(!result) {
                errorReadList();
            }
            finishReadList();
            displayList(CacheManager.getInstance().getLocalFiles());
        }
    }
}
