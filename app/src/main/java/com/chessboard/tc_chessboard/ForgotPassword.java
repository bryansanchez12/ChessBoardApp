package com.chessboard.tc_chessboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

public class ForgotPassword extends FragmentActivity {

    DB_handler db_handler;
    AlertDialog dialog;

    Button send_email;
    EditText username_fp;
    EditText email_fp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        db_handler = new DB_handler();
        dialog = new AlertDialog.Builder(ForgotPassword.this).create();

        username_fp = findViewById(R.id.usernameF);
        email_fp    = findViewById(R.id.emailF);

        send_email = findViewById(R.id.send_email);

        // Checks the inputs and send an email with a recovery password
        send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username      = username_fp.getText().toString();
                String email_address = email_fp.getText().toString();

                if (!username.isEmpty() || !email_address.isEmpty()){
                    sendRecoveryEmail(username, email_address);
                } else {
                    dialog.setTitle("Empty inputs");
                    dialog.setMessage("Please enter valid username and email");
                    dialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog
                            // closed
                            Toast.makeText(getApplicationContext(),
                                    "You clicked on OK", Toast.LENGTH_SHORT).show();
                            setContentView(R.layout.forgot_password);
                        }
                    });

                    // Showing Alert Message
                    dialog.show();
                }
            }
        });
    }

    public void sendRecoveryEmail(String username, String email_address){

    }
}
