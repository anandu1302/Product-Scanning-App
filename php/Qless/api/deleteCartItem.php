<?php

include("connection.php");

 $id=$_POST['pid'];

   $res1=mysqli_query($con,"delete from cart_tbl where id='$id'");
	$res=mysqli_query($con,"select * from cart_tbl where id='$id'");
	if(mysqli_num_rows($res)>0){
		echo "failed";
	
	}
	else{
		
		echo "success";
	}


?>