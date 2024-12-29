<?php

include("connection.php");

$uid = $_REQUEST['uid'];


$sql = "Select * from cart_tbl Where uid='$uid'";
$result = mysqli_query($con,$sql);

if(mysqli_num_rows($result) > 0){

	while($row = mysqli_fetch_assoc($result)){
		$product_id = $row['pid'];

		//Check if the product available in the offer_tbl

        $res2 = mysqli_query($con,"SELECT * FROM offer_tbl WHERE pid = '$row[pid]'");
		

		if (mysqli_num_rows($res2) > 0) {

			//if the product is available in offer_tbl

			$row2 = mysqli_fetch_assoc($res2);
			
           $ress = mysqli_query($con,"SELECT * FROM products_tbl WHERE id = '$row[pid]'");
		$roww = mysqli_fetch_assoc($ress);

		$data['data'][] = array('id'=>$row['id'],'offer_price'=>$row['price'],'product_name' => $roww['pname'],'qty'=>$row['quantity'], 'price' => $row['price']);

		}else{

			//if the product is not available in offer_tbl

			 $ress = mysqli_query($con,"SELECT * FROM products_tbl WHERE id = '$row[pid]'");
		$roww = mysqli_fetch_assoc($ress);

		$data['data'][] = array('id'=>$row['id'],'offer_price'=>$row['price'],'product_name' => $roww['pname'],'qty'=>$row['quantity'], 'price' => $row['price']);

		}

	

		
	}
	echo json_encode($data);
}

else{
	echo "failed";
}

?>