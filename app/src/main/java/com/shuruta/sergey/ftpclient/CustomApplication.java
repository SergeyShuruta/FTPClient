package com.shuruta.sergey.ftpclient;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.shuruta.sergey.ftpclient.database.DatabaseAdapter;
import com.shuruta.sergey.ftpclient.entity.Connection;
import com.shuruta.sergey.ftpclient.services.FtpService;

import java.io.File;

/**
 * Created by Sergey on 24.07.2015.
 */
public class CustomApplication extends Application {

    private Context mContext;
    private DatabaseAdapter mDatabaseAdapter;
    private Connection mCurrentConnection;
    private SharedPreferences mPreferences;

    private static CustomApplication instance;

    public static final String TAG = CustomApplication.class.getSimpleName();
    public static final String APP_PREFERENCES = "com.shuruta.sergey.ftpclient.APP_PREFERENCES";

    public static CustomApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = getApplicationContext();
        mDatabaseAdapter = new DatabaseAdapter(mContext);
        mPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        startService(new Intent(this, FtpService.class));

    }

    public DatabaseAdapter getDatabaseAdapter() {
        return mDatabaseAdapter;
    }

    public void setCurrentConnection(Connection currentConnection) {
        this.mCurrentConnection = currentConnection;
        setPath(Constants.TYPE_FTP);
        setPath(Constants.TYPE_LOCAL);
    }

    public Connection getCurrentConnection() {
        return mCurrentConnection;
    }

    public void setPath(int type) {
        if(null == mCurrentConnection) return;
        setPath(type, type == Constants.TYPE_FTP ? mCurrentConnection.getDir() : mCurrentConnection.getLocalDir());
    }

    public void setPath(int type, String path) {
        mPreferences.edit().putString(type + "--" + mCurrentConnection.getName(), path).commit();
    }

    public String getPath(int type) {
        if(null == mCurrentConnection) return File.separator;
        String defValue = type == Constants.TYPE_FTP ? mCurrentConnection.getDir() : mCurrentConnection.getLocalDir();
        return mPreferences.getString(type + "--" + mCurrentConnection.getName(), defValue);
    }
}
