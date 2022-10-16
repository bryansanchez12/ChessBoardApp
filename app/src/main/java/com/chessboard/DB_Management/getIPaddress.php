<?php

function getIP_address(){
    $conn = new mysqli('localhost', 'id17750788_group21', '#oX5YiZtyjijtSA', 'id17750788_tc_chessboard');
    $response = array();

    // The boardID
    $boardID   = $_GET["boardID"];

    $stmt = $conn->prepare("SELECT IPaddress FROM Boards WHERE boardID = '$boardID'");
    $stmt->execute();
    $result = $stmt->get_result();
    $outp = mysqli_fetch_assoc($result);
    $conn->close();
    $IP_address = $outp['IPaddress'];
    echo $IP_address;
}

getIP_address();
?>