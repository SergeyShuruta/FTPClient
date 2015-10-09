package com.shuruta.sergey.ftpclient.ui.activitys;

import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.shuruta.sergey.ftpclient.CustomApplication;
import com.shuruta.sergey.ftpclient.R;
import com.shuruta.sergey.ftpclient.databinding.ActivityAddconBinding;
import com.shuruta.sergey.ftpclient.entity.Connection;
import com.shuruta.sergey.ftpclient.ui.DialogFactory;

import java.io.File;

/**
 * Created by Sergey on 24.07.2015.
 */
public class AddConActivity extends BaseActivity implements View.OnClickListener {

    private Connection connection;
    private ActivityAddconBinding binding;
    public static final String CONNECTION_ID = "connection_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_addcon);
        connection = new Connection(getIntent().getLongExtra(CONNECTION_ID, 0));
        binding.setConnection(connection);

        setupToolBar(null,
                R.string.app_name,
                getString(connection.getId() == 0 ? R.string.new_connection : R.string.edit_connection_x, connection.getName()),
                null, true, null);
    }

    @Override
    public void onClick(View v) {
        saveConnection();
    }

    private void saveConnection() {

        String strPort = binding.portEditText.getText().toString();
        String dir = binding.dirEditText.getText().toString();
        dir = dir.isEmpty() ? File.separator : dir;
        connection.setName(binding.nameEditText.getText().toString());
        connection.setHost(binding.hostEditText.getText().toString());
        connection.setPort(strPort.length() > 0 ? Integer.parseInt(strPort) : 21);
        connection.setDir(dir);
        connection.setLogin(binding.loginEditText.getText().toString());
        connection.setPassw(binding.passwEditText.getText().toString());
        CustomApplication.getInstance().getDatabaseAdapter().saveConnection(connection);
    }

    @Override
    public void onBackPressed() {
        saveConnection();
        super.onBackPressed();
    }
}
