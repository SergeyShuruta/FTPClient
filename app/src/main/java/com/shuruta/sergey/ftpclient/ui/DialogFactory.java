package com.shuruta.sergey.ftpclient.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.shuruta.sergey.ftpclient.R;

/**
 * Created by Sergey on 12.08.2015.
 */
public class DialogFactory {

    public static AlertDialog showDialog(Context context, int message, DialogInterface.OnClickListener okListener) {
        return showDialog(context, null, context.getString(message), R.string.ok, R.string.cancel, okListener, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
    }

    public static AlertDialog showMessage(Context context, int title, int message, DialogInterface.OnClickListener okListener) {
        return showMessage(context, context.getString(title), context.getString(message), okListener);
    }

    public static AlertDialog showMessage(Context context, String title, String message, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setPositiveButton(R.string.ok, okListener == null ? new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                } : okListener);
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    public static AlertDialog showDialog(Context context, int title, int message, int positiveMsg, int negativeMsg, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        return showDialog(context, context.getString(title), context.getString(message), positiveMsg, negativeMsg, okListener, cancelListener);
    }

    public static AlertDialog showDialog(Context context, String title, String message, int positiveMsg, int negativeMsg, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setPositiveButton(positiveMsg, okListener)
                .setNegativeButton(negativeMsg, cancelListener);
        if(null != title && !title.isEmpty())
            builder.setTitle(title);
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }


/*    public static AlertDialog showProgressDialog(Context context, String message, DialogInterface.OnClickListener cancelListener) {
        return showProgressDialog(context, null, message, cancelListener);
    }

    public static AlertDialog showProgressDialog(Context context, String title, String message, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setNegativeButton(R.string.cancel, cancelListener);
        if(null != title)
            builder.setTitle(title);
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }*/

}
