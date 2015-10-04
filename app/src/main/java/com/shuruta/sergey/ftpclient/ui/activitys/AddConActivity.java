package com.shuruta.sergey.ftpclient.ui.activitys;

import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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

    //private ConForm conForm;
    private Connection connection;
    private ActivityAddconBinding binding;
    //private boolean isChanged = false;
    public static final String CONNECTION_ID = "connection_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_addcon);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_addcon);
        connection = new Connection(getIntent().getLongExtra(CONNECTION_ID, 0));
        binding.setConnection(connection);
        //conForm = new ConForm(getWindow().getDecorView(), this, connection);
        setupToolBar(R.drawable.ic_launcher,
                R.string.app_name,
                getString(connection.getId() == 0 ? R.string.new_connection : R.string.edit_connection_x, connection.getName()),
                null, null);
    }

    @Override
    public void onClick(View v) {
        saveConnection();
    }

    private void saveConnection() {
        Log.d("TEST", binding.nameEditText.getText().toString());
        String dir = binding.dirEditText.getText().toString();
        dir = dir.isEmpty() ? File.separator : dir;
        connection.setName(binding.nameEditText.getText().toString());
        connection.setHost(binding.hostEditText.getText().toString());
        //connection.setPort(Integer.parseInt(binding.portEditText.getText().toString()));
        //connection.setDir(dir);
        connection.setLogin(binding.loginEditText.getText().toString());
        connection.setPassw(binding.passwEditText.getText().toString());
        CustomApplication.getInstance().getDatabaseAdapter().saveConnection(connection);
    }

    @Override
    public void onBackPressed() {
        saveConnection();
        super.onBackPressed();
    }

/*    private class ConForm {

        public final EditText
                nameEditText,
                hostEditText,
                portEditText,
                dirEditText,
                loginEditText,
                passwEditText;
        public final Button saveButton;

        public ConForm(View view, View.OnClickListener onClickListener, Connection connection) {
            this.nameEditText  = (EditText) view.findViewById(R.id.nameEditText);
            this.hostEditText  = (EditText) view.findViewById(R.id.hostEditText);
            this.portEditText  = (EditText) view.findViewById(R.id.portEditText);
            this.dirEditText = (EditText) view.findViewById(R.id.dirEditText);
            this.loginEditText = (EditText) view.findViewById(R.id.loginEditText);
            this.passwEditText = (EditText) view.findViewById(R.id.passwEditText);

            this.nameEditText.addTextChangedListener(textChangedListener);
            this.hostEditText.addTextChangedListener(textChangedListener);
            this.portEditText.addTextChangedListener(textChangedListener);
            this.dirEditText.addTextChangedListener(textChangedListener);
            this.loginEditText.addTextChangedListener(textChangedListener);
            this.passwEditText.addTextChangedListener(textChangedListener);

            this.saveButton    = (Button) view.findViewById(R.id.saveButton);
            this.saveButton.setOnClickListener(onClickListener);
            fillForm(connection);
        }

        private void fillForm(Connection connection) {
            this.nameEditText.setText(connection.getName());
            this.hostEditText.setText(connection.getHost());
            this.portEditText.setText(String.valueOf(connection.getPort()));
            this.dirEditText.setText(connection.getDir());
            this.loginEditText.setText(connection.getLogin());
            this.passwEditText.setText(connection.getPassw());
        }

        private boolean checkFieldError(EditText editText) {
            boolean isError = editText.getText().toString().isEmpty();
            editText.setBackgroundColor(getResources().getColor(isError ? R.color.field_error : android.R.color.transparent));
            return isError;
        }

        public boolean isFormValid() {
            boolean isValid = true;
            if(checkFieldError(nameEditText)) isValid = false;
            if(checkFieldError(hostEditText)) isValid = false;
            return isValid;
        }

        private TextWatcher textChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isChanged = count > 0;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }*/
}
