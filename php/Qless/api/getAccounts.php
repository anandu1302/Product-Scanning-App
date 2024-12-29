<?php
 
include("connection.php");

$uid=$_REQUEST['uid'];

$sql = "SELECT * FROM account_tbl WHERE uid ='$uid'";
$result = mysqli_query($con,$sql);

if(mysqli_num_rows($result) > 0){

	while($row = mysqli_fetch_assoc($result))
		$data["data"][] = $row;
	echo json_encode($data);
}
else{
	echo "failed";
}

?>