package com.chessboard.tc_chessboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;

public class RegisterBoard extends FragmentActivity {

    DB_handler db_handler;
    AlertDialog dialog;

    Button register;
    EditText boardID;
    EditText code;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_board);

        username = getIntent().getExtras().getString("username");

        db_handler = new DB_handler();
        dialog = new AlertDialog.Builder(RegisterBoard.this).create();

        // Initialization of objects
        register = findViewById(R.id.rb_registerBoard);
        boardID  = findViewById(R.id.rb_boardID);
        code     = findViewById(R.id.rb_register_code);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rCode = code.getText().toString();
                String bID   = boardID.getText().toString();

                if (!rCode.isEmpty() || !bID.isEmpty()){
                    try {
                        boolean valid = db_handler.registerBoard(username, rCode, bID);
                        if (valid){
                            dialog.setTitle("Successful registration");
                            dialog.setMessage("This board was registered with this admin: " + username);
                            dialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });

                            // Showing Alert Message
                            dialog.show();
                        } else {
                            dialog.setTitle("Invalid inputs");
                            dialog.setMessage("Please enter valid a board serial and register code");
                            dialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            // Showing Alert Message
                            dialog.show();
                        }
                    } catch (Exception e ){
                        dialog.setTitle("User already registered with this board");
                        dialog.setMessage("Please enter other board serial and register code");
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
    }
}
