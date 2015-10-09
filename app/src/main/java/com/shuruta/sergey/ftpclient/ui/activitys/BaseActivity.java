package com.shuruta.sergey.ftpclient.ui.activitys;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.support.v7.app.AppCompatActivity;

import com.shuruta.sergey.ftpclient.R;

/**
 * Created by Sergey Shuruta
 * 03.09.2015 at 17:04
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar mToolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setupToolBar(Integer logoRes, Integer titleRes, String subTitleRes, boolean isBacked) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if(null != logoRes) mToolbar.setLogo(logoRes);
        if(null != titleRes) mToolbar.setTitle(titleRes);
        if(null != subTitleRes) mToolbar.setSubtitle(subTitleRes);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.actionbar_text_primary));
        mToolbar.setSubtitleTextColor(getResources().getColor(R.color.actionbar_text_secondary));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(isBacked);
        if (isBacked) {
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
    }

}
