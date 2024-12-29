<?php

include("connection.php");

$uid = $_POST['uid'];
$name = $_POST['cName'];
$email = $_POST['cEmail'];
$username = $_POST['cUsername'];
$number = $_POST['cNumber'];
$address = $_POST['address'];


$sql = "UPDATE register SET name='$name',email='$email',username='$username',phone='$number', address='$address' WHERE id='$uid'";

//echo $sql;
        
 if(mysqli_query($con,$sql)){
    echo "Successfully Updated";
}else{
	echo"Failed to Update Profile";
}

?>