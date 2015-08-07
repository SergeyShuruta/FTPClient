package com.shuruta.sergey.ftpclient;

import android.app.Application;
import android.content.Context;

import com.shuruta.sergey.ftpclient.database.DatabaseAdapter;
import com.shuruta.sergey.ftpclient.database.entity.Connection;

/**
 * Created by Sergey on 24.07.2015.
 */
public class CustomApplication extends Application {

    private Context mContext;
    private DatabaseAdapter databaseAdapter;

    private static CustomApplication instance;

    public static CustomApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = getApplicationContext();
        databaseAdapter = new DatabaseAdapter(mContext);
    }

    public DatabaseAdapter getDatabaseAdapter() {
        return databaseAdapter;
    }
}
