<?php

include("connection.php");

$uid = $_POST['uid'];
$productId = $_POST['pid'];
$productName = $_POST['pName'];
$quantity = $_POST['quantity'];
$price = $_POST['price'];

$sql ="INSERT INTO cart_tbl (uid,pid,pname,quantity,price) VALUES ('$uid','$productId','$productName','$quantity','$price')";

if(mysqli_query($con,$sql)){
	
	echo"success";
}

?>