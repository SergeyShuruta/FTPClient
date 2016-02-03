package com.shuruta.sergey.ftpclient.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shuruta.sergey.ftpclient.CustomApplication;
import com.shuruta.sergey.ftpclient.Constants;
import com.shuruta.sergey.ftpclient.R;
import com.shuruta.sergey.ftpclient.entity.DFile;
import com.shuruta.sergey.ftpclient.event.CommunicationEvent;
import com.shuruta.sergey.ftpclient.interfaces.OnDialogCloseListener;
import com.shuruta.sergey.ftpclient.services.FtpService;
import com.shuruta.sergey.ftpclient.utils.Utils;

import de.greenrobot.event.EventBus;

/**
 * Created by Sergey Shuruta
 * 31.12.2015 at 11:39
 */
public class DownloadDialog extends DialogFragment implements
        DialogInterface.OnClickListener,
        CommunicationEvent.FileDownloadEventListener {

    private ProgressBar progressBarFile;
    private TextView title, textViewProgressFile;

    public static DownloadDialog show(FragmentActivity fragmentActivity) {
        DownloadDialog downloadDialog = new DownloadDialog();
        downloadDialog.setCancelable(false);
        downloadDialog.show(fragmentActivity.getSupportFragmentManager(), "DownloadDialog");
        return downloadDialog;
    }
/*
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            dialogCloseListener = (OnDialogCloseListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnDialogCloseListener");
        }
    }*/

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setNegativeButton(R.string.cancel, DownloadDialog.this);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_download, null);
        title = (TextView) view.findViewById(R.id.title);
        progressBarFile = (ProgressBar) view.findViewById(R.id.progressBarFile);
        textViewProgressFile = (TextView) view.findViewById(R.id.totalProgressPercentFile);
        adb.setView(view, Utils.dpToPx(24), 0, Utils.dpToPx(24), 0);
        return adb.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_NEGATIVE:
                CustomApplication.getInstance().stopDownload();
                break;
        }
    }

    @Override
    public void onEventMainThread(CommunicationEvent event) {
        event.setListener(DownloadDialog.this);
    }

    @Override
    public void onFileDownloadStart(DFile dFile) {
        progressBarFile.setIndeterminate(false);
        title.setText(dFile.getName());
    }

    @Override
    public void onFileDownloadProgress(DFile dFile) {
        progressBarFile.setProgress(dFile.getProgressInt());
        textViewProgressFile.setText(dFile.getProgress().concat("%"));
    }

    @Override
    public void onFileDownloadPresent(DFile dFile) {
        // Ask for overwrite
    }

    @Override
    public void onFileDownloadError(DFile dFile, String message) {
        // Ask for retry
    }

    @Override
    public void onFileDownloadFinish(DFile dFile) {

    }
}
