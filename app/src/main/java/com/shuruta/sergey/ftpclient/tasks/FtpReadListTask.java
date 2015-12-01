package com.shuruta.sergey.ftpclient.tasks;

import android.content.Context;
import android.os.Bundle;

import com.shuruta.sergey.ftpclient.Constants;
import com.shuruta.sergey.ftpclient.adapters.FTPFileAdapter;
import com.shuruta.sergey.ftpclient.cache.CacheManager;
import com.shuruta.sergey.ftpclient.EventBusMessenger;
import com.shuruta.sergey.ftpclient.services.FtpService;
import com.shuruta.sergey.ftpclient.interfaces.FFile;
import com.shuruta.sergey.ftpclient.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPListParseException;

/**
 * Author: Sergey Shuruta
 * Date: 08/15/15
 * Time: 22:11
 */
public class FtpReadListTask extends Task {

    private final FTPClient ftpClient;
    private final String path;

    public static final String TAG = FtpService.TAG + "." + FtpReadListTask.class.getSimpleName();

    public FtpReadListTask(Context context, FTPClient ftpClient, String path) {
        super(context);
        this.ftpClient = ftpClient;
        this.path = path;
    }

    @Override
    public void run() {
        EventBusMessenger.sendFtpMessage(EventBusMessenger.Event.START);
        Bundle bundle = new Bundle();
        try {
            FTPFile[] filesArray = ftpClient.list(path);
            List<FFile> files = new ArrayList<>();
            for(int i = 0; i < filesArray.length; i++) {
                if(filesArray[i].getName().equals("..")) continue;
                files.add(FTPFileAdapter.create(filesArray[i]));
            }
            Utils.sortFiles(files);
            CacheManager.getInstance().putFiles(files, Constants.TYPE_FTP);
            EventBusMessenger.sendFtpMessage(EventBusMessenger.Event.OK);
        } catch (IOException e) {
            bundle.putString(EventBusMessenger.MSG, e.getMessage());
            e.printStackTrace();
        } catch (FTPIllegalReplyException e) {
            bundle.putString(EventBusMessenger.MSG, e.getMessage());
            e.printStackTrace();
        } catch (FTPException e) {
            bundle.putString(EventBusMessenger.MSG, e.getMessage());
            e.printStackTrace();
        } catch (FTPDataTransferException e) {
            bundle.putString(EventBusMessenger.MSG, e.getMessage());
            e.printStackTrace();
        } catch (FTPAbortedException e) {
            bundle.putString(EventBusMessenger.MSG, e.getMessage());
            e.printStackTrace();
        } catch (FTPListParseException e) {
            bundle.putString(EventBusMessenger.MSG, e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            bundle.putString(EventBusMessenger.MSG, e.getMessage());
            e.printStackTrace();
        }
        if(bundle.containsKey(EventBusMessenger.MSG))
            EventBusMessenger.sendFtpMessage(EventBusMessenger.Event.ERROR, bundle);
        EventBusMessenger.sendFtpMessage(EventBusMessenger.Event.FINISH);
    }
}
