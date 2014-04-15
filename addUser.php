<?php
//connecting to the db
$db = mysqli_connect('csdb.wheaton.edu', 'TimelineAdmin', 'kurtconner',"Timeline");

//get the name
$name = "'".$_GET['name']."'";
//echo 'Name: '.$name;

//get the password
$password = " '".$_GET['password']."'";
//echo ' Password: '.$password.' ';

//query for specific user and password
$result = mysqli_query($db, "SELECT * FROM userAdmin WHERE name=".$name);
$row = mysqli_fetch_array($result);
if(" '".$row['password']."'" == $password){
	//user and password in db so we return uid
	
	$uid = $row[uid];
	echo "Welcome, your UID: ".$uid;
	}else {
		//user password combo not in db so we add them
		$sql="INSERT INTO userAdmin(name, password) VALUES (".$name.",".$password.")";
		mysqli_query($db, $sql);
		echo "added";
		}
mysqli_close($db);
?>
		
