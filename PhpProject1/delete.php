<?php
        if ($_SERVER["REQUEST_METHOD"] == "POST") {
            
           
            // Create connection
            $conn = mysqli_connect("localhost", "root", "", "temp") or die("Connection failed: " . mysqli_connect_error());
            $id=$_POST["id"];

            $sql = "DELETE FROM tblstud WHERE id = '$id'";

            if (mysqli_query($conn, $sql)) {
                echo "Data Deleted successfully";
            } else {
                echo "Error: " . $sql . "<br>" . mysqli_error($conn);
            }

            mysqli_close($conn);

       }
     
?>
