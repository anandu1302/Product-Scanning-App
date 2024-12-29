<?php

include("connection.php");


$uid=$_GET['uid'];
$pin=$_GET['pin'];
$acc=$_GET['acc'];
$am=$_GET['am'];


$date=date('Y-m-d');

 $sel1 = "Select * from account_tbl where accountno='$acc'";
 $res1 = mysqli_query($con,$sel1);
 
 if(mysqli_num_rows($res1)>0){

 	while($row1=mysqli_fetch_array($res1)){

 		$p=$row1["pin"];

 		if($p==$pin){
		    $amount=$row1["amount"];
			if($amount>=$am){
			  $a=$amount-$am;

			  //echo $a;
			  $sql2 = "Update account_tbl set amount='$a' where accountno='$acc'";
			  mysqli_query($con,$sql2);

			  $sel2 ="Select * from cart_tbl where uid='$uid'";
			  $res2 = mysqli_query($con,$sel2);

			  if(mysqli_num_rows($res2)>0){

			  	$sql3 = "INSERT INTO bill_tbl(uid,date) values('$uid','$date')";
			  	$res3 = mysqli_query($con,$sql3);

			  	$sel33 ="Select * from bill_tbl where uid='$uid'";
			  	$res33 = mysqli_query($con,$sel33);

			  	if($row33=mysqli_fetch_array($res33)){
			  		$six_digit_random_number = mt_rand(100000, 999999);
				    $bid=$six_digit_random_number;
			  		//$bid=$row33["id"];
				}

				while ($row4=mysqli_fetch_array($res2)) {

                 //$date = date('Y-m-d');

					$sql5 ="INSERT INTO history_tbl (uid,pname,quantity,price,date,bill_id) VALUES ('$uid','$row4[pname]','$row4[quantity]','$row4[price]','$date','$bid')";
 					mysqli_query($con,$sql5);

					//echo $ress;


				}

				$sql6 ="INSERT INTO payment_tbl (bill_id,accname,amount,date,status) VALUES ('$bid','$row1[accname]','$am','$date','paid')";

					mysqli_query($con,$sql6);

					

				mysqli_query($con,"delete from cart_tbl where uid='$uid'");

			  }

			  echo "success";

			}

			else{
				echo "failed";	
				}
			}

			else{
					echo "pin";
				}
		}

 	}
  
  else{
	echo "accerror";	
	}	   
?>