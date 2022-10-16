package com.chessboard.tc_chessboard;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import tech.gusavila92.websocketclient.WebSocketClient;

public class Game_lobby extends FragmentActivity {

    DB_handler db_handler;
    AlertDialog dialog;
    WebSocketClient client;
    String username;

    Button local_game;
    Button online_game;

    String onlinePlayerName;
    int difficulty;
    String colour_play_as = "White";
    String clock = "noClock" ;
    String IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_lobby);

        IP = getIntent().getExtras().getString("IP");

        //createWebSocketClient.createClient(IP);
        client = createWebSocketClient.getWebSocketClient();
        db_handler = new DB_handler();
        dialog = new AlertDialog.Builder(Game_lobby.this).create();

        // Objects from the game_lobby layout
        local_game  = findViewById(R.id.gl_local_game);
        online_game = findViewById(R.id.gl_online_game);

        //---------------------------------------------------------
        //------                Local Game                   ------
        //---------------------------------------------------------


        local_game.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                setContentView(R.layout.game_settings);

                username = getIntent().getExtras().getString("username");

                // Objects from the layout Game Settings
                TextView play_as = findViewById(R.id.gs_play_as);

                SeekBar seekBar = findViewById(R.id.gs_difficulty);
                Switch colour   = findViewById(R.id.gs_colour); // Default: white

                Button noClock = findViewById(R.id.gs_noClock);
                Button time15  = findViewById(R.id.gs_15min);
                Button time30  = findViewById(R.id.gs_30min);
                Button start   = findViewById(R.id.gs_start_game);


                //  -----------       Difficulty       ---------------

                seekBar.setMin(1);
                seekBar.setProgress(1);
                seekBar.incrementProgressBy(1);
                seekBar.setMax(10);


                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        difficulty = progress;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });


                //  -----------     Colour player      ---------------


                colour.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint({"UseCompatLoadingForColorStateLists", "SetTextI18n"})
                    @Override
                    public void onClick(View view) {
                        if (!colour.isChecked()){
                            play_as.setText("White");
                            colour_play_as = "White";
                        } else {
                            play_as.setText("Black");
                            colour_play_as = "Black";
                        }
                    }
                });


                //  -----------     Clock Buttons      ---------------


                noClock.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("UseCompatLoadingForColorStateLists")
                    @Override
                    public void onClick(View view) {
                        // Set other buttons background to gray
                        noClock.setBackgroundTintList(getResources().getColorStateList(R.color.Maroon));
                        time15.setBackgroundTintList(getResources().getColorStateList(R.color.Gray));
                        time30.setBackgroundTintList(getResources().getColorStateList(R.color.Gray));

                        // clock identifier
                        clock = "noClock";
                    }
                });

                time15.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("UseCompatLoadingForColorStateLists")
                    @Override
                    public void onClick(View view) {
                        // Set other buttons background to gray
                        noClock.setBackgroundTintList(getResources().getColorStateList(R.color.Gray));
                        time15.setBackgroundTintList(getResources().getColorStateList(R.color.Maroon));
                        time30.setBackgroundTintList(getResources().getColorStateList(R.color.Gray));

                        // clock identifier
                        clock = "15min";
                    }
                });

                time30.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("UseCompatLoadingForColorStateLists")
                    @Override
                    public void onClick(View view) {
                        // Set other buttons background to gray
                        noClock.setBackgroundTintList(getResources().getColorStateList(R.color.Gray));
                        time15.setBackgroundTintList(getResources().getColorStateList(R.color.Gray));
                        time30.setBackgroundTintList(getResources().getColorStateList(R.color.Maroon));

                        // clock identifier
                        clock = "30min";
                    }
                });

                // -----------------------------------------------------

                // Send this information to the board, and start playing
                start.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("UseCompatLoadingForColorStateLists")
                    @Override
                    public void onClick(View view) {
                        //db_handler.getIPAddress("TCC-000-001"); // TODO: this should be replaced by something (client.createClient("192.168.68.105"))
                        JSONObject jsonObj = new JSONObject();

                        try {
                            jsonObj.put("Color", colour_play_as);
                            jsonObj.put("Difficulty", difficulty);
                            jsonObj.put("Clock", clock);
                        } catch (JSONException e) {
                            e.printStackTrace(); //TODO: handle exception better!
                        }

                        String rules = jsonObj.toString();
                        //client.send(rules);

                        // Start the game
                        Intent initiate = new Intent (Game_lobby.this, ChessApp.class);
                        initiate.putExtra("username", username);
                        initiate.putExtra("IP", IP);
                        startActivity(initiate);
                    }
                });

            }
        });


        //---------------------------------------------------------
        //------               Online Game                   ------
        //---------------------------------------------------------

        online_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.online_player_matching);

                EditText on_username = findViewById(R.id.op_username);
                ImageButton search   = findViewById(R.id.op_search_username);

                onlinePlayerName = on_username.getText().toString();

                search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setContentView(R.layout.game_settings);

                        // Objects from the layout Game Settings
                        TextView play_as = findViewById(R.id.gs_play_as);
                        TextView labels  = findViewById(R.id.gs_difficulty_labels);
                        TextView userN   = findViewById(R.id.gs_online_username);
                        TextView user    = findViewById(R.id.gs_userID);

                        SeekBar seekBar = findViewById(R.id.gs_difficulty);
                        Switch colour   = findViewById(R.id.gs_colour); // Default: white

                        Button noClock = findViewById(R.id.gs_noClock);
                        Button time15  = findViewById(R.id.gs_15min);
                        Button time30  = findViewById(R.id.gs_30min);
                        Button start   = findViewById(R.id.gs_start_game);


                        //  -----------       Online user       ---------------


                        seekBar.setVisibility(View.GONE);
                        labels.setVisibility(View.GONE);
                        userN.setVisibility(View.VISIBLE);
                        user.setVisibility(View.VISIBLE);

                        userN.setText(onlinePlayerName);


                        //  -----------     Colour player      ---------------


                        colour.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint({"UseCompatLoadingForColorStateLists", "SetTextI18n"})
                            @Override
                            public void onClick(View view) {
                                if (colour.isChecked()){
                                    play_as.setText("White");
                                    colour_play_as = "White";
                                } else {
                                    play_as.setText("Black");
                                    colour_play_as = "Black";
                                }
                            }
                        });


                        //  -----------     Clock Buttons      ---------------


                        noClock.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("UseCompatLoadingForColorStateLists")
                            @Override
                            public void onClick(View view) {
                                // Set other buttons background to gray
                                noClock.setBackgroundTintList(getResources().getColorStateList(R.color.Maroon));
                                time15.setBackgroundTintList(getResources().getColorStateList(R.color.Gray));
                                time30.setBackgroundTintList(getResources().getColorStateList(R.color.Gray));

                                // clock identifier
                                clock = "noClock";
                            }
                        });

                        time15.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("UseCompatLoadingForColorStateLists")
                            @Override
                            public void onClick(View view) {
                                // Set other buttons background to gray
                                noClock.setBackgroundTintList(getResources().getColorStateList(R.color.Gray));
                                time15.setBackgroundTintList(getResources().getColorStateList(R.color.Maroon));
                                time30.setBackgroundTintList(getResources().getColorStateList(R.color.Gray));

                                // clock identifier
                                clock = "15min";
                            }
                        });

                        time30.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("UseCompatLoadingForColorStateLists")
                            @Override
                            public void onClick(View view) {
                                // Set other buttons background to gray
                                noClock.setBackgroundTintList(getResources().getColorStateList(R.color.Gray));
                                time15.setBackgroundTintList(getResources().getColorStateList(R.color.Gray));
                                time30.setBackgroundTintList(getResources().getColorStateList(R.color.Maroon));

                                // clock identifier
                                clock = "30min";
                            }
                        });

                        // -----------------------------------------------------

                        // Send this information to the board, and start playing
                        start.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("UseCompatLoadingForColorStateLists")
                            @Override
                            public void onClick(View view) {
                                // TODO: send this information through the web socket

                                // Start the game
                                Intent initiate = new Intent (Game_lobby.this, ChessApp.class);
                                startActivity(initiate);
                            }
                        });
                    }
                });
            }
        });

    }
}
