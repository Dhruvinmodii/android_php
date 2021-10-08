<?php
            $con = mysqli_connect("localhost", "root", "", "temp") or die("Connection failed: " . mysqli_connect_error());
            $result= mysqli_query($con, "select * from tblstud");
            $data=array();
             while($row = mysqli_fetch_object($result)){
                 $data['students'][] = array(
                     "id" =>$row->id,
                     "StudentName"=>$row->StudentName,
                     "City"=>$row->City,
                     "ContactNo"=>$row->ContactNo
                 );
             }
           
             echo json_encode($data);
             
        ?>

