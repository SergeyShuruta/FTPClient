package com.shuruta.sergey.ftpclient.entity;


import android.content.ContentValues;
import android.database.Cursor;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.shuruta.sergey.ftpclient.BR;
import com.shuruta.sergey.ftpclient.CustomApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Sergey on 24.07.2015.
 */
public class Connection extends BaseObservable {

    public static final String TABLE = "connections";

    public static final String _ID   = "_id";
    public static final String NAME  = "name";
    public static final String HOST  = "host";
    public static final String PORT  = "port";
    public static final String DIR   = "dir";
    public static final String LOGIN = "login";
    public static final String PASSW = "password";
    public static final String DATE  = "date";

    public static String getCreateTableSQL() {

        return "CREATE TABLE " + TABLE + " ("
                    + Connection._ID      + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Connection.NAME     + " TEXT NOT NULL,"
                    + Connection.HOST     + " TEXT NOT NULL,"
                    + Connection.PORT     + " INTEGER NOT NULL,"
                    + Connection.DIR      + " TEXT NOT NULL DEFAULT '" + File.separator + "',"
                    + Connection.LOGIN    + " TEXT,"
                    + Connection.PASSW + " TEXT, "
                    + Connection.DATE + " INTEGER NOT NULL"
                + ")";
    }

    public boolean isAnonymous() {
        return login.isEmpty() && passw.isEmpty();
    }

    private long    id;
    private String  name;
    private String  host;
    private int     port;
    private String  dir;
    private String  login;
    private String  passw;
    private long    date;

    private boolean isActive = false;

    public Connection(long id) {
        this(CustomApplication.getInstance().getDatabaseAdapter().getConnection(id));
    }

    public Connection(Cursor cursor) {
        if(null == cursor) return;
        setId(cursor.getLong(cursor.getColumnIndex(Connection._ID)));
        setName(cursor.getString(cursor.getColumnIndex(Connection.NAME)));
        setHost(cursor.getString(cursor.getColumnIndex(Connection.HOST)));
        setPort(cursor.getInt(cursor.getColumnIndex(Connection.PORT)));
        setDir(cursor.getString(cursor.getColumnIndex(Connection.DIR)));
        setLogin(cursor.getString(cursor.getColumnIndex(Connection.LOGIN)));
        setPassw(cursor.getString(cursor.getColumnIndex(Connection.PASSW)));
    }

    public String getStringId() {
        return String.valueOf(id);
    }

    private void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Bindable
    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public String getName() {
        return name;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void backDir() {
        if(this.dir.isEmpty() || this.dir.equals(File.separator)) return;
        String[] dirs = this.dir.split(File.separator);
        this.dir = File.separator;
        for(String d : dirs) {
            if(d.isEmpty()) continue;
            if(dirs[dirs.length - 1].equals(d)) continue;
            this.dir += d + File.separator;
        }
    }

    public void addDir(String dir) {
        if(dir.isEmpty()) return;
        Log.d("TEST", "addDir(" + dir + ")");
        String[] dirs = this.dir.split(File.separator);
        if(0 != dirs.length && dirs[dirs.length - 1].equals(dir)) return;
        this.dir += dir + File.separator;
        Log.d("TEST", "this.dir=" + this.dir);
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setPassw(String passw) {
        this.passw = passw;
    }

    public String getPassw() {
        return passw;
    }

    private void setDate(long date) {
        this.date = date;
    }

    public long getDate() {
        return date;
    }

    public boolean isNewRow() {
        return 0 == id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        if(id > 0) values.put(_ID,   id);
        values.put(NAME,  name);
        values.put(HOST,  host);
        values.put(PORT,  port);
        values.put(DIR,  dir);
        values.put(LOGIN, login);
        values.put(PASSW, passw);
        values.put(DATE, Calendar.getInstance().getTimeInMillis() / 1000);
        return values;
    }

    public static List<Connection> createList(Cursor cursor) {
        List<Connection> connections = new ArrayList<>();
        if(null != cursor) {
            if(cursor.moveToFirst()) {
                do {
                    connections.add(new Connection(cursor));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return connections;
    }
}
