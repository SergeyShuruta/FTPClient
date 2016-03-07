package com.shuruta.sergey.ftpclient.ui.dialog;

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
import android.widget.TextView;

import com.shuruta.sergey.ftpclient.Constants;
import com.shuruta.sergey.ftpclient.CustomApplication;
import com.shuruta.sergey.ftpclient.R;
import com.shuruta.sergey.ftpclient.cache.CacheManager;
import com.shuruta.sergey.ftpclient.entity.DFile;
import com.shuruta.sergey.ftpclient.entity.DownloadEntity;
import com.shuruta.sergey.ftpclient.event.CommunicationEvent;
import com.shuruta.sergey.ftpclient.services.FtpService;
import com.shuruta.sergey.ftpclient.utils.Utils;

import de.greenrobot.event.EventBus;

/**
 * Created by Sergey Shuruta
 * 31.12.2015 at 11:39
 */
public class DownloadPresentDialog extends DialogFragment implements
        DialogInterface.OnClickListener {

    private TextView message;
    private FtpService mFtpService;
    private boolean bound;

    public static DownloadPresentDialog show(FragmentActivity fragmentActivity) {
        DownloadPresentDialog downloadDialog = new DownloadPresentDialog();
        downloadDialog.setCancelable(false);
        downloadDialog.show(fragmentActivity.getSupportFragmentManager(), "DownloadPresentDialog");
        return downloadDialog;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setPositiveButton(R.string.override_file, DownloadPresentDialog.this);
        adb.setNeutralButton(R.string.skip_file, DownloadPresentDialog.this);
        adb.setNegativeButton(R.string.cancel, DownloadPresentDialog.this);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_download_present, null);
        message = (TextView) view.findViewById(R.id.message);
        message.setText(getString(R.string.file_x_is_present, CacheManager.getInstance().getDownloadEntity().getCurrentFile().getName()));
        adb.setView(view, Utils.dpToPx(24), 0, Utils.dpToPx(24), 0);
        return adb.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().bindService(new Intent(getActivity(), FtpService.class), mServiceConnection, 0);
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
    public void onStop() {
        super.onStop();
        if (!bound) return;
        getActivity().unbindService(mServiceConnection);
        bound = false;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                CacheManager.getInstance().getDownloadEntity().setOverride();
                mFtpService.download(null, null, null);
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                CacheManager.getInstance().getDownloadEntity().setSkip();
                mFtpService.download(null, null, null);
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                CacheManager.getInstance().clearDownload();
                break;
        }
        dismiss();
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder binder) {
            mFtpService = ((FtpService.ConnectionBinder) binder).getService();
            bound = true;
        }

        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };
}
