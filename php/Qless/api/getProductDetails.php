<?php

include("connection.php");

$productId=$_POST['productId'];
//$productId="1";


$sql = "Select * from products_tbl where product_code='$productId'";
$result = mysqli_query($con,$sql);

if(mysqli_num_rows($result) > 0){

	while($row = mysqli_fetch_assoc($result)){

       $data['data'][] = $row;
		
	}
	echo json_encode($data);
}

else{
	echo "failed";
}

?>