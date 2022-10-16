<?php

function registerNewUser(){
    $conn = new mysqli('localhost', 'id17750788_group21', '#oX5YiZtyjijtSA', 'id17750788_tc_chessboard');
    $response = array();

    // User credentials
    $username      = $_POST["username"];
    $email_address = $_POST["email_address"];
    $password_temp = $_POST["password"];
    
    // Create a hash from the given password
    $password =  password_hash($password_temp, PASSWORD_DEFAULT);

    $query  = "INSERT INTO Users(username,email_address,password) VALUES('$username','$email_address','$password');";
    $result = $conn -> query($query) or die(mysqli_error($conn));
    if ($result) {
        $response["error"] = false;
        $response["message"] = "User $username was registered successfully!";
    } else {
        $response["error"] = true;
        $response["message"] = "Failed to register user with username: $username!";
    }

    $conn->close();
    echo json_encode($response);
}

registerNewUser();
?>