package com.shuruta.sergey.ftpclient.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.shuruta.sergey.ftpclient.database.entity.Connection;

import java.util.ArrayList;
import java.util.List;

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

    public List<Connection> getConnections() {
        List<Connection> connections = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(Connection.TABLE, null, null, null, null, null, Connection.DATE + " DESC");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(null != cursor) {
            if(cursor.moveToFirst()) {
                do {
                    connections.add(new Connection(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        Log.d(TAG, "getConnections(" + connections.size() + ")");
        return connections;
    }

    public void saveConnection(Connection connection) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = connection.getContentValues();
        if(connection.isNewRow()) {
            db.insert(Connection.TABLE, null, values);
        } else {
            db.update(Connection.TABLE, values, Connection._ID + "=?", new String[]{connection.getStringId()});
        }
        db.close();
    }

}
