package com.chessboard.tc_chessboard;

import android.os.Build;
import android.util.Log;

//import androidx.annotation.RequiresApi;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;
import tech.gusavila92.websocketclient.WebSocketClient;

import java.net.URI;
import java.net.URISyntaxException;

public class createWebSocketClient implements Runnable { //TODO: put in a run!

    static public WebSocketClient webSocketClient;
    public static ChessApp chessApp;
    public static ConnectToBoard connBoard;
    public String IP_address;

    public createWebSocketClient(String IP_address, ChessApp app, ConnectToBoard conn){
        chessApp = app;
        connBoard = conn;
        setIP_address(IP_address);
        run();
    }

    public static void createClient(String IP_address) {
        int uid = 1234; //TODO: don't let this be hardcoded!
        //      if (webSocketClient == null){
        URI uri;
        try {
            // Connect to local host
            uri = new URI("ws://" + IP_address + ":8080");  //"ws://10.0.2.2:8080/websocket"
            webSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen() {
                    //TODO: is this put on the right place?
                    JSONObject message = new JSONObject();
                    try {
                        message.put("uid", uid);
                    } catch (JSONException e) {
                        e.printStackTrace(); //TODO: change exception
                    }

                    Log.i("WebSocket", "Session is starting");
                    webSocketClient.send(message.toString());
                }

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onTextReceived(String s) {
                    Log.i("WebSocket", "Message received");
                    Log.i("answer", s);
                    //String message = s;

                    JSONObject obj;

                    try {
                        obj = new JSONObject(s);
    //                    JSONObject obj = new JSONObject();
    //                    for (int i = 0; i < mess.length() ;i++){
    //                        obj = mess.getJSONObject(i);
    //                    }

                        if (obj.has("From")) {

                            // Handle the response from the server with a thread
                            try{
                                chessApp.serverHandler(obj);
                            } catch (Exception e){
                                e.printStackTrace();
                            }

                            // Send acknowledgement   //TODO: is this really needed?
                            JSONObject message = new JSONObject();
                            try {
                                message.put("uid", uid);
                                message.put("ack", "received");
                            } catch (JSONException e) {
                                e.printStackTrace(); //TODO: change exception
                            }

                        }
                        if (obj.has("uid")) {
                            Log.i("the uid:", obj.getString("uid"));
                            if (obj.getInt("uid") == uid) {
                                connBoard.setReceived(true);
                            } else {
                                //TODO: when connection can not be made?!
                            }
                        }
                        if (obj.has("err")) {
                            //err -> string
                            //TODO: create a way to show an error and handle it!
                        }
                        if (obj.has("end")) {
                            switch (obj.getString("end")) {
                                case "win":
                                    chessApp.displayWinner(obj.getString("winner"));
                                    break;
                                case "draw":
                                    //TODO: create a way to show you have a draw
                                    break;
                                case "int":
                                    //TODO: create a way to show an interuption
                                    break;
                            }
                            // end _> win, draw, int
                            // winner _> white, black, null
                        } else {
                            //TODO: handle if not!
                        }
                    } catch (JSONException e) {
                        e.printStackTrace(); //TODO: better handeling exception
                    }

                    //    Toast.makeText(getApplicationContext(),
                    //            "The server says: " + s, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onBinaryReceived(byte[] data) {
                }

                @Override
                public void onPingReceived(byte[] data) {
                }

                @Override
                public void onPongReceived(byte[] data) {
                }

                @Override
                public void onException(Exception e) {
                    System.out.println("$$$$$$$ Hello ");
                    System.out.println(e.getMessage());
                }

                @Override
                public void onCloseReceived() {
//                webSocketClient.close();
//                webSocketClient = null;
                    Log.i("WebSocket", "Closed ");
                    System.out.println("onCloseReceived");
                }
            };
            webSocketClient.setConnectTimeout(10000);
            webSocketClient.setReadTimeout(1000000);
            webSocketClient.enableAutomaticReconnection(5000);
            webSocketClient.connect();
            // }
            //  return getWebSocketClient();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            //  return null; //TODO: handle exception
        }
    }

    public static WebSocketClient getWebSocketClient() {
        return webSocketClient;
    }

    @Override
    public void run() {
        createClient(IP_address);
    }

    public void setIP_address(String IP_address) {
        this.IP_address = IP_address;
    }

    public static void setChessApp(ChessApp app){
        chessApp = app;
    }

    public static void setConnBoard(ConnectToBoard conn){
        connBoard = conn;
    }


}

