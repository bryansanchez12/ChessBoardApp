package com.chessboard.tc_chessboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import tech.gusavila92.websocketclient.WebSocketClient;

public class ConnectToBoard extends FragmentActivity {

    DB_handler db_handler;
    AlertDialog dialog;
    String username;

    EditText boardID;
    ImageButton connect;

    WebSocketClient webSocketClient;
    createWebSocketClient client;

    Lock lock = new ReentrantLock();
    public static AtomicBoolean received = new AtomicBoolean(); //TODO: maybe not correct way?

    String IP_address = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_to_board);

        db_handler = new DB_handler();
        username = getIntent().getExtras().getString("username");
        dialog = new AlertDialog.Builder(ConnectToBoard.this).create();

        boardID = findViewById(R.id.ctb_boardID);
        connect = findViewById(R.id.ctb_connect);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bID = boardID.getText().toString();

                if (bID.isEmpty()) {
                    dialog.setTitle("Empty input");
                    dialog.setMessage("Please enter valid board ID");
                    dialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    // Showing Alert Message
                    dialog.show();
                } else {
                    connectToBoard(bID);
                    long start = System.currentTimeMillis();
                    while (!received.get() && System.currentTimeMillis() - start < 3000) {
                        dialog.setTitle("Waiting for connection");
                        dialog.show();
                    }
                    if (received.get()) {

                        Intent initiate = new Intent(ConnectToBoard.this, Game_lobby.class);
                        initiate.putExtra("username", username);
                        initiate.putExtra("IP", IP_address );
                        //TODO: create way to give websocket all classes!
                        startActivity(initiate);

                    } else {
                        dialog.setTitle("Connection interrupted");
                        dialog.setMessage("Please try again with a valid board ID");
                        dialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                    }
                    //TODO: go back to previous page


//                    if (received.get()) {
//                        //   Toast.makeText(getApplicationContext(),
//                        //           "Successful connection!", Toast.LENGTH_SHORT).show();
//                        Intent initiate = new Intent(ConnectToBoard.this, Game_lobby.class);
//                        initiate.putExtra("username", username);
//                        startActivity(initiate);
//                    } else {
//                        dialog.setTitle("Connection interrupted");
//                        dialog.setMessage("Please try again with a valid board ID");
//                        dialog.setButton("OK", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        });
//                    }
                }
            }
        });

    }

    /**
     * Creates an active web socket that is connected to the IP address of the board
     *
     * @param boardID board ID
     * @return true if the connection was successfully, false otherwise
     */
    public void connectToBoard(String boardID) {
        IP_address = db_handler.getIPAddress(boardID);
        //     Toast.makeText(getApplicationContext(),
        //             "The server says: " + IP_address, Toast.LENGTH_SHORT).show();
//        URI uri = null;
//        try {
//            // Connect to local host
//            uri = new URI("ws://" + IP_address + ":8080");  //"ws://10.0.2.2:8080/websocket"
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        } //TODO: maybe change back;

        if (IP_address != null) {

            client = new createWebSocketClient(IP_address, null, this);
//            client.run();
//            createWebSocketClient.createClient(IP_address);
//            webSocketClient = createWebSocketClient.getWebSocketClient();
            if(webSocketClient != null){
                received.set(true);
            }
//            webSocketClient = new WebSocketClient(uri) {
//                @Override
//                public void onOpen() {
//                    //TODO: is this put on the right place?
//                    JSONObject message = new JSONObject();
//                    try {
//                        message.put("uid", 1234);
//                    } catch (JSONException e) {
//                        e.printStackTrace(); //TODO: change exception
//                    }
//
//                    Log.i("WebSocket", "Session is starting");
//                    webSocketClient.send(message.toString());
//                }
//
//                @Override
//                public void onTextReceived(String s) {
//                    Log.i("WebSocket", "Message received");
//                    Log.i("answer", s);
//                    final String message = s;
//                    //    Toast.makeText(getApplicationContext(),
//                    //            "The server says: " + s, Toast.LENGTH_SHORT).show();
//                    received.set(true); //TODO: maybe not correct way?
//                }
//
//                @Override
//                public void onBinaryReceived(byte[] data) {
//                }
//
//                @Override
//                public void onPingReceived(byte[] data) {
//                }
//
//                @Override
//                public void onPongReceived(byte[] data) {
//                }
//
//                @Override
//                public void onException(Exception e) {
//                    System.out.println(e.getMessage());
//                }
//
//                @Override
//                public void onCloseReceived() {
//                    Log.i("WebSocket", "Closed ");
//                    System.out.println("onCloseReceived");
//                }
//            }; //TODO: either remove or return to old state

//            webSocketClient.setConnectTimeout(100000);
//            webSocketClient.setReadTimeout(100000);
//            webSocketClient.enableAutomaticReconnection(5000);
//            webSocketClient.connect();
        }
    }

    public void setReceived(boolean a){
        received.set(a);
    }

    public createWebSocketClient getClient() {
        return client;
    }
}
