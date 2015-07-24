package com.shuruta.sergey.ftpclient.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shuruta.sergey.ftpclient.database.entity.Connection;

/**
 * Created by Sergey on 24.07.2015.
 */
public class DatabaseAdapter extends SQLiteOpenHelper {

    private static final String TAG = DatabaseAdapter.class.getSimpleName();

    private static final String DATABASE_NAME = "connections.db";

    private static final int CUR_DATABASE_VERSION = 1;

    private final Context mContext;

    public DatabaseAdapter(Context context) {
        super(context, DATABASE_NAME, null, CUR_DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Connection.getCreateTableSQL());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
