<?php

include('connection.php');

$uid = $_POST['uid'];

//$uid ="1";

$sql = "SELECT SUM(points) AS cc FROM points_tbl WHERE uid='$uid'";

$res = mysqli_query($con,$sql);
$row = mysqli_fetch_array($res);

$cc = $row['cc'];

if ($cc === null || $cc === 0) {
    $cc = 0;
}

echo $cc;

?>