package com.shuruta.sergey.ftpclient.database.entity;


import android.content.ContentValues;
import android.database.Cursor;

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

    private long   id;
    private String name;
    private String host;
    private String port;
    private String login;
    private String passw;
    private long   date;

    public Connection(Cursor cursor) {

    }

    public Connection(String name, String host, String port) {
        this.id = 0;
        this.name = name;
        this.host = host;
        this.port = port;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassw(String passw) {
        this.passw = passw;
    }

    public String getStringId() {
        return String.valueOf(id);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getLogin() {
        return login;
    }

    public String getPassw() {
        return passw;
    }

    public long getDate() {
        return date;
    }

    public boolean isNewRow() {
        return 0 == id;
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(_ID,   id);
        values.put(NAME,  name);
        values.put(HOST,  host);
        values.put(PORT,  port);
        values.put(LOGIN, login);
        values.put(PASSW, passw);
        values.put(DATE, Calendar.getInstance().getTimeInMillis() / 1000);
        return values;
    }
}
