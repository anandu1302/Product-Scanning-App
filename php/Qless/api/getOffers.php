<?php

include("connection.php");


$sql = "Select * from offer_tbl";
$result = mysqli_query($con,$sql);

if(mysqli_num_rows($result) > 0){

	while($row = mysqli_fetch_assoc($result)){

	$ress = mysqli_query($con,"SELECT * FROM products_tbl WHERE id = '$row[pid]'");
		$roww = mysqli_fetch_assoc($ress);

		$data['data'][] = array('id'=>$row['id'],'pid'=>$row['pid'],'offer_price'=>$row['price'],'product_name' => $roww['pname'], 'price' => $roww['price']);

		
	}
	echo json_encode($data);
}

else{
	echo "failed";
}

?>