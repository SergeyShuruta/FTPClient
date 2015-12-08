package com.shuruta.sergey.ftpclient.ui.activitys;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.shuruta.sergey.ftpclient.R;

/**
 * Created by Sergey Shuruta
 * 03.09.2015 at 17:04
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar mToolbar = null;

    public enum ToolBarButton {
        ACCEPT,
        BACK,
        CLOSE
    }

    public void setupToolBar(Integer titleRes, String subTitleRes, ToolBarButton toolBarButton) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if(null != titleRes) mToolbar.setTitle(titleRes);
        if(null != subTitleRes) mToolbar.setSubtitle(subTitleRes);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.toolbar_text_primary));
        mToolbar.setSubtitleTextColor(getResources().getColor(R.color.toolbar_text_secondary));
        setSupportActionBar(mToolbar);
        if(null != toolBarButton) {
            switch (toolBarButton) {
                case ACCEPT:
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_accept);
                    break;
                case BACK:
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_cancel);
                    break;
                case CLOSE:
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_remove);
                    break;
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }



}
