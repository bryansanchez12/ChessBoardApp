package com.chessboard.tc_chessboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

public class AccountSettings extends FragmentActivity {
    Button changePassword;
    EditText oldPassword;
    EditText newPassword;
    TextView as_username;

    String username;
    DB_handler db_handler;

    // Button only available for admin users
    Button addUsersToBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_settings);

        username = getIntent().getExtras().getString("username");
        db_handler = new DB_handler();

        // Button onl y available for admin users
        addUsersToBoard = findViewById(R.id.as_add_u_to_board);

        changePassword = findViewById(R.id.as_change_password);
        oldPassword    = findViewById(R.id.as_old_password);
        newPassword    = findViewById(R.id.as_new_password);
        as_username    = findViewById(R.id.as_username);

        // Displaying with the current username
        as_username.setText(username);

        // If this user is admin, display the button
        if (isAdminUser(username)){
            addUsersToBoard.setVisibility(View.VISIBLE);
        }

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: verify this transaction and send request to change
            }
        });

        addUsersToBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent initiate = new Intent (AccountSettings.this, AddUsers.class);
                initiate.putExtra("username", username);
                startActivity(initiate);
            }
        });
    }

    /**
     * Method that sends an HTTP GET request in order to know if such a user is
     *    an admin user of this board
     * @param username of the player using the app
     * @return true if this user is admin, false otherwise
     */
    public boolean isAdminUser(String username){
        System.out.println("$$$$$$$$$$$ " + db_handler.isAdminUser(username));
        return db_handler.isAdminUser(username);
    }
}
