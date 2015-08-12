package com.shuruta.sergey.ftpclient;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.shuruta.sergey.ftpclient.database.DatabaseAdapter;

/**
 * Created by Sergey on 24.07.2015.
 */
public class CustomApplication extends Application {

    private Context mContext;
    private DatabaseAdapter mDatabaseAdapter;

    private static CustomApplication instance;
    public static final String TAG = CustomApplication.class.getSimpleName();

    public static CustomApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = getApplicationContext();
        mDatabaseAdapter = new DatabaseAdapter(mContext);
        startService(new Intent(this, FtpConnectionService.class));
    }

    public DatabaseAdapter getDatabaseAdapter() {
        return mDatabaseAdapter;
    }
}
