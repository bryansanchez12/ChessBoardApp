<?php

function registerBoard(){
    $conn = new mysqli('localhost', 'id17750788_group21', '#oX5YiZtyjijtSA', 'id17750788_tc_chessboard');
    $response = array();

    // User credentials
    $username = $_POST["username"];
    $boardID  = $_POST["boardID"];
    $code     = $_POST["code"];
    
    $dbCode = getCodeDB($boardID, $conn);
    
    if ($outp == $code){
        $Userid = getUserID($username, $conn);
        $Bid    = getBoardID($boardID, $conn);
    
        $query  = "INSERT INTO UserBoards(Userid,Bid,admin) VALUES('$Userid','$Bid','true');";
        $result = $conn -> query($query) or die(mysqli_error($conn));
        if ($result) {
            $response["error"] = false;
            $response["message"] = "Board $boardID was registered successfully!";
        } else {
            $response["error"] = true;
            $response["message"] = "Failed to register admin user with username: $username!";
        }
    } else {
        $response["error"] = true;
        $response["message"] = "Failed to register admin user with username: $username!";
    }
    
    
    $conn->close();
    echo $response;
}

function getUserID($username, $conn){
    // Query that returns the User id from such a user
    $query = "SELECT Userid FROM Users WHERE username = '$username'";
    
    $result = $conn -> query($query) or die(mysqli_error($conn));
    $data = mysqli_fetch_assoc($result);
    
    $id = $data["Userid"];
    return $id;
}

function getBoardID($boardID, $conn){
    // Query that returns the User id from such a user
    $query = "SELECT Bid FROM Boards WHERE boardID = '$boardID''";
    
    $result = $conn -> query($query) or die(mysqli_error($conn));
    $data = mysqli_fetch_assoc($result);
    
    $id = $data["Bid"];
    return $id;
}

function getCode($boardID, $conn){
    // Query that returns the User id from such a user
    $query = "SELECT registration_code FROM Boards WHERE boardID = '$boardID'";
    
    $result = $conn -> query($query) or die(mysqli_error($conn));
    $data = mysqli_fetch_assoc($result);
    
    $id = $data["registration_code"];
    return $id;
}

registerBoard();
?>