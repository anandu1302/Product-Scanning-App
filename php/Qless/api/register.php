<?php

include("connection.php");

$name = $_POST['name'];
$email = $_POST['email'];
$username = $_POST['username'];
$password = $_POST['password'];
$address = $_POST['address'];
$phone = $_POST['phone'];
$accno = $_POST['accno'];

$sql ="INSERT INTO register (name,email,username,password,address,phone,accno) VALUES ('$name','$email','$username','$password','$address','$phone','$accno')";

if(mysqli_query($con,$sql)){
	
	echo"success";
}
else{
	
	echo"failed";
}


?>