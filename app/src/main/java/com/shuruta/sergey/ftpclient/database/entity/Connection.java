package com.shuruta.sergey.ftpclient.database.entity;


import android.content.ContentValues;
import android.database.Cursor;

import com.shuruta.sergey.ftpclient.CustomApplication;
import com.shuruta.sergey.ftpclient.database.DatabaseAdapter;

import java.util.Calendar;

/**
 * Created by Sergey on 24.07.2015.
 */
public class Connection {

    public static final String TABLE = "connections";

    public static final String _ID  = "_id";
    public static final String NAME = "name";
    public static final String HOST = "host";
    public static final String PORT  = "port";
    public static final String LOGIN = "login";
    public static final String PASSW = "password";
    public static final String DATE  = "date";

    public static String getCreateTableSQL() {

        return "CREATE TABLE " + TABLE + " ("
                    + Connection._ID      + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Connection.NAME     + " TEXT NOT NULL,"
                    + Connection.HOST     + " TEXT NOT NULL,"
                    + Connection.PORT     + " INTEGER NOT NULL,"
                    + Connection.LOGIN    + " TEXT,"
                    + Connection.PASSW + " TEXT, "
                    + Connection.DATE + " INTEGER NOT NULL"
                + ")";
    }

    public enum State {
        PREPARE,
        CONNECTED,
        ERROR,
        CLOSED
    }

    private long   id = 0;
    private String name = new String();
    private String host = new String();
    private int    port = 21;
    private String login = new String();
    private String passw = new String();
    private long   date = Calendar.getInstance().getTimeInMillis();
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

    public void setName(String name) {
        this.name = name;
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
        values.put(LOGIN, login);
        values.put(PASSW, passw);
        values.put(DATE, Calendar.getInstance().getTimeInMillis() / 1000);
        return values;
    }
}
