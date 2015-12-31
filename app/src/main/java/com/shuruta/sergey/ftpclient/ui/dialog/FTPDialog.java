package com.shuruta.sergey.ftpclient.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.shuruta.sergey.ftpclient.CustomApplication;
import com.shuruta.sergey.ftpclient.R;
import com.shuruta.sergey.ftpclient.utils.Utils;

/**
 * Created by Sergey Shuruta
 * 30.12.2015 at 10:15
 */
public class FTPDialog extends DialogFragment implements DialogInterface.OnClickListener {

    public static final String TITLE = "title";
    public static final String MESSAGE = "message";
    public static final String IS_PROGRESS = "is_progress";

    public static void showNoInternetMessage(FragmentActivity fragmentActivity, DialogInterface.OnClickListener positiveClickListener) {
        FTPDialog dialogFragment = new FTPDialog();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, fragmentActivity.getString(R.string.internet_connection_error_title));
        bundle.putString(MESSAGE, fragmentActivity.getString(R.string.connection_will_be_closed));
        dialogFragment.setArguments(bundle);
        dialogFragment.setPositiveClickListener(R.string.ok, positiveClickListener);
        dialogFragment.show(fragmentActivity.getSupportFragmentManager(), "FTPDialog");
    }

    public static FTPDialog showReconnectDialog(FragmentActivity fragmentActivity,
                                                DialogInterface.OnClickListener positiveClickListener,
                                                DialogInterface.OnClickListener negativeClickListener) {
        FTPDialog dialogFragment = new FTPDialog();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, fragmentActivity.getString(R.string.connection_lost_title));
        bundle.putString(MESSAGE, fragmentActivity.getString(R.string.connection_lost_restore_ask));
        dialogFragment.setArguments(bundle);
        dialogFragment.setPositiveClickListener(R.string.yes, positiveClickListener);
        dialogFragment.setNegativeClickListener(R.string.no, negativeClickListener);
        dialogFragment.show(fragmentActivity.getSupportFragmentManager(), "FTPDialog");
        return dialogFragment;
    }

    public static FTPDialog showProgressDialog(FragmentActivity fragmentActivity, int messageRes) {
        return showProgressDialog(fragmentActivity,
                CustomApplication.getInstance().getString(messageRes));
    }

    public static FTPDialog showProgressDialog(FragmentActivity fragmentActivity, String message) {
        FTPDialog dialogFragment = new FTPDialog();
        Bundle bundle = new Bundle();
        bundle.putString(MESSAGE, message);
        bundle.putBoolean(IS_PROGRESS, true);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(fragmentActivity.getSupportFragmentManager(), "FTPDialog");
        return dialogFragment;
    }

    private ProgressBar progressBar;

    private String title = null,  message = null;
    private int positiveButton = 0, negativeButton = 0;
    private DialogInterface.OnClickListener positiveClickListener = null;
    private DialogInterface.OnClickListener negativeClickListener = null;
    private boolean isProgress;

    public void setPositiveClickListener(int caption,
                                         DialogInterface.OnClickListener positiveClickListener) {
        this.positiveButton = caption;
        this.positiveClickListener = positiveClickListener;
    }

    public void setNegativeClickListener(int caption,
                                         DialogInterface.OnClickListener negativeClickListener) {
        this.negativeButton = caption;
        this.negativeClickListener = negativeClickListener;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        this.title = bundle.getString(TITLE);
        this.message = bundle.getString(MESSAGE);
        this.isProgress = bundle.getBoolean(IS_PROGRESS);
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        if(null != title)
            adb.setTitle(title);
        if(null != message)
            adb.setMessage(message);
        if(null != positiveClickListener)
            adb.setPositiveButton(positiveButton, positiveClickListener);
        if(null != negativeClickListener)
            adb.setNegativeButton(negativeButton, negativeClickListener);
        if(isProgress) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_progress, null);
            adb.setView(view, Utils.dpToPx(24), 0, Utils.dpToPx(24), 0);
            progressBar = (ProgressBar) view;
        }
        return adb.create();
    }

    public void setProgress(int progress) {
        if(progressBar.isIndeterminate())
            progressBar.setIndeterminate(false);
        progressBar.setProgress(progress);
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
