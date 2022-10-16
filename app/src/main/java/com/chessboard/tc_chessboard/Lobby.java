package com.chessboard.tc_chessboard;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

public class Lobby extends FragmentActivity {

    DB_handler db_handler;
    AlertDialog dialog;
    String username;

    Button connectToBoard;
    Button registerBoard;
    Button accountStetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        db_handler = new DB_handler();
        username = getIntent().getExtras().getString("username");
        dialog = new AlertDialog.Builder(Lobby.this).create();

        connectToBoard  = findViewById(R.id.connectToBoard);
        registerBoard   = findViewById(R.id.registerBoard);
        accountStetting = findViewById(R.id.accountSettings);

        // Start an activity that allows the user to see their settings
        accountStetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent initiate = new Intent (Lobby.this, AccountSettings.class);
                initiate.putExtra("username", username);
                startActivity(initiate);
            }
        });

        // Start an activity that allows the user to register a new board
        registerBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent initiate = new Intent (Lobby.this, RegisterBoard.class);
                initiate.putExtra("username", username);
                startActivity(initiate);
            }
        });

        // Start an activity that allows the user to connect to a board
        connectToBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent initiate = new Intent (Lobby.this, ConnectToBoard.class);
                initiate.putExtra("username", username);
                startActivity(initiate);
            }
        });
    }
}
