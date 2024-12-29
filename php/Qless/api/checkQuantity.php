<?php

include("connection.php");

$productId = $_POST['pid'];
$quantity = $_POST['quantity'];

$sql = "SELECT * FROM products_tbl WHERE id='$productId'";
$result = mysqli_query($con,$sql);

if($row=mysqli_fetch_array($result)){

	$qty = $row['quantity'];

	if ($qty >= $quantity) {

		echo "success";

	}else{

		echo "failed";
	}
			  		
}

?>