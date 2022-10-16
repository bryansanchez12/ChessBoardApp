<?php

function getPasswordFromUser(){
    $conn = new mysqli('localhost', 'id17750788_group2', '#oX5YiZtyjijtSA', 'id17750788_tc_chessboard');
    $response = array();

    // The amount of sucks
    $username   = $_GET["username"];

    $stmt = $conn->prepare("SELECT password FROM Users WHERE username = '$username'");
    $stmt->execute();
    $result = $stmt->get_result();
    $outp = $result->fetch_all(MYSQLI_ASSOC);
    $conn->close();
    return json_encode($outp);
}

getPasswordFromUser();
?>