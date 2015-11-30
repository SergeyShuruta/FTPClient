package com.shuruta.sergey.ftpclient.ui.activitys;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.shuruta.sergey.ftpclient.R;
import com.shuruta.sergey.ftpclient.databinding.ActivityAddconBinding;
import com.shuruta.sergey.ftpclient.entity.Connection;

/**
 * Created by Sergey on 24.07.2015.
 */
public class AddConActivity extends BaseActivity {

    private Connection connection;
    private ActivityAddconBinding binding;
    private static final String CONNECTION = "var_connection";
    public static final String CONNECTION_ID = "connection_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_addcon);
        connection = null == savedInstanceState
                ? new Connection(getIntent().getLongExtra(CONNECTION_ID, 0))
                : (Connection) savedInstanceState.getParcelable(CONNECTION);
        binding.setConnection(connection);
        setupToolBar(R.string.app_name
                , getString(connection.getId() == 0 ? R.string.new_connection : R.string.edit_connection_x
                , connection.getName()), ToolBarButton.ACCEPT);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(CONNECTION, connection);
        super.onSaveInstanceState(outState);
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
    protected void onPause() {
        super.onPause();
        makeConnection();
    }

    private void makeConnection() {
        connection.setName(binding.nameEditText.getText().toString());
        connection.setHost(binding.hostEditText.getText().toString());
        connection.setPort(binding.portEditText.getText().toString());
        connection.setDir(binding.dirEditText.getText().toString());
        connection.setLocalDir(binding.ldirEditText.getText().toString());
        connection.setLogin(binding.loginEditText.getText().toString());
        connection.setPassw(binding.passwEditText.getText().toString());
    }

    @Override
    public void onBackPressed() {
        makeConnection();
        if (connection.isCanSave())
            connection.save();
        super.onBackPressed();
    }
}
