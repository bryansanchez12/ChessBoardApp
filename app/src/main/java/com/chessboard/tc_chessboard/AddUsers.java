package com.chessboard.tc_chessboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AddUsers extends FragmentActivity {

    TextView boardID;
    EditText username;
    ImageButton addUser;
    AlertDialog dialog;

    String admin_username;
    String boardIDText = "";

    DB_handler db_handler;
    List<User_Model> userList = new ArrayList<>();

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    User_Adapter user_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //View root = View.inflate()
        recyclerView = (RecyclerView) findViewById(R.id.ru_users_list);
        System.out.println("8888888888888888888888888");
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        System.out.println("8888888888888888888888888");
        setContentView(R.layout.registered_users);

        db_handler = new DB_handler();
        dialog = new AlertDialog.Builder(AddUsers.this).create();
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        admin_username = getIntent().getExtras().getString("username");

        boardID  = findViewById(R.id.ru_board_id);
        username = findViewById(R.id.ru_username);
        addUser  = findViewById(R.id.ru_register_username);
        boardIDText = db_handler.getBoardID(admin_username);

        boardID.setText(boardIDText);

        //initUserList();

        updateUsersTable();


        user_adapter = new User_Adapter(userList);
        user_adapter.notifyDataSetChanged();
        recyclerView.setAdapter(user_adapter);

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: verify the entries and add new user
                String name  = username.getText().toString();

                if (!name.isEmpty()){
                    try {
                        boolean valid = db_handler.addNewUserToBoard(name, boardIDText);
                        if (valid){
                            dialog.setTitle("Successful registration");
                            dialog.setMessage("This board was registered with this admin: " + username);
                            dialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //TODO: update list of users registered with this board
                                }
                            });

                            // Showing Alert Message
                            dialog.show();

                            finish();
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


    public void updateUsersTable(){
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        List<String> users = db_handler.getUsersRegistered(boardIDText);
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        for (String name: users){
            userList.add(new User_Model(name, boardIDText));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initUserList() {
        //recyclerView = findViewById(R.id.ru_users_list);
        //linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        //recyclerView.setLayoutManager(linearLayoutManager);
        //user_adapter = new User_Adapter(userList);
        //recyclerView.setAdapter(user_adapter);
        //user_adapter.notifyDataSetChanged();
        updateUsersTable();

        // Lookup the recyclerview in activity layout

        recyclerView.setLayoutManager(layoutManager);


    }

}
