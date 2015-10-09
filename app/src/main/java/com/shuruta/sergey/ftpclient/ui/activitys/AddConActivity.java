package com.shuruta.sergey.ftpclient.ui.activitys;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.shuruta.sergey.ftpclient.CustomApplication;
import com.shuruta.sergey.ftpclient.R;
import com.shuruta.sergey.ftpclient.databinding.ActivityAddconBinding;
import com.shuruta.sergey.ftpclient.entity.Connection;

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
                getString(connection.getId() == 0 ? R.string.new_connection : R.string.edit_connection_x, connection.getName()), true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View v) {
        saveConnection();
    }

    private void saveConnection() {
        connection.setName(binding.nameEditText.getText().toString());
        connection.setHost(binding.hostEditText.getText().toString());
        connection.setPort(binding.portEditText.getText().toString());
        connection.setDir(binding.dirEditText.getText().toString());
        connection.setLogin(binding.loginEditText.getText().toString());
        connection.setPassw(binding.passwEditText.getText().toString());
        if(connection.getName().isEmpty()) return;
        connection.save();
    }

    @Override
    public void onBackPressed() {
        saveConnection();
        super.onBackPressed();
    }
}
