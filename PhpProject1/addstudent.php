<?php
        if ($_SERVER["REQUEST_METHOD"] == "POST") {
            $StudentName = $_POST["StudentName"];
            $City = $_POST["City"];
            $ContactNo = $_POST["ContactNo"];

            // Create connection
            $conn = mysqli_connect("localhost", "root", "", "temp") or die("Connection failed: " . mysqli_connect_error());

            $sql = "INSERT INTO `tblstud` (`id`, `StudentName`, `City`, `ContactNo`) VALUES (NULL, '$StudentName','$City','$ContactNo'  );";

            if (mysqli_query($conn, $sql)) {
                echo "Data Added successfully";
            } else {
                echo "Error: " . $sql . "<br>" . mysqli_error($conn);
            }

            mysqli_close($conn);

       }
     
?>
