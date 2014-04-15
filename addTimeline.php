<?php
//connecting to the db
$db = mysqli_connect('csdb.wheaton.edu', 'TimelineAdmin', 'kurtconner',"Timeline");

//get uid
$uid = "'".$_GET['uid']."'";
//echo $uid;

//get timeline name
$name = "'".$_GET['name']."'";
//echo $name;

if($uid!= "''" | $name != "''"){
	$sql="INSERT INTO timelines(uid, name) VALUES (".$uid.",".$name.")";
	mysqli_query($db, $sql);
	echo "added";
	}else {
		echo "Not valid php entry";
		}
mysqli_close($db);
?>
