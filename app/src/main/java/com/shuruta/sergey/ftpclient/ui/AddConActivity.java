package com.shuruta.sergey.ftpclient.ui;

import android.app.Activity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcon);
        conForm = new ConForm(getWindow().getDecorView(), this);
    }

    @Override
    public void onClick(View v) {
        if(!conForm.isValid()) return;
        Connection connection = new Connection(
                conForm.nameEditText.getText().toString(),
                conForm.hostEditText.getText().toString(),
                Integer.parseInt(conForm.portEditText.getText().toString()));
        connection.setLogin(conForm.loginEditText.getText().toString());
        connection.setPassw(conForm.passwEditText.getText().toString());
        CustomApplication.getInstance().getDatabaseAdapter().saveConnection(connection);
    }

    private class ConForm {

        public final EditText
                nameEditText,
                hostEditText,
                portEditText,
                loginEditText,
                passwEditText;
        public final Button saveButton;

        public ConForm(View view, View.OnClickListener onClickListener) {
            this.nameEditText  = (EditText) view.findViewById(R.id.nameEditText);
            this.hostEditText  = (EditText) view.findViewById(R.id.hostEditText);
            this.portEditText  = (EditText) view.findViewById(R.id.portEditText);
            this.loginEditText = (EditText) view.findViewById(R.id.loginEditText);
            this.passwEditText = (EditText) view.findViewById(R.id.passwEditText);
            this.saveButton    = (Button) view.findViewById(R.id.saveButton);
            this.saveButton.setOnClickListener(onClickListener);
        }

        public boolean isValid() {

            boolean isValid = true;
            checkFieldError(nameEditText, nameEditText.getText().toString().isEmpty(), isValid);
            checkFieldError(hostEditText, hostEditText.getText().toString().isEmpty(), isValid);
            return isValid;
        }

        private void checkFieldError(View view, boolean isError, boolean isValid) {
            view.setBackgroundColor(getResources().getColor(isError ? R.color.field_error : android.R.color.white));
            isValid = isValid ? !isError : isValid;
        }
    }
}
