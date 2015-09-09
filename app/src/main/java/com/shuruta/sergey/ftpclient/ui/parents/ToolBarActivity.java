package com.shuruta.sergey.ftpclient.ui.parents;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.shuruta.sergey.ftpclient.R;

/**
 * Created by Sergey Shuruta
 * 03.09.2015 at 17:04
 */
public abstract class ToolBarActivity extends FragmentActivity {

    protected Toolbar mToolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Menu setupToolBar(Integer logoRes, Integer titleRes, Integer subTitleRes, Integer menuRes, Toolbar.OnMenuItemClickListener listener) {
        return setupToolBar(logoRes, titleRes, null != subTitleRes ? getString(subTitleRes) : null, menuRes, listener);
    }

    public Menu setupToolBar(Integer logoRes, Integer titleRes, String subTitleRes, Integer menuRes, Toolbar.OnMenuItemClickListener listener) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if(null != logoRes) mToolbar.setLogo(logoRes);
        if(null != titleRes) mToolbar.setTitle(titleRes);
        if(null != subTitleRes) mToolbar.setSubtitle(subTitleRes);
        if(null != menuRes) mToolbar.inflateMenu(menuRes);
        if(null != listener) mToolbar.setOnMenuItemClickListener(listener);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.actionbar_text_primary));
        mToolbar.setSubtitleTextColor(getResources().getColor(R.color.actionbar_text_secondary));
        return mToolbar.getMenu();
    }

}
