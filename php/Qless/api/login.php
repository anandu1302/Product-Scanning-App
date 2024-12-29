<?php

include("connection.php");

$username=$_POST['username'];
$password=$_POST['password'];

/*$email="fz@gmail.com";
$password="fayiz";*/

$sqli ="SELECT * FROM register where username='$username' and password='$password'";


$result = mysqli_query($con,$sqli);


if(mysqli_num_rows($result)>0){
	
	$row=mysqli_fetch_assoc($result);
	$data[]=array('id'=> $row['id'],'name'=>$row['name'],'email'=>$row['email']);
	
	echo json_encode($data);

}
else{
	echo "failed";
}


?>