package com.shuruta.sergey.ftpclient.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.shuruta.sergey.ftpclient.CustomApplication;
import com.shuruta.sergey.ftpclient.R;
import com.shuruta.sergey.ftpclient.database.DatabaseAdapter;
import com.shuruta.sergey.ftpclient.database.entity.Connection;

import java.util.List;

/**
 * Created by Sergey on 24.07.2015.
 */
public class AddConActivity extends Activity implements View.OnClickListener {

    private ConForm conForm;
    private Connection connection;
    public static final String CONNECTION_ID = "connection_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcon);
        connection = new Connection(getIntent().getLongExtra(CONNECTION_ID, 0));
        conForm = new ConForm(getWindow().getDecorView(), this, connection);
    }

    @Override
    public void onClick(View v) {
        if(!conForm.isFormValid()) return;
        connection.setName(conForm.nameEditText.getText().toString());
        connection.setHost(conForm.hostEditText.getText().toString());
        connection.setPort(Integer.parseInt(conForm.portEditText.getText().toString()));
        connection.setLogin(conForm.loginEditText.getText().toString());
        connection.setPassw(conForm.passwEditText.getText().toString());
        CustomApplication.getInstance().getDatabaseAdapter().saveConnection(connection);
        finish();
    }

    @Override
    public void onBackPressed() {
        if(connection.getId() > 0) {
            DialogUtility.showDialog(
                    AddConActivity.this,
                    R.string.changes_not_saved_still_close_ask,
                    R.string.yes,
                    R.string.no,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AddConActivity.super.onBackPressed();
                        }
                    }
            );
        } else {
            super.onBackPressed();
        }
    }

    private class ConForm {

        public final EditText
                nameEditText,
                hostEditText,
                portEditText,
                loginEditText,
                passwEditText;
        public final Button saveButton;

        public ConForm(View view, View.OnClickListener onClickListener, Connection connection) {
            this.nameEditText  = (EditText) view.findViewById(R.id.nameEditText);
            this.hostEditText  = (EditText) view.findViewById(R.id.hostEditText);
            this.portEditText  = (EditText) view.findViewById(R.id.portEditText);
            this.loginEditText = (EditText) view.findViewById(R.id.loginEditText);
            this.passwEditText = (EditText) view.findViewById(R.id.passwEditText);
            this.saveButton    = (Button) view.findViewById(R.id.saveButton);
            this.saveButton.setOnClickListener(onClickListener);
            fillForm(connection);
        }

        private void fillForm(Connection connection) {
            this.nameEditText.setText(connection.getName());
            this.hostEditText.setText(connection.getHost());
            this.portEditText.setText(String.valueOf(connection.getPort()));
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
    }
}
