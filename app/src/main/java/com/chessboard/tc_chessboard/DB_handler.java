package com.chessboard.tc_chessboard;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DB_handler extends AsyncTask< String, Void, Void> {

    // DB credentials           PhPAdmin
    // username = "id17750788_group21"
    // password = "#oX5YiZtyjijtSA"
    // dbScheme = "id17750788_tc_chessboard"


    // URL from the php files
    private String URL = "https://tc-chessboard.000webhostapp.com//";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }

    @SuppressLint("LongLogTag")
    @Override
    protected Void doInBackground(String... arg) {

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        // Page identifier
        String page = arg[0];
        params.add(new BasicNameValuePair("formType", arg[1]));

        // Username from input
        params.add(new BasicNameValuePair("username", arg[2]));

        if (page.equalsIgnoreCase("start_page")) {
            // Password from input
            params.add(new BasicNameValuePair("password", arg[3]));

        } else if (page.equalsIgnoreCase("forgot_password")) {
            // Email address input
            params.add(new BasicNameValuePair("email_address", arg[3]));

        } else if (page.equalsIgnoreCase("create_user")) {
            // User credentials
            params.add(new BasicNameValuePair("email_address", arg[3]));
            params.add(new BasicNameValuePair("password", arg[4]));

        } else {
            return null;
        }

        ServiceHandler serviceClient = new ServiceHandler();
        String json = serviceClient.makeServiceCall(URL, ServiceHandler.POST, params);


        Log.d("## Submission Request: ", "> " + json);

        // Checking the JSON data
        if (json != null) {
            try {
                JSONObject jsonObj = new JSONObject(json);
                boolean error = jsonObj.getBoolean("error");
                // Search for error node in json
                if (!error) {
                    // No error, hence data submitted successfully
                    Log.e("$$ Data submitted successfully!",
                            "> " + jsonObj.getString("message"));
                } else {
                    Log.e("$$ Error in submission: ",
                            "> " + jsonObj.getString("message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.e("$$ JSON Data", "Error!, JSON data corrupted");
        }

        return null;
    }

    /**
     * Sends a HTTP request to a php file that fetches data from the Database
     *
     * @return a JSON object with the required information
     */
    public boolean verifyUserCredentialsFromDB(String username, String password) {

        String URL_withParam = URL + "login.php?username=" + username + "&password=" + password;

        ServiceHandler serviceClient = new ServiceHandler();
        String response = serviceClient.sendLoginRequest(URL_withParam,
                ServiceHandler.GET, null);

        Log.d("## Submission Request: ", "Response >" + response.trim() + "<");

        String bool = response.trim();

        // Checking the JSON data
        if (!bool.equalsIgnoreCase("")) {
            return Boolean.parseBoolean(bool);
        } else {
            Log.e("$$ JSON Data", "Error!, JSON data corrupted");
        }

        return false;

    }

    public String getIPAddress(String boardID) {

        return "192.168.203.128"; //TODO: change this back! Where is the database
//        String URL_withParam = URL+"getIPaddress.php?boardID=" + boardID;
//
//        ServiceHandler serviceClient = new ServiceHandler();
//        String response = serviceClient.sendLoginRequest(URL_withParam,
//                ServiceHandler.GET, null);
//
//        Log.d("## Submission Request: ", "Response >" + response.trim() + "<" );
//
//        String ip = response.trim();
//
//        // Checking the JSON data
//        if (!response.equalsIgnoreCase("")) {
//            return ip;
//        } else {
//            Log.e("$$ JSON Data", "Error!, JSON data corrupted");
//        }
//
//        return null;

    }

    @SuppressLint("LongLogTag")
    public boolean registerNewUser(String username, String email_address, String password) {

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        // Username from input
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("email_address", email_address));
        params.add(new BasicNameValuePair("password", password));

        ServiceHandler serviceClient = new ServiceHandler();
        String json = serviceClient.makeServiceCall(URL + "registerNewUser.php",
                ServiceHandler.POST, params);


        Log.d("## Submission Request: ", "> " + json);

        // Checking the JSON data
        if (json != null) {
            try {
                JSONObject jsonObj = new JSONObject(json);
                boolean error = jsonObj.getBoolean("error");
                // Search for error node in json
                if (!error) {
                    // No error, hence data submitted successfully
                    Log.e("$$ Data submitted successfully!",
                            "> " + jsonObj.getString("message"));
                    return true;
                } else {
                    Log.e("$$ Error in submission: ",
                            "> " + jsonObj.getString("message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.e("$$ JSON Data", "Error!, JSON data corrupted");
        }

        return false;
    }

    /**
     * Send a HTTP request to register a new board to an admin user
     *
     * @param username
     * @param code
     * @return
     */
    @SuppressLint("LongLogTag")
    public boolean registerBoard(String username, String code, String boardID) throws IllegalAccessException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("boardID", boardID));
        params.add(new BasicNameValuePair("code", code));

        ServiceHandler serviceClient = new ServiceHandler();
        String response = serviceClient.makeServiceCall(URL + "registerBoard.php",
                ServiceHandler.POST, params);

        Log.d("## Submission Request: ", "> " + response);

        // Checking the JSON data
        if (response != null) {
            try {
                JSONObject jsonObj = new JSONObject(response);
                boolean error = jsonObj.getBoolean("error");
                // Search for error node in json
                if (!error) {
                    // No error, hence data submitted successfully
                    Log.e("$$ Data submitted successfully!",
                            "> " + jsonObj.getString("message"));
                    return true;
                } else {
                    Log.e("$$ Error in submission: ",
                            "> " + jsonObj.getString("message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
                throw new IllegalAccessException();
            }

        } else {
            Log.e("$$ JSON Data", "Error!, JSON data corrupted");
        }

        return false;

    }

    public boolean isAdminUser(String username) {

        String URL_withParam = URL + "isAdminUser.php?username=" + username;

        ServiceHandler serviceClient = new ServiceHandler();
        String response = serviceClient.sendLoginRequest(URL_withParam,
                ServiceHandler.GET, null);

        Log.d("## Submission Request: ", "Response >" + response.trim() + "<");

        String bool = response.trim();

        // Checking the JSON data
        if (!bool.equalsIgnoreCase("")) {
            return Boolean.parseBoolean(bool);
        } else {
            Log.e("$$ JSON Data", "Error!, JSON data corrupted");
        }

        return false;
    }

    public String getBoardID(String username) {

        String URL_withParam = URL + "getBoardID.php?username=" + username;

        ServiceHandler serviceClient = new ServiceHandler();
        String response = serviceClient.sendLoginRequest(URL_withParam,
                ServiceHandler.GET, null);

        Log.d("## Submission Request: ", "Response >" + response.trim() + "<");

        String id = response.trim();

        // Checking the JSON data
        if (!id.equalsIgnoreCase("")) {
            return id;
        } else {
            Log.e("$$ JSON Data", "Error!, JSON data corrupted");
        }

        return null;
    }

    @SuppressLint("LongLogTag")
    public boolean addNewUserToBoard(String username, String boardID) throws IllegalAccessException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("boardID", boardID));

        ServiceHandler serviceClient = new ServiceHandler();
        String response = serviceClient.makeServiceCall(URL + "addUser.php",
                ServiceHandler.POST, params);

        Log.d("## Submission Request: ", "> " + response);

        // Checking the JSON data
        if (response != null) {
            try {
                JSONObject jsonObj = new JSONObject(response);
                boolean error = jsonObj.getBoolean("error");
                // Search for error node in json
                if (!error) {
                    // No error, hence data submitted successfully
                    Log.e("$$ Data submitted successfully!",
                            "> " + jsonObj.getString("message"));
                    return true;
                } else {
                    Log.e("$$ Error in submission: ",
                            "> " + jsonObj.getString("message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
                throw new IllegalAccessException();
            }

        } else {
            Log.e("$$ JSON Data", "Error!, JSON data corrupted");
        }

        return false;

    }

    @SuppressLint("LongLogTag")
    public List<String> getUsersRegistered(String boardID) {
        List<String> result = new ArrayList<>();

        String URL_withParam = URL + "getUsersRegistered.php?boardID=" + boardID;

        ServiceHandler serviceClient = new ServiceHandler();
        String response = serviceClient.sendLoginRequest(URL_withParam,
                ServiceHandler.GET, null);

        Log.d("## Submission Request: ", "Response >" + response + "<");

        // Checking the JSON data
        if (response != null) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0 ; i < jsonArray.length() ; i++){
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    String username = jsonObj.getString("username");
                    result.add(username);
                }
                return result;

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.e("$$ JSON Data", "Error!, JSON data corrupted");
        }

        return null;
    }
}
