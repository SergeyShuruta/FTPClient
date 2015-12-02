package com.shuruta.sergey.ftpclient.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.shuruta.sergey.ftpclient.R;

/**
 * Created by Sergey on 12.08.2015.
 */
public class DialogFactory {

    public static void showDialog(Context context, int message, DialogInterface.OnClickListener okListener) {
        showDialog(context, null, context.getString(message), R.string.ok, R.string.cancel, okListener, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
    }

    public static void showDialog(Context context, int title, int message, DialogInterface.OnClickListener okListener) {
        showDialog(context, context.getString(title), context.getString(message), R.string.ok, R.string.cancel, okListener, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
    }

    public static void showDialog(Context context, int title, int message, int positiveMsg, int negativeMsg, DialogInterface.OnClickListener okListener) {
        showDialog(context, context.getString(title), context.getString(message), positiveMsg, negativeMsg, okListener, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
    }

    public static void showMessage(Context context, int title, int message) {
        showMessage(context, title, message, null);
    }

    public static void showMessage(Context context, int title, int message, DialogInterface.OnClickListener okListener) {
        showMessage(context, context.getString(title), context.getString(message), okListener);
    }

    public static void showMessage(Context context, String title, String message, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setPositiveButton(R.string.ok, okListener == null ? new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                } : okListener);
        builder.create().show();
    }

    public static void showDialog(Context context, int title, int message, int positiveMsg, int negativeMsg, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        showDialog(context, context.getString(title), context.getString(message), positiveMsg, negativeMsg, okListener, cancelListener);
    }

    public static void showDialog(Context context, String title, String message, int positiveMsg, int negativeMsg, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setPositiveButton(positiveMsg, okListener)
                .setNegativeButton(negativeMsg, cancelListener);
        if(null != title && !title.isEmpty())
            builder.setTitle(title);
        builder.create().show();
    }
}
