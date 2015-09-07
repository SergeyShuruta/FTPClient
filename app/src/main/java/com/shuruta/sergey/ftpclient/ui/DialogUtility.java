package com.shuruta.sergey.ftpclient.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.shuruta.sergey.ftpclient.R;

/**
 * Created by Sergey on 12.08.2015.
 */
public class DialogUtility {


    public static void showDialog(Context context, int message, DialogInterface.OnClickListener okListener) {
        showDialog(context, message, R.string.ok, R.string.cancel, okListener, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
    }

    public static void showDialog(Context context, int message, int positiveMsg, int negativeMsg, DialogInterface.OnClickListener okListener) {
        showDialog(context, message, positiveMsg, negativeMsg, okListener, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
    }
    public static void showDialog(Context context, int message, int positiveMsg, int negativeMsg, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setPositiveButton(positiveMsg, okListener)
                .setNegativeButton(negativeMsg, cancelListener);
        builder.create().show();
    }
}
