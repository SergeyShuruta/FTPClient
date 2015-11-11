package com.shuruta.sergey.ftpclient.ui.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.shuruta.sergey.ftpclient.Constants;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TEST", "Start read local list!");
        readList(CustomApplication.getInstance().getPath(Constants.TYPE_LOCAL));
        initList(Constants.TYPE_LOCAL, CustomApplication.getInstance().getCurrentConnection().getLocalDir());
    }

    @Override
    public List<FFile> getFiles() {

        return CacheManager.getInstance().getLocalFiles();
    }

    @Override
    public void readList(String patch) {

        new ReadLocalFilesAsyncTask().execute(patch);
    }

    @Override
    public void onFileClick(FFile ftpFile) {

    }

    private class ReadLocalFilesAsyncTask extends AsyncTask<String, Boolean, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                EventBusMessenger.sendLocalMessage(EventBusMessenger.Event.ERROR);
            }
            CacheManager.getInstance().putLocalFiles(files);
            EventBusMessenger.sendLocalMessage(EventBusMessenger.Event.OK);
            EventBusMessenger.sendLocalMessage(EventBusMessenger.Event.FINISH);
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(!result) {

            }
        }
    }
}
