<?php

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $id = $_POST["id"];
    $StudentName = $_POST["StudentName"];
    $City = $_POST["City"];
    $ContactNo = $_POST["ContactNo"];

    // Create connection
    $conn = mysqli_connect("localhost", "root", "", "temp") or die("Connection failed: " . mysqli_connect_error());

    $sql = "update tblstud set StudentName='$StudentName' , City='$City', ContactNo=$ContactNo where id=$id ";

    if (mysqli_query($conn, $sql)) {
        echo "Record updated successfully";
    } else {
        echo "Error: " . $sql . "<br>" . mysqli_error($conn);
    }
    mysqli_close($conn);
}