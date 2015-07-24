package com.shuruta.sergey.ftpclient.database.entity;


/**
 * Created by Sergey on 24.07.2015.
 */
public class Connection {

    public static final String TABLE = "connections";

    public static final String _ID       = "_id";
    public static final String NAME     = "name";
    public static final String HOST     = "host";
    public static final String PORT     = "port";
    public static final String LOGIN    = "login";
    public static final String PASSWORD = "password";

    public static String getCreateTableSQL() {

        return "CREATE TABLE " + TABLE + " ("
                    + Connection._ID      + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Connection.NAME     + " TEXT NOT NULL,"
                    + Connection.HOST     + " TEXT NOT NULL,"
                    + Connection.PORT     + " INTEGER NOT NULL,"
                    + Connection.LOGIN    + " TEXT,"
                    + Connection.PASSWORD + " TEXT" +
                ")";
    }

}
