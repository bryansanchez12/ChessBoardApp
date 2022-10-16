package com.chessboard.tc_chessboard;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import tech.gusavila92.websocketclient.WebSocketClient;

//connecting: send uid (id of as app)
//after sending uid: you get same uid back from pi
//error can be send back: err
//JSON types have name of what it is
//

public class ChessApp extends FragmentActivity {

    AlertDialog.Builder dialog;
    WebSocketClient client;
    String colour;
    String username;
    String clock;

    // Clock features

    TextView U_clock;
    TextView C_clock;

    CountDownTimer U_timer;
    CountDownTimer C_timer;

    Long U_milliLeft;
    Long U_min;
    Long U_sec;

    Long C_milliLeft;
    Long C_min;
    Long C_sec;

    // Textview objects

    TextView previousMove;
    TextView turn_indicator;
    EditText fromCoordinate;
    EditText toCoordinate;
    Button confirmMove;

    HashMap<String, ImageView> positions;

    //  White pieces

    ImageView w_king;
    ImageView w_queen;
    ImageView w_L_bishop;
    ImageView w_R_bishop;
    ImageView w_L_knight;
    ImageView w_R_knight;
    ImageView w_L_rook;
    ImageView w_R_rook;

    ImageView w_A_pawn;
    ImageView w_B_pawn;
    ImageView w_C_pawn;
    ImageView w_D_pawn;
    ImageView w_E_pawn;
    ImageView w_F_pawn;
    ImageView w_G_pawn;
    ImageView w_H_pawn;

    //  Black pieces

    ImageView b_king;
    ImageView b_queen;
    ImageView b_L_bishop;
    ImageView b_R_bishop;
    ImageView b_L_knight;
    ImageView b_R_knight;
    ImageView b_L_rook;
    ImageView b_R_rook;

    ImageView b_A_pawn;
    ImageView b_B_pawn;
    ImageView b_C_pawn;
    ImageView b_D_pawn;
    ImageView b_E_pawn;
    ImageView b_F_pawn;
    ImageView b_G_pawn;
    ImageView b_H_pawn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chessboard);

        username = getIntent().getExtras().getString("username");
        clock = getIntent().getExtras().getString("clock");
        colour = getIntent().getExtras().getString("colour");
        dialog = new AlertDialog.Builder(ChessApp.this);

        // ---------------------------------------------------
        // ----                Objects                   -----
        // ---------------------------------------------------

        U_clock = findViewById(R.id.cb_U_clock);
        C_clock = findViewById(R.id.cb_C_clock);

        turn_indicator = findViewById(R.id.cb_move_indicator);
        previousMove   = findViewById(R.id.cb_previous_move);
        fromCoordinate = findViewById(R.id.cb_from_coordinate);
        toCoordinate   = findViewById(R.id.cb_to_coordinate);
        confirmMove    = findViewById(R.id.cb_confirm_move);

        // ---------------------------------------------------
        // ----              White Pieces                -----
        // ---------------------------------------------------

        w_king     = findViewById(R.id.cb_whiteKing);
        w_queen    = findViewById(R.id.cb_whiteQueen);
        w_L_bishop = findViewById(R.id.cb_left_whiteBishop);
        w_R_bishop = findViewById(R.id.cb_right_whiteBishop);
        w_L_knight = findViewById(R.id.cb_left_whiteKnight);
        w_R_knight = findViewById(R.id.cb_right_whiteKnight);
        w_L_rook   = findViewById(R.id.cb_left_whiteRook);
        w_R_rook   = findViewById(R.id.cb_right_whiteRook);

        w_A_pawn = findViewById(R.id.cb_A_whitePawn);
        w_B_pawn = findViewById(R.id.cb_B_whitePawn);
        w_C_pawn = findViewById(R.id.cb_C_whitePawn);
        w_D_pawn = findViewById(R.id.cb_D_whitePawn);
        w_E_pawn = findViewById(R.id.cb_E_whitePawn);
        w_F_pawn = findViewById(R.id.cb_F_whitePawn);
        w_G_pawn = findViewById(R.id.cb_G_whitePawn);
        w_H_pawn = findViewById(R.id.cb_H_whitePawn);


        // ---------------------------------------------------
        // ----              Black Pieces                -----
        // ---------------------------------------------------

        b_king     = findViewById(R.id.cb_blackKing);
        b_queen    = findViewById(R.id.cb_blackQueen);
        b_L_bishop = findViewById(R.id.cb_left_blackBishop);
        b_R_bishop = findViewById(R.id.cb_right_blackBishop);
        b_L_knight = findViewById(R.id.cb_left_blackKnight);
        b_R_knight = findViewById(R.id.cb_right_blackKnight);
        b_L_rook   = findViewById(R.id.cb_left_blackRook);
        b_R_rook   = findViewById(R.id.cb_right_blackRook);

        b_A_pawn = findViewById(R.id.cb_A_blackPawn);
        b_B_pawn = findViewById(R.id.cb_B_blackPawn);
        b_C_pawn = findViewById(R.id.cb_C_blackPawn);
        b_D_pawn = findViewById(R.id.cb_D_blackPawn);
        b_E_pawn = findViewById(R.id.cb_E_blackPawn);
        b_F_pawn = findViewById(R.id.cb_F_blackPawn);
        b_G_pawn = findViewById(R.id.cb_G_blackPawn);
        b_H_pawn = findViewById(R.id.cb_H_blackPawn);

        String IP = getIntent().getExtras().getString("IP");
        //createWebSocketClient.createClient(IP);
        client = createWebSocketClient.getWebSocketClient();

        createWebSocketClient.setChessApp(this);
        initializePositions();

        // ---------------------------------------------------
        // ----          Clock initialization            -----
        // ---------------------------------------------------
        /**

        if (!clock.equalsIgnoreCase("noClock")){
            findViewById(R.id.subtitle).setVisibility(View.GONE);
            previousMove.setVisibility(View.GONE);

            U_clock.setVisibility(View.VISIBLE);
            C_clock.setVisibility(View.VISIBLE);
            if(clock.equalsIgnoreCase("15min")) {

            } else {

            }
        }

         */
        // ---------------------------------------------------
        // ----             Button Handler               -----
        // ---------------------------------------------------

        confirmMove.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                String from = fromCoordinate.getText().toString();
                String to   = toCoordinate.getText().toString();

                JSONObject jsonObj = new JSONObject();

                try {
                    jsonObj.put("From", from);
                    jsonObj.put("To", to);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String move = jsonObj.toString();
                client.send(move);
            }
        });

    }

    public void startClocks(Long time){
        if (this.colour.equalsIgnoreCase("White")){
            userTimerStart(time);
            computerTimerStart(time);
            computerTimerPause();
        } else {
            userTimerStart(time);
            userTimerPause();
            computerTimerStart(time);
        }
    }

    /**
     * Actioned by server response, handles the move by the user
     * @param from the current coordinate of the piece
     * @param to the desired coordinate of the piece
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void checkMove(String from, String to, boolean validMove){
        if(validMove){
            movePiece(from, to);
        } else {
            dialog.setTitle("Invalid coordinates");
            dialog.setMessage("Please enter valid coordinates from this chessboard.");
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog alert = dialog.create();
            // Showing Alert Message
            alert.show();
        }
    }

    /**
     * Moves a piece to a desired position in the board
     * @param from the current coordinate of the piece
     * @param to the desired coordinate of the piece
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void movePiece(String from, String to){
        ImageView piece = positions.get(from);
        moveToCoordinate(to,piece);

        updatePreviousMove(from, to);

        // Update the chessboard accordingly
        positions.replace(from, null);
        positions.replace(to, piece);
    }

    /**
     * Updates the previous move description on screen
     * @param from the current coordinate of the piece
     * @param to the desired coordinate of the piece
     */
    @SuppressLint("SetTextI18n")
    public void updatePreviousMove(String from, String to){
        ImageView piece = positions.get(from);
        String pieceName = piece.getContentDescription().toString();
        previousMove.setText(pieceName + " to " + to);
    }

    /**
     * Avoid exiting the game by mistake
     */
    @Override
    public void onBackPressed() {
        dialog.setTitle("Cancel the game?");
        dialog.setMessage("Are you sure you want to quit the game?");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });

        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = dialog.create();

        // Showing Alert Message
        alert.show();

    }


    /**
     * Takes a piece and change the margins depending on the desired coordinate
     * @param coordinate where this piece should be now.
     * @param piece the piece that should be in that coordinate
     * @requires coordinate to be valid
     */
    public void moveToCoordinate(String coordinate, ImageView piece){
        DisplayMetrics dm = getResources().getDisplayMetrics();

        char X = coordinate.charAt(0);
        char Y = coordinate.charAt(1);

        int left_margin   = 0;
        int right_margin  = 0;
        int top_margin    = 0;
        int bottom_margin = 0;


        if (X == 'a' || X == 'A'){
            left_margin   = convertDpToPx(30, dm);
            right_margin  = convertDpToPx(283, dm);
        } else if (X == 'b' || X == 'B'){
            left_margin   = convertDpToPx(75, dm);
            right_margin  = convertDpToPx(283, dm);
        } else if (X == 'c' || X == 'C'){
            left_margin   = convertDpToPx(122, dm);
            right_margin  = convertDpToPx(283, dm);
        } else if (X == 'd' || X == 'D'){
            left_margin   = convertDpToPx(170, dm);
            right_margin  = convertDpToPx(283, dm);
        } else if (X == 'e' || X == 'E'){
            left_margin   = convertDpToPx(215, dm);
            right_margin  = convertDpToPx(283, dm);
        } else if (X == 'f' || X == 'F'){
            left_margin   = convertDpToPx(263, dm);
            right_margin  = convertDpToPx(283, dm);
        } else if (X == 'g' || X == 'G'){
            left_margin   = convertDpToPx(308, dm);
            right_margin  = convertDpToPx(283, dm);
        } else if (X == 'h' || X == 'H'){
            left_margin   = convertDpToPx(355, dm);
            right_margin  = convertDpToPx(283, dm);
        } else {
           Log.i("functionCoordinates","Something went wrong in coordinates X, with x: " + X + ", y: " + Y);
        }

        if (Y == '1'){
            top_margin    = convertDpToPx(380, dm);
        } else if (Y =='2'){
            top_margin    = convertDpToPx(335, dm);
        } else if (Y == '3'){
            top_margin    = convertDpToPx(280, dm);
        } else if (Y == '4'){
            top_margin    = convertDpToPx(230, dm);
        } else if (Y == '5'){
            top_margin    = convertDpToPx(180, dm);
        } else if (Y == '6'){
            top_margin    = convertDpToPx(130, dm);
        } else if (Y == '7'){
            top_margin    = convertDpToPx(75, dm);
        } else if (Y == '8'){
            top_margin    = convertDpToPx(20, dm);
        } else {
            Log.i("functionCoordinates","Something went wrong in coordinates X, with x: " + X + ", y: " + Y);
        }

        ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(piece.getLayoutParams());
        marginParams.setMargins(left_margin, top_margin, right_margin, bottom_margin);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(marginParams);
        piece.setLayoutParams(layoutParams);

    }

    /**
     * Function that returns the desired dimension
     * @param dp the actual dp that will be converted to pixels
     * @param displayMetrics class that converts to pixels
     * @return the desired dimension
     */
    private int convertDpToPx(int dp, DisplayMetrics displayMetrics) {
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
        return Math.round(pixels);
    }

    /**
     * Initialize the positions of this chessboard according to he standard rules
     */
    public void initializePositions(){
        positions = new HashMap<>();

        positions.put("a1", w_L_rook);
        positions.put("a2", w_A_pawn);
        positions.put("a3", null);
        positions.put("a4", null);
        positions.put("a5", null);
        positions.put("a6", null);
        positions.put("a7", b_A_pawn);
        positions.put("a8", b_L_rook);

        positions.put("b1", w_L_knight);
        positions.put("b2", w_B_pawn);
        positions.put("b3", null);
        positions.put("b4", null);
        positions.put("b5", null);
        positions.put("b6", null);
        positions.put("b7", b_B_pawn);
        positions.put("b8", b_L_knight);

        positions.put("c1", w_L_bishop);
        positions.put("c2", w_C_pawn);
        positions.put("c3", null);
        positions.put("c4", null);
        positions.put("c5", null);
        positions.put("c6", null);
        positions.put("c7", b_B_pawn);
        positions.put("c8", b_L_bishop);

        positions.put("d1", w_king);
        positions.put("d2", w_D_pawn);
        positions.put("d3", null);
        positions.put("d4", null);
        positions.put("d5", null);
        positions.put("d6", null);
        positions.put("d7", b_D_pawn);
        positions.put("d8", b_king);

        positions.put("e1", w_queen);
        positions.put("e2", w_E_pawn);
        positions.put("e3", null);
        positions.put("e4", null);
        positions.put("e5", null);
        positions.put("e6", null);
        positions.put("e7", b_E_pawn);
        positions.put("e8", b_queen);

        positions.put("f1", w_R_bishop);
        positions.put("f2", w_F_pawn);
        positions.put("f3", null);
        positions.put("f4", null);
        positions.put("f5", null);
        positions.put("f6", null);
        positions.put("f7", b_F_pawn);
        positions.put("f8", b_R_bishop);

        positions.put("g1", w_R_knight);
        positions.put("g2", w_G_pawn);
        positions.put("g3", null);
        positions.put("g4", null);
        positions.put("g5", null);
        positions.put("g6", null);
        positions.put("g7", b_G_pawn);
        positions.put("g8", b_R_knight);

        positions.put("h1", w_R_rook);
        positions.put("h2", w_H_pawn);
        positions.put("h3", null);
        positions.put("h4", null);
        positions.put("h5", null);
        positions.put("h6", null);
        positions.put("h7", b_H_pawn);
        positions.put("h8", b_R_rook);

    }

    /**
     * Set a connection, send moves and listen for server responses
     */
//    public void createClient() {
//        URI uri;
//        try {
//            // Connect to local host
//            uri = new URI("ws://127.0.0.1:8080");  //"ws://10.0.2.2:8080/websocket"
//        }
//        catch (URISyntaxException e) {
//            e.printStackTrace();
//            return;
//        }
//
//        client = new WebSocketClient(uri) {
//            @Override
//            public void onOpen() {
//                // Handshake?
//                Log.i("WebSocket", "Session is starting");
//                client.send("Start game!");
//            }
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onTextReceived(String s) {
//                Log.i("WebSocket", "Message received: " + s );
//
//                String responseType = null;
//                String from = null;
//                String to = null;
//                boolean valid = false;
//                String colour = null;
//
//                try {
//                    JSONArray jsonArray = new JSONArray(s);
//                    // TODO: Adjust these values according to the response format from server
//                    responseType = jsonArray.getString(0);
//                    from         = jsonArray.getString(1);
//                    to           = jsonArray.getString(2);
//                    valid        = Boolean.parseBoolean(jsonArray.getString(3));
//                    colour       = jsonArray.getString(4);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                if (responseType.equalsIgnoreCase("ai-move")){
//                    movePiece(from, to);
//                } else if (responseType.equalsIgnoreCase("user-move")){
//                    checkMove(from, to, valid);
//                } else if (responseType.equalsIgnoreCase("game-over")){
//                    displayWinner(colour);
//                }
//            }
//            @Override
//            public void onBinaryReceived(byte[] data) {
//            }
//            @Override
//            public void onPingReceived(byte[] data) {
//            }
//            @Override
//            public void onPongReceived(byte[] data) {
//            }
//            @Override
//            public void onException(Exception e) {
//                System.out.println(e.getMessage());
//            }
//            @Override
//            public void onCloseReceived() {
//                Log.i("WebSocket", "Closed ");
//                System.out.println("onCloseReceived");
//            }
//
//        };
//
//        client.setConnectTimeout(10000);
//        client.setReadTimeout(60000);
//        client.enableAutomaticReconnection(5000);
//        client.connect();



    /**
     * Starts the timer of the player: User
     * @param timeLengthMilli the desired start time
     */
    public void userTimerStart(long timeLengthMilli) {
        U_timer = new CountDownTimer(timeLengthMilli, 1000) {

            @Override
            public void onTick(long milliTillFinish) {
                U_milliLeft = milliTillFinish;
                U_min = (milliTillFinish / (1000 * 60));
                U_sec = ((milliTillFinish / 1000) - U_min * 60);
                U_clock.setText(Long.toString(U_min) + ":" + Long.toString(U_sec));
                Log.i("Tick", "Tock");
            }

            @Override
            public void onFinish() {
                // TODO
            }
        };
        U_timer.start();
    }

    /**
     * Pause the timer of the player: User
     */
    public void userTimerPause() {
        U_timer.cancel();
    }

    /**
     * Resumes the timer of the player: User
     */
    private void userTimerResume() {
        Log.i("min", Long.toString(U_min));
        Log.i("Sec", Long.toString(U_sec));
        userTimerStart(U_milliLeft);
    }

    /**
     * Starts the timer of the player: Computer
     * @param timeLengthMilli the desired start time
     */
    public void computerTimerStart(long timeLengthMilli) {
        C_timer = new CountDownTimer(timeLengthMilli, 1000) {

            @Override
            public void onTick(long milliTillFinish) {
                C_milliLeft = milliTillFinish;
                C_min = (milliTillFinish / (1000 * 60));
                C_sec = ((milliTillFinish / 1000) - C_min * 60);
                C_clock.setText(Long.toString(C_min) + ":" + Long.toString(C_sec));
                Log.i("Tick", "Tock");
            }

            @Override
            public void onFinish() {
                // TODO
            }
        };
        C_timer.start();
    }

    /**
     * Pauses the timer of the player: Computer
     */
    public void computerTimerPause() {
        C_timer.cancel();
    }

    /**
     * Resumes the timer of the player: Computer
     */
    private void computerTimerResume() {
        Log.i("min", Long.toString(C_min));
        Log.i("Sec", Long.toString(C_sec));
        userTimerStart(C_milliLeft);
    }

    /**
     * Display which player will play next
     * @param colour the color the next turn
     */
    @SuppressLint("SetTextI18n")
    public void changePlayerTurn(String colour){
        if (colour.equalsIgnoreCase(this.colour)){
            turn_indicator.setText("Your turn");
            turn_indicator.setBackground(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.rounded_corner_maroon));
        } else {
            turn_indicator.setText("Their turn");
            turn_indicator.setBackground(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.rounded_corner_gray));
        }
    }

    /**
     * Changes the layout in order to display the winner
     * @param colour winner of this game
     */
    public void displayWinner(String colour){
        if (colour.equalsIgnoreCase("white")){
            setContentView(R.layout.winners);
            TextView v = findViewById(R.id.w_white_text);
            v.setVisibility(View.VISIBLE);

        } else if (colour.equalsIgnoreCase("black")){
            setContentView(R.layout.winners);
            TextView v = findViewById(R.id.w_white_text);
            v.setVisibility(View.GONE);

        } else {
            Log.e("Error", "The colour specified could not be recognized as either black or white");
        }

        Button playAgain = findViewById(R.id.w_play_again);

        if (playAgain != null){
            playAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }


    /**
     * Handles the response form the server
     * @param obj the response form the server
     */
    public void serverHandler(JSONObject obj) {
        runOnUiThread (new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void run() {
                try {
                    // Getting coordinates
                    String from = obj.getString("From");
                    String to = obj.getString("To");
                    boolean valid = obj.getBoolean("Valid"); //show if it is invalid!
                    String color = obj.getString("Color");

                    // TODO: define functions that handles more actions
                    checkMove(from, to, valid);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }));
    }
}
