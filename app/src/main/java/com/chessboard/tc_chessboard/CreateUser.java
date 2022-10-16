package com.chessboard.tc_chessboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;

public class CreateUser extends FragmentActivity {

    DB_handler db_handler;
    AlertDialog dialog;

    Button register;
    EditText username_cu;
    EditText email_cu;
    EditText password_cu;
    EditText password_a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user);

        db_handler = new DB_handler();
        dialog = new AlertDialog.Builder(CreateUser.this).create();

        username_cu = findViewById(R.id.usernameR);
        email_cu    = findViewById(R.id.emailR);
        password_cu = findViewById(R.id.passwordR);
        password_a = findViewById(R.id.passwordAgain);

        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username      = username_cu.getText().toString();
                String email_address = email_cu.getText().toString();
                String password      = password_cu.getText().toString();
                String password_ag   = password_a.getText().toString();


                if (username.isEmpty() || email_address.isEmpty()
                        || password.isEmpty() || password_ag.isEmpty()){

                    dialog.setTitle("Empty inputs");
                    dialog.setMessage("Please enter valid username, email and password");
                    dialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    // Showing Alert Message
                    dialog.show();


                } else if(!password.equals(password_ag)){
                    dialog.setTitle("Different passwords");
                    dialog.setMessage("The passwords specified must be identical");
                    dialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            password_cu.setText("");
                            password_a.setText("");
                        }
                    });

                    // Showing Alert Message
                    dialog.show();
                } else {
                    boolean tmp = db_handler.registerNewUser(username, email_address, password);

                    if (tmp){
                        dialog.setTitle("Registration Completed");
                        dialog.setMessage("User " + username + " was registered successfully!");
                        dialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                setContentView(R.layout.start_page);
                            }
                        });

                        // Showing Alert Message
                    } else {
                        dialog.setTitle("Registration failed");
                        dialog.setMessage("Please enter valid username, email and password");
                        dialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                    }
                    dialog.show();
                }
            }
        });
    }
}
