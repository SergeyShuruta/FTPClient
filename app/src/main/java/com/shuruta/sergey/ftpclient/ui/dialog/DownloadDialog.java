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
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuruta.sergey.ftpclient.Constants;
import com.shuruta.sergey.ftpclient.R;
import com.shuruta.sergey.ftpclient.cache.CacheManager;
import com.shuruta.sergey.ftpclient.entity.DFile;
import com.shuruta.sergey.ftpclient.entity.DownloadEntity;
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
        CommunicationEvent.FileDownloadEventListener,
        CommunicationEvent.DownloadEventListener {

    private final String TAG = DownloadDialog.class.getSimpleName();

    private ProgressBar progressBarTotal, progressBarFile;
    private TextView title,
            textViewProgressTotal,
            textViewProgressFile,
            textViewFile;
    private RelativeLayout containerFile;
    private boolean isDownloading = false;
    private OnDialogCloseListener dialogCloseListener;

    public static DownloadDialog show(FragmentActivity fragmentActivity) {
        DownloadDialog downloadDialog = new DownloadDialog();
        downloadDialog.setCancelable(false);
        downloadDialog.show(fragmentActivity.getSupportFragmentManager(), "DownloadDialog");
        return downloadDialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            dialogCloseListener = (OnDialogCloseListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnDialogCloseListener");
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setPositiveButton(R.string.hide, DownloadDialog.this);
        adb.setNegativeButton(R.string.cancel, DownloadDialog.this);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_download, null);
        title = (TextView) view.findViewById(R.id.title);
        containerFile = (RelativeLayout) view.findViewById(R.id.containerFile);
        progressBarTotal = (ProgressBar) view.findViewById(R.id.progressBarTotal);
        progressBarFile = (ProgressBar) view.findViewById(R.id.progressBarFile);
        textViewProgressTotal = (TextView) view.findViewById(R.id.totalProgressPercentTotal);
        textViewProgressFile = (TextView) view.findViewById(R.id.totalProgressPercentFile);
        textViewFile = (TextView) view.findViewById(R.id.textViewFile);
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
            case R.string.hide:
                break;
            case R.string.cancel:
                break;
        }
    }

    @Override
    public void onEventMainThread(CommunicationEvent event) {
        event.setListener((CommunicationEvent.FileDownloadEventListener)DownloadDialog.this);
        event.setListener((CommunicationEvent.DownloadEventListener)DownloadDialog.this);
    }

    @Override
    public void onDownloadStart() {

    }

    @Override
    public void onDownloadError(String message) {
        dismiss();
        dialogCloseListener.onCloseDialog(OnDialogCloseListener.State.ERROR);
    }

    @Override
    public void onDownloadFinish() {
        dismiss();
        dialogCloseListener.onCloseDialog(OnDialogCloseListener.State.FINISH);
    }

    @Override
    public void onFileDownloadStart(DFile dFile) {
        if(!isDownloading) {
            progressBarTotal.setIndeterminate(false);
            progressBarFile.setIndeterminate(false);
            textViewProgressTotal.setVisibility(View.VISIBLE);
            containerFile.setVisibility(View.VISIBLE);
            isDownloading = true;
        }
        DownloadEntity downloadEntity = CacheManager.getInstance().getCurrentDownload();
        title.setText(getString(R.string.downloading_file_x_from_y, downloadEntity.getCompletedSize(), downloadEntity.getSize()));
        textViewFile.setText(dFile.getTo().concat(dFile.getName()));
    }

    @Override
    public void onFileDownloadProgress(DFile dFile) {
        Log.d("TEST", "File: " + dFile.getProgress().concat("%"));
        progressBarFile.setProgress(dFile.getProgressInt());
        textViewProgressFile.setText(dFile.getProgress().concat("%"));
    }

    @Override
    public void onFileDownloadAborted(DFile dFile) {

    }

    @Override
    public void onFileDownloadError(DFile dFile, String message) {

    }

    @Override
    public void onFileDownloadFinish(DFile dFile) {
        progressBarTotal.setProgress(CacheManager.getInstance().getCurrentDownload().getTotalProgressInt());
        textViewProgressTotal.setText(CacheManager.getInstance().getCurrentDownload().getTotalProgress().concat("%"));
    }
}
