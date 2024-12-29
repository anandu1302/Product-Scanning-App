  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="bootstrap.min.css">
  <script src="jquery.min.js"></script>
  <script src="bootstrap.min.js"></script>
</head>
<style>
.col-sm-4
{

}
.pad
{
border:1px solid #CCC;	
margin-bottom:10px;	
padding:10px;
}
.right
{
float:right;	
}
</style>

<script>
function myFunction(i) {
	var y=i;
	//alert(y);
    var x = document.getElementById("demo").innerHTML+document.getElementById("myBtn"+i).value;
	document.getElementById("myText").value = x;
    document.getElementById("demo").innerHTML = x;
	if(x.length==4){
		android.showToast(x);
	}
		//window.location="https://www.google.co.in?q="+x;
}
</script>
<br>

<center><h3 style="color: black;">Enter 4-Digit Pin</h3></center>

<div class="container"><br>
<form action="" method="get">


<?php
$str = '123456789';
$shuffled = str_shuffle($str);

// This will echo something like: bfdaec
//echo $shuffled;

$j=10;
for($i=0;$i<9;$i++)
{
	$j=$j-1;
	$n="myBtn".$i;
	echo "<div class='col-xs-4'><div class='pad'><input type='button' value='$shuffled[$i]' onclick='myFunction($i)' name='a' id='$n'> <span class='right'> </span></div></div>";
}
?>

<center><input type='text'   name='aa' id='myText'></center>
</form>
<p id="demo"></p>
</div>