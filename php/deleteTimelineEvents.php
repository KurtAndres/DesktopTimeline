<?php 
//connecting to the db
$db = mysqli_connect('DB-LOCATION', 'DB-UserName', 'DB-Password',"DB-TableName");

//get uid
$uid = "'".$_GET['uid']."'";
//echo $uid;

if($uid != "''"){
	$result = mysqli_query($db, "SELECT * FROM timelines WHERE uid=".$uid);
	while($row = mysqli_fetch_assoc($result)){
		$php_delete = "DELETE FROM events WHERE tid = ".$row['tid'];
		mysqli_query($db,$php_delete);
		//echo "deleted events with uid: ".$uid;
		
		}	
		$php_delete = "DELETE FROM timelines WHERE uid = ".$uid;
		mysqli_query($db,$php_delete);
		//echo "Deleted timelines with uid: ".$uid;
	}
mysqli_close($db);

?>
