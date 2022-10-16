<?php

function login(){
    // Prevention of SQL injection
    
    // User credentials
    $username = $_GET["username"];
    $password = $_GET["password"];
    
    // Get the hash stored in the Database
    $hash = getHashFromUser($username);
    
    if (password_verify($password, $hash)) {
        echo "true";
    } else {
        echo "false";
    }
}

function getHashFromUser($username){
    $dsn = "mysql:host=localhost;dbname=id17750788_tc_chessboard;charset=utf8mb4";
    $options = [
      PDO::ATTR_EMULATE_PREPARES   => false, // turn off emulation mode for "real" prepared statements
      PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION, //turn on errors in the form of exceptions
      PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC, //make the default fetch be an associative array
    ];
    
    $conn = new PDO($dsn, 'id17750788_group21', '#oX5YiZtyjijtSA', $options);
    $conn -> setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $response = array();

    // Prepared statement that returns the password of such a username
    $query = "SELECT password FROM Users WHERE username = ?";
    $stmt = $conn->prepare($query);
    $stmt -> execute([$username]);

    $hash = $stmt->fetch(PDO::FETCH_COLUMN);


    //$stmt -> bind_param(':username', $username);
    // $result = $conn -> query($query) or die(mysqli_error($conn));
    //$result = $stmt->get_result(); 
    //$data = $result->fetch_assoc();
    
    $conn = null;
    
    //$hash = $data["password"];
    return $hash;
}

login();
?>