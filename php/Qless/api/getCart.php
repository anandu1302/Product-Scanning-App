<?php

include("connection.php");

$uid=$_GET['uid'];


$sql = "Select * from cart_tbl where uid='$uid'";
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