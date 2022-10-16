package com.chessboard.tc_chessboard;

public class User_Model {
    String userName;
    String BoardID;

    public User_Model(String userName, String boardID){
        this.userName = userName;
        this.BoardID  = boardID;
    }

    public String getBoardID() {
        return BoardID;
    }

    public String getUserName() {
        return userName;
    }

    public void setBoardID(String boardID) {
        BoardID = boardID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isUserNameEquals(String userName){
        return this.userName.equalsIgnoreCase(userName);
    }
}
