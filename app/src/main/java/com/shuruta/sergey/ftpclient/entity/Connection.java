package com.shuruta.sergey.ftpclient.entity;


import android.content.ContentValues;
import android.database.Cursor;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.shuruta.sergey.ftpclient.BR;
import com.shuruta.sergey.ftpclient.Constants;
import com.shuruta.sergey.ftpclient.CustomApplication;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Sergey on 24.07.2015.
 */
public class Connection extends BaseObservable implements Parcelable {

    public static final String TABLE = "connections";

    public static final String _ID   = "_id";
    public static final String NAME  = "name";
    public static final String HOST  = "host";
    public static final String PORT  = "port";
    public static final String DIR   = "dir";
    public static final String L_DIR = "local_dir";
    public static final String LOGIN = "login";
    public static final String PASSW = "password";
    public static final String NOOP  = "noop";
    public static final String DATE  = "date";

    public static String getCreateTableSQL() {

        return "CREATE TABLE " + TABLE + " ("
                    + Connection._ID      + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Connection.NAME     + " TEXT NOT NULL,"
                    + Connection.HOST     + " TEXT NOT NULL,"
                    + Connection.PORT     + " INTEGER NOT NULL,"
                    + Connection.DIR      + " TEXT NOT NULL DEFAULT '" + File.separator + "',"
                    + Connection.L_DIR    + " TEXT NOT NULL DEFAULT '" + File.separator + "',"
                    + Connection.LOGIN    + " TEXT,"
                    + Connection.PASSW    + " TEXT, "
                    + Connection.NOOP     + " INTEGER NOT NULL,"
                    + Connection.DATE     + " INTEGER NOT NULL"
                + ")";
    }

    public static final Parcelable.Creator<Connection> CREATOR = new Parcelable.Creator<Connection>() {

        public Connection createFromParcel(Parcel in) {
            return new Connection(in);
        }

        public Connection[] newArray(int size) {
            return new Connection[size];
        }
    };

    private long    id;
    private String  name;
    private String  host;
    private int     port;
    private String  dir;
    private String  ldir;
    private String  login;
    private String  passw;
    private int     noop;

    private boolean isChanged = false;
    private boolean isActive = false;


    public ObservableField<String> text = new ObservableField<>();

    public Connection(long id) {
        this(CustomApplication.getInstance().getDatabaseAdapter().getConnection(id));
    }

    public Connection(Cursor cursor) {
        if(null == cursor) {
            this.dir = File.separator;
            this.ldir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            this.noop = Constants.NOOP_TIMEOUT_DEFAULT;
            return;
        }
        this.id    = cursor.getLong(cursor.getColumnIndex(Connection._ID));
        this.name  = cursor.getString(cursor.getColumnIndex(Connection.NAME));
        this.host  = cursor.getString(cursor.getColumnIndex(Connection.HOST));
        this.port  = cursor.getInt(cursor.getColumnIndex(Connection.PORT));
        this.dir   = cursor.getString(cursor.getColumnIndex(Connection.DIR));
        this.ldir  = cursor.getString(cursor.getColumnIndex(Connection.L_DIR));
        this.login = cursor.getString(cursor.getColumnIndex(Connection.LOGIN));
        this.passw = cursor.getString(cursor.getColumnIndex(Connection.PASSW));
        this.noop  = cursor.getInt(cursor.getColumnIndex(Connection.NOOP));
    }

    private Connection(Parcel parcel) {
        id = parcel.readLong();
        name = parcel.readString();
        host = parcel.readString();
        port = parcel.readInt();
        dir = parcel.readString();
        ldir = parcel.readString();
        login = parcel.readString();
        passw = parcel.readString();
        noop = parcel.readInt();
        parcel.readBooleanArray(new boolean[]{isChanged, isActive});
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

    public int getPort() {
        return port;
    }

    public String getDir() {
        return dir;
    }

    public String getLocalDir() {
        return ldir;
    }

    public String getLogin() {
        return login;
    }

    public String getPassw() {
        return passw;
    }

    public int getNoop() {
        return noop;
    }

    @Bindable
    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public void setHost(String host) {
        this.host = host;
        notifyPropertyChanged(BR.host);
    }

    @Bindable
    public void setPort(String port) {
        setPort(port.length() > 0 ? Integer.parseInt(port) : 0);
   }

    @Bindable
    public void setPort(int port) {
        this.port = port;
        notifyPropertyChanged(BR.port);
    }

    @Bindable
    public void setDir(String dir) {
        this.dir = dir;
        notifyPropertyChanged(BR.dir);
    }

    @Bindable
    public void setLocalDir(String ldir) {
        this.ldir = ldir;
        notifyPropertyChanged(BR.localDir);
    }

    @Bindable
    public void setLogin(String login) {
        this.login = login;
        notifyPropertyChanged(BR.login);
    }

    @Bindable
    public void setPassw(String passw) {
        this.passw = passw;
        notifyPropertyChanged(BR.passw);
    }

    @Bindable
    public void setNoop(int noop) {
        this.noop = noop;
        notifyPropertyChanged(BR.noop);
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

    public boolean isChanged() {

        return isChanged;
    }

    public boolean isAnonymous() {

        return login.isEmpty() && passw.isEmpty();
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        if(id > 0) values.put(_ID,   id);
        values.put(NAME,  name);
        values.put(HOST,  host);
        values.put(PORT,  port);
        values.put(DIR,   prepareDir(dir));
        values.put(L_DIR, prepareDir(ldir));
        values.put(LOGIN, login);
        values.put(PASSW, passw);
        values.put(NOOP,  noop);
        values.put(DATE,  Calendar.getInstance().getTimeInMillis() / 1000);
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

    public void save() {
        CustomApplication.getInstance().getDatabaseAdapter().saveConnection(this);
    }

    private String prepareDir(String dir) {
        if(null == dir || dir.isEmpty()) dir = File.separator;
        if(!dir.substring(dir.length()-1).equals(File.separator)) dir = dir.concat(File.separator);
        if(!dir.substring(0, 1).equals(File.separator)) dir = File.separator.concat(dir);
        return dir;
    }

    public boolean isCanSave() {
        return this.name != null && this.name.length() > 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(host);
        dest.writeInt(port);
        dest.writeString(dir);
        dest.writeString(ldir);
        dest.writeString(login);
        dest.writeString(passw);
        dest.writeInt(noop);
        dest.writeBooleanArray(new boolean[]{isChanged, isActive});
    }

}
