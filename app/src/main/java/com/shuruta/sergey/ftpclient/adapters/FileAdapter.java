package com.shuruta.sergey.ftpclient.adapters;


import android.graphics.drawable.Drawable;

import com.shuruta.sergey.ftpclient.CustomApplication;
import com.shuruta.sergey.ftpclient.R;
import com.shuruta.sergey.ftpclient.interfaces.FFile;

/**
 * Created by Sergey Shuruta
 * 02.10.2015 at 17:00
 */
abstract class FileAdapter implements FFile {

    @Override
    public Drawable getIcon() {
        int res;
        switch (getName().substring(getName().lastIndexOf(".") + 1).toLowerCase()) {
            case "php": res = R.drawable.ic_action_php; break;
            case "txt": res = R.drawable.ic_action_txt; break;
            case "png":
            case "jpg":
            case "jpeg":
            case "gif":
                res = R.drawable.ic_action_picture; break;
            default:
                res = R.drawable.ic_action_unknown; break;
        }
        return CustomApplication.getInstance().getApplicationContext().getResources().getDrawable(res);
    }
}
