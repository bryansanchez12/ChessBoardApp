package com.chessboard.tc_chessboard;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class StartPage extends AppCompatActivity {

    DB_handler db_handler;

    public String username;

    // Start page layout objects
    Button login;
    Button newAccount;
    Button forgot_password;
    EditText username_login;
    EditText password_login;

    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page);
        db_handler = new DB_handler();
        dialog = new AlertDialog.Builder(StartPage.this).create();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        // -----------------    Buttons initialization      ------------------

        login = findViewById(R.id.login);
        newAccount = findViewById(R.id.newUser);
        forgot_password = findViewById(R.id.forgotPassword);

        // ---------------    Text inputs initialization    -----------------

        username_login = findViewById(R.id.usernameL);
        password_login = findViewById(R.id.passwordL);

        //  ------------------------------------------------------------------



        // Fetch data from inputs and send it for comparing with DB
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username2 = username_login.getText().toString();
                String password  = password_login.getText().toString();

                if (!username2.isEmpty() || !password.isEmpty()){
                    if (db_handler.verifyUserCredentialsFromDB(username2, password)){
                        username = username2;
                        Intent initiate = new Intent (StartPage.this, Lobby.class);
                        initiate.putExtra("username", username);
                        startActivity(initiate);
                    } else {
                        dialog.setTitle("Failed to login");
                        dialog.setMessage("Please enter valid username and password");
                        dialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        // Showing Alert Message
                        dialog.show();
                    }
                } else {
                    dialog.setTitle("Empty inputs");
                    dialog.setMessage("Please enter valid username and password");
                    dialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    // Showing Alert Message
                    dialog.show();
                }

            }
        });

        // Change the layout to display the form Forgot Password
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent initiate = new Intent (StartPage.this, ForgotPassword.class);
                startActivity(initiate);
            }
        });

        // Change the layout to display the form Register New User
        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent initiate = new Intent (StartPage.this, CreateUser.class);
                startActivity(initiate);

            }
        });
    }

    /**
     * Returns the username that was correctly logged in
     * @return username of the player
     */
    public String getUsername() {
        return username;
    }
}